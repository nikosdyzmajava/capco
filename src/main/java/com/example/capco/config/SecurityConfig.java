package com.example.capco.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(((authz) -> authz
                        .antMatchers(HttpMethod.POST, "/app").hasRole("ADMIN")
                        .antMatchers(HttpMethod.GET, "/app").hasRole("USER")
                        .antMatchers("/app/user/{userId}/feature/{featureId}").hasRole("ADMIN")
                        .antMatchers("/console/**").permitAll()
                ))
                .httpBasic(withDefaults()).csrf().disable()
                .headers().frameOptions().disable().and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().logout().permitAll();
        return http.build();
    }

}
