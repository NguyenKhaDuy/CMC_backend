package org.example.cmc_backend.Config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.example.cmc_backend.JwtTokenFilter.JwtTokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableWebMvc
@RequiredArgsConstructor
@EnableMethodSecurity
public class WebSecurityConfig {
    private final JwtTokenFilter jwtTokenFilter;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity){
        try {
            httpSecurity.cors(Customizer.withDefaults())
                    .csrf(AbstractHttpConfigurer::disable)
                    .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                    .authorizeHttpRequests(requests->{
                        requests.requestMatchers("/api/**").permitAll()
                                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                                .requestMatchers("/api/customer/**").hasRole("CUSTOMER")
                                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                                .anyRequest().authenticated();
                    })
                    .formLogin(Customizer.withDefaults());
            return httpSecurity.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of(
                "http://localhost:3001"
        ));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    private static final String SECURITY_SCHEME_NAME = "bearerAuth";

    @Bean
    public OpenAPI greenyOpenApi() {
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                .components(new Components().addSecuritySchemes(
                        SECURITY_SCHEME_NAME,
                        new SecurityScheme()
                                .name(SECURITY_SCHEME_NAME)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("Token")
                ));
    }


}
