package com.service.auth.configs;

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

import com.service.auth.securities.JwtAuthenticationFilter;
import com.service.auth.securities.JwtTokenProvider;
import com.service.auth.services.impl.CustomUserDetailsService;

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
      "/users/add"
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

    // http.oauth2ResourceServer(oauth2 ->
    // oauth2.jwt(JwtConfigurer ->
    // JwtConfigurer.decoder(jwtTokenProvider.jwtDecoder()))
    // );

    http.csrf(AbstractHttpConfigurer::disable);

    http.addFilterBefore(
        new JwtAuthenticationFilter(jwtTokenProvider, customUserDetailsService),
        UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

}
