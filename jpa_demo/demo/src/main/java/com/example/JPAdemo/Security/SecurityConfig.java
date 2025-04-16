package com.example.JPAdemo.Security;


import com.example.JPAdemo.SystemUser.SystemUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
//@AllArgsConstructor
@EnableWebSecurity
public class SecurityConfig {
private final SystemUserService systemUserService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public SecurityConfig(SystemUserService systemUserService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.systemUserService = systemUserService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/user/**").permitAll()// Allow access to login endpoints
                        .requestMatchers("/doctor/**").hasRole("DOCTOR") // Restrict access to Doctor role
                        .requestMatchers("/employee/**").hasRole("EMPLOYEE") // Restrict access to Employee role
                        .anyRequest().authenticated()
                )    .httpBasic(Customizer.withDefaults());

        return http.build();
    }
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(bCryptPasswordEncoder);
        provider.setUserDetailsService(systemUserService);
        return provider;
    }
}