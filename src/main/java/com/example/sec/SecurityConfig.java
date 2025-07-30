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
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    /**
     * Configure HTTP Basic Authentication
     */
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.httpBasic(httpBasic -> httpBasic.realmName("Spring Security Tutorial"))
                // only Basic Auth
                .formLogin(form -> form.disable())
                // Two roles + public
                .authorizeHttpRequests(auth -> auth.requestMatchers("/", "/public").permitAll() //
                        .requestMatchers("/admin").hasRole("ADMIN") //
                        .requestMatchers("/private").hasRole("USER") //
                        // the endpoints not specified above are all denied
                        .anyRequest().denyAll());
        // Disable Cross-Site Request Forgery for curl testing
        // .csrf(csrf -> csrf.disable())
        // For "classic" webapp, the use of non-GET form requires passing the CSRF token
        // <input type="hidden" th:name="${_csrf.parameterName}"
        // th:value="${_csrf.token}"/>

        // SPA with REST API could need
        // csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()

        return http.build();
    }

    /**
     * Configure each user with password and associated roles
     */
    @Bean
    UserDetailsService userDetailsService() {
        PasswordEncoder encoder = passwordEncoder();

        UserDetails user = User.builder().username("user").password(encoder.encode("password")).roles("USER").build();
        UserDetails admin = User.builder().username("admin").password(encoder.encode("admin")).roles("ADMIN").build();
        UserDetails superuser = User.builder().username("superuser").password(encoder.encode("super"))
                .roles("USER", "ADMIN").build();

        return new InMemoryUserDetailsManager(user, admin, superuser);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
