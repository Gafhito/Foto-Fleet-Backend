package com.digitalhouse.fotofleet.config;

import com.digitalhouse.fotofleet.security.JwtAuthenticationEntryPoint;
import com.digitalhouse.fotofleet.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests()
                .requestMatchers( "/doc/swagger-ui/**").permitAll()
                .requestMatchers( "/v3/api-docs/**").permitAll()
                .requestMatchers("/auth/login").permitAll()
                .requestMatchers("/auth/register/user").permitAll()
                .requestMatchers(HttpMethod.POST, "/auth/register/moderator").hasAuthority("Admin")
                .requestMatchers(HttpMethod.POST, "/auth/update").hasAuthority("Admin")
                .requestMatchers(HttpMethod.GET, "/user").hasAnyAuthority("Admin", "Moderator", "User")
                .requestMatchers(HttpMethod.POST, "/user/favorite").hasAnyAuthority("Admin", "Moderator", "User")
                .requestMatchers(HttpMethod.DELETE, "/user/favorite").hasAnyAuthority("Admin", "Moderator", "User")
                .requestMatchers(HttpMethod.GET, "/products").permitAll()
                .requestMatchers(HttpMethod.GET, "/products/{id}").permitAll()
                .requestMatchers(HttpMethod.GET, "/products/search").permitAll()
                .requestMatchers(HttpMethod.POST, "/products").hasAnyAuthority("Admin", "Moderator")
                .requestMatchers(HttpMethod.PUT, "/products/{id}").hasAnyAuthority("Admin", "Moderator")
                .requestMatchers(HttpMethod.DELETE, "/products").hasAnyAuthority("Admin", "Moderator")
                .requestMatchers(HttpMethod.POST, "/products/images").hasAnyAuthority("Admin", "Moderator")
                .requestMatchers(HttpMethod.GET, "/categories").permitAll()
                .requestMatchers(HttpMethod.GET, "/categories/{id}").permitAll()
                .requestMatchers(HttpMethod.POST, "/categories").hasAnyAuthority("Admin", "Moderator")
                .requestMatchers(HttpMethod.PUT, "/categories/{id}").hasAnyAuthority("Admin", "Moderator")
                .requestMatchers(HttpMethod.DELETE, "/categories").hasAnyAuthority("Admin", "Moderator")
                .requestMatchers(HttpMethod.POST, "/categories/image").hasAnyAuthority("Admin", "Moderator")
                .requestMatchers(HttpMethod.GET, "/characteristics").permitAll()
                .requestMatchers(HttpMethod.GET, "/characteristics/{id}").permitAll()
                .requestMatchers(HttpMethod.POST, "/characteristics").hasAnyAuthority("Admin", "Moderator")
                .requestMatchers(HttpMethod.PUT, "/characteristics/{id}").hasAnyAuthority("Admin", "Moderator")
                .requestMatchers(HttpMethod.DELETE, "/characteristics").hasAnyAuthority("Admin", "Moderator")
                .requestMatchers(HttpMethod.POST, "/characteristics/{productId}").hasAnyAuthority("Admin", "Moderator")
                .requestMatchers(HttpMethod.POST, "/rental").hasAuthority("User")
                .requestMatchers(HttpMethod.POST, "/rental/status").hasAnyAuthority("Admin", "Moderator")
                .anyRequest().authenticated()
                .and()
                .httpBasic();

        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
