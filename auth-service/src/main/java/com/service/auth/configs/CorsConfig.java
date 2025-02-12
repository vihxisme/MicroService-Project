package com.service.auth.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

  @Bean
  public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("http://localhost:8000")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD")
            .allowedHeaders("Authorization", "Content-Type", "Accept", "X-Requested-With", "remember-me")
            .allowCredentials(true);
      }
    };
  }

}
