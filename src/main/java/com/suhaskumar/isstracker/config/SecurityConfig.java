package com.suhaskumar.isstracker.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final String userName;
    private final String userPassword;
    private final String adminName;
    private final String adminPassword;

    // Using constructor injection
    public SecurityConfig(
            @Value("${security.user.name}") String userName,
            @Value("${security.user.password}") String userPassword,
            @Value("${security.admin.name}") String adminName,
            @Value("${security.admin.password}") String adminPassword) {
        this.userName = userName;
        this.userPassword = userPassword;
        this.adminName = adminName;
        this.adminPassword = adminPassword;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .httpBasic(withDefaults());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Using BCrypt for strong, modern password hashing
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        // Building users with the secure password encoder
        UserDetails user = User.builder()
                .username(userName)
                .password(passwordEncoder.encode(userPassword))
                .roles("USER")
                .build();

        UserDetails admin = User.builder()
                .username(adminName)
                .password(passwordEncoder.encode(adminPassword))
                .roles("ADMIN", "USER")
                .build();

        return new InMemoryUserDetailsManager(user, admin);
    }
}