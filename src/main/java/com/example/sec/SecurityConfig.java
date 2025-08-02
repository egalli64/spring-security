/*
 * Spring Boot Security tutorial 
 * 
 * https://github.com/egalli64/spring-security
 */
package com.example.sec;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final SecUserDetailsService svc;

    public SecurityConfig(SecUserDetailsService secUserSvc) {
        this.svc = secUserSvc;
    }

    /**
     * Configure Form-Based Authentication
     */
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // Disable HTTP Basic - using form login instead
        http.httpBasic(httpBasic -> httpBasic.disable())
                // Enable form login - custom login page
                .formLogin(form -> form.loginPage("/login").loginProcessingUrl("/do_login")
                        // redirections after success/failure
                        .defaultSuccessUrl("/", true).failureUrl("/login?error=true") //
                        .permitAll())
                // logout configuration
                .logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/").permitAll())
                // use the app-specific UserDetailsService
                .userDetailsService(svc)
                // Two roles + public and H2 console
                .authorizeHttpRequests(auth -> auth.requestMatchers("/", "/public", "/h2-console/**").permitAll() //
                        .requestMatchers("/admin").hasRole("ADMIN") //
                        .requestMatchers("/private").hasRole("USER") //
                        // Allow access to user management endpoints for admins
                        .requestMatchers("/users/**").hasRole("ADMIN") //
                        .anyRequest().denyAll())
                // Disable Cross-Site Request Forgery for H2
                .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"))
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));

        return http.build();
    }
}
