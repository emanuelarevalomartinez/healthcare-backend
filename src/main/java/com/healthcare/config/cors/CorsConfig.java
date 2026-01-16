package com.healthcare.config.cors;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();

        // 🔐 Orígenes permitidos
        config.setAllowedOrigins(List.of(
                "http://localhost:3000",
                "http://localhost:4200"
        ));

        // 🔐 Métodos permitidos
        config.setAllowedMethods(List.of(
                "GET", "POST", "PUT", "DELETE"
        ));

        // 🔐 Headers permitidos
        config.setAllowedHeaders(List.of(
                "Authorization",
                "Content-Type"
        ));

        // 🔐 Permitir cookies / Authorization header
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}
