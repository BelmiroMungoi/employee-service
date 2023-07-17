package com.bbm.employeeservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import static com.bbm.employeeservice.model.Permission.*;
import static com.bbm.employeeservice.model.Role.ADMIN;
import static com.bbm.employeeservice.model.Role.USER;
import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final LogoutHandler logoutHandler;
    private final JwtAuthenticationFilter authenticationFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorizationManager) -> authorizationManager
                        .requestMatchers("/api/v1/auth/**")
                        .permitAll()
                                .requestMatchers("/api/v1/employee/**").hasAnyRole(ADMIN.name(), USER.name())
                                .requestMatchers(POST, "/api/v1/employee/**").hasAuthority(ADMIN_CREATE.name())
                                .requestMatchers(GET, "/api/v1/employee/**").hasAnyAuthority(ADMIN_READ.name(), USER_READ.name())
                                .requestMatchers(PUT, "/api/v1/employee/**").hasAuthority(ADMIN_UPDATE.name())
                                .requestMatchers(DELETE, "/api/v1/employee/**").hasAuthority(ADMIN_UPDATE.name())
                                .anyRequest()
                                .authenticated()
                        )
                .sessionManagement(sessionManager -> sessionManager
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .logout((logoutConfigurer) -> logoutConfigurer
                        .logoutUrl("/api/v1/auth/logout")
                        .addLogoutHandler(logoutHandler)
                        .logoutSuccessHandler((request, response, authentication) ->
                                SecurityContextHolder.clearContext())
                );

        return http.build();
    }
}
