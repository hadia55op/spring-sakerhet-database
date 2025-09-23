package com.example.spring_sakerhet_database.config;
import com.example.spring_sakerhet_database.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import com.example.spring_sakerhet_database.utility.JwtAuthenticationFilter;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.http.HttpMethod;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  // Disable for REST API (stateless)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .headers(headers -> headers// säkra HTTP-headers (HSTS, X-Frame-Options, Content Security Policy)"
                        .httpStrictTransportSecurity(hsts -> hsts
                                .includeSubDomains(true)
                                .maxAgeInSeconds(15552000)  // 6 months
                        )
                        .frameOptions(frame -> frame
                                .deny()  // Prevent clickjacking
                        )
                        .contentSecurityPolicy(csp -> csp
                                .policyDirectives("default-src 'self'; script-src 'self'; style-src 'self'; object-src 'none'")
                        )

                )
                .authorizeHttpRequests(auth -> auth

                        //  Public endpoints
                        .requestMatchers("/api/auth/**").permitAll()
                        //.requestMatchers("/public/**").permitAll()
                        .requestMatchers("/books", "/books/search").permitAll()
                        .requestMatchers("/authors", "/authors/name/**").permitAll()
                        .requestMatchers("/api/auth/logout").permitAll()
                        //.requestMatchers("/api/auth/refresh").permitAll()
                        //.requestMatchers("/api/auth/login", "/api/auth/refresh").permitAll()
                        //.requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                        //.requestMatchers(HttpMethod.POST, "/api/auth/refresh").permitAll()
                        //.requestMatchers(HttpMethod.POST, "/api/auth/logout").permitAll()



                        //  Admin-only endpoints
                        .requestMatchers(HttpMethod.POST, "/books").hasRole("ADMIN")
                        //.requestMatchers("/users/**").hasAnyRole( "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/authors").hasRole("ADMIN")
                       // .requestMatchers("/api/admin/**").hasRole("ADMIN")
                       // .requestMatchers("/admin/**").hasRole("ADMIN")


                        // 🔐 User and Admin can access
                       // .requestMatchers("/api/user/**").hasAnyRole("USER", "ADMIN")
                       .requestMatchers("/users/**").hasAnyRole( "ADMIN", "USER")
                       // .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")

                        // 🔐 Loan and User-specific endpoints
                        .requestMatchers("/loans/**").hasAnyRole("USER", "ADMIN")
                        //.requestMatchers("/users/**").hasAnyRole("USER", "ADMIN")

                        // 🔒 Everything else must be authenticated
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }//VG krav för säkra cross-origin requests"
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:3000"));
        config.setAllowedMethods(List.of("GET", "POST"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;

}}