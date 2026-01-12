package bada_project.SpringApplication.config;

import bada_project.SpringApplication.auth.UnifiedUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final UnifiedUserDetailsService unifiedUserDetailsService;

    public SecurityConfiguration(UnifiedUserDetailsService unifiedUserDetailsService) {
        this.unifiedUserDetailsService = unifiedUserDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints
                        .requestMatchers("/", "/index", "/login", "/register").permitAll()
                        .requestMatchers("/css/**", "/js/**", "/webjars/**", "/assets/**").permitAll()

                        // Admin endpoints - only for employees with CZY_ADMIN = 1
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        // User endpoints - both clients and employees
                        .requestMatchers("/user/**").hasRole("USER")
                        .requestMatchers("/user/vehicles/**").hasRole("USER")

                        // Main page for authenticated users
                        .requestMatchers("/main").authenticated()

                        // Everything else requires authentication
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .usernameParameter("username")  // matches your form field
                        .passwordParameter("password")  // matches your form field
                        .defaultSuccessUrl("/main", true)
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/index")
                        .permitAll()
                );
                /*.exceptionHandling(ex -> ex
                        .accessDeniedPage("/access-denied")
                );*/

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);

        authManagerBuilder
                .userDetailsService(unifiedUserDetailsService)
                .passwordEncoder(passwordEncoder());

        return authManagerBuilder.build();
    }
}