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
                // Two roles + public
                .authorizeHttpRequests(auth -> auth.requestMatchers("/", "/public").permitAll() //
                        .requestMatchers("/admin").hasRole("ADMIN") //
                        .requestMatchers("/private").hasRole("USER") //
                        .anyRequest().denyAll());
        // Disable Cross-Site Request Forgery for curl testing
        // .csrf(csrf -> csrf.disable())

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
