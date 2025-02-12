package com.service.auth.securities;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.auth.services.impl.CustomUserDetailsService;

import io.micrometer.common.lang.NonNull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final static Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

  private final JwtTokenProvider jwtTokenProvider;
  private final CustomUserDetailsService customUserDetailsService;

  private final RequestMatcher requestMatcher = new OrRequestMatcher(
      new AntPathRequestMatcher("/auth/login"),
      new AntPathRequestMatcher("/users/add"),
      new AntPathRequestMatcher("/auth/register"));

  @Autowired
  public JwtAuthenticationFilter(
      JwtTokenProvider jwtTokenProvider,
      CustomUserDetailsService customUserDetailsService) {
    this.jwtTokenProvider = jwtTokenProvider;
    this.customUserDetailsService = customUserDetailsService;
  }

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain)
      throws ServletException, IOException {
    try {
      // String path = request.getServletPath();

      if (requestMatcher.matches(request)) {
        filterChain.doFilter(request, response);
        return;
      }

      final String token = jwtTokenProvider.resolveToken(request.getHeader("Authorization"));

      if (token != null && jwtTokenProvider.validateToken(token)) {
        String userId = jwtTokenProvider.getUserIdFromJWT(token);
        if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
          UserDetails userDetails = customUserDetailsService.loadUserByUsername(userId);
          UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
              userDetails,
              null,
              userDetails.getAuthorities());
          authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          SecurityContextHolder.getContext().setAuthentication(authentication);
        }
      } else {
        logger.error("Cannot set user authentication in security context");
        sendErrorResponse(
            response,
            request,
            HttpServletResponse.SC_UNAUTHORIZED,
            "Unauthorized",
            "Unauthorized");
      }

      filterChain.doFilter(request, response);
    } catch (ServletException | IOException | UsernameNotFoundException e) {
      logger.error("Could not set user authentication in security context", e);
      sendErrorResponse(
          response,
          request,
          HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
          "Cannot process request",
          e.getMessage());
    }
  }

  @SuppressWarnings("unused")
  private void sendErrorResponse(
      @NonNull HttpServletResponse response,
      @NonNull HttpServletRequest request,
      @NonNull int status,
      @NonNull String error,
      @NonNull String message) throws IOException {

    response.setStatus(status);
    response.setContentType("application/json");

    Map<String, Object> errorResponse = new HashMap<>();
    errorResponse.put("Timestamp", System.currentTimeMillis());
    errorResponse.put("Status", status);
    errorResponse.put("Error", error);
    errorResponse.put("Message", message);
    errorResponse.put("Path", request.getRequestURI());

    String json = new ObjectMapper().writeValueAsString(errorResponse);

    response.getWriter().write(json);

  }
}
