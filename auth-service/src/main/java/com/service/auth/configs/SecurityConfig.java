package com.service.auth.configs;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.auth.securities.JwtAuthenticationFilter;
import com.service.auth.securities.JwtTokenProvider;
import com.service.auth.services.impl.CustomUserDetailsService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Autowired
  private JwtTokenProvider jwtTokenProvider;

  @Autowired
  private CustomUserDetailsService customUserDetailsService;

  private static final String[] PUBLIC_MATCHERS = {
      "/auth/login",
      "/auth/register",
      "/users/add",
      "authenticated/validate"
  };

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    http.authorizeHttpRequests(request -> request
        .requestMatchers(PUBLIC_MATCHERS).permitAll()
        .anyRequest().authenticated());

    http.csrf(AbstractHttpConfigurer::disable);

    http.exceptionHandling(ex -> ex
        .authenticationEntryPoint((request, response, authException) -> handleErrorResponse(response,
            request, HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized", authException.getMessage()))
        .accessDeniedHandler((request, response, accessDeniedException) -> handleErrorResponse(response,
            request, HttpServletResponse.SC_FORBIDDEN, "Forbidden", accessDeniedException.getMessage())));

    http.addFilterBefore(
        new JwtAuthenticationFilter(jwtTokenProvider, customUserDetailsService),
        UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  /**
   * Phương thức chung để xử lý lỗi xác thực và truy cập bị từ chối.
   * 
   * @throws java.io.IOException
   */
  private void handleErrorResponse(
      HttpServletResponse response,
      HttpServletRequest request,
      int status,
      String error,
      String message)
      throws IOException {
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
