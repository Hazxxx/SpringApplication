package bada_project.SpringApplication.config;

import bada_project.SpringApplication.admin.SystemFlagsDAO;
import bada_project.SpringApplication.auth.UnifiedUserDetailsService;
import bada_project.SpringApplication.security.MaintenanceFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final UnifiedUserDetailsService unifiedUserDetailsService;
    private final SystemFlagsDAO systemFlagsDAO;

    public SecurityConfiguration(
            UnifiedUserDetailsService unifiedUserDetailsService,
            SystemFlagsDAO systemFlagsDAO
    ) {
        this.unifiedUserDetailsService = unifiedUserDetailsService;
        this.systemFlagsDAO = systemFlagsDAO;
    }

    /* ===== PASSWORD ENCODER ===== */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /* ===== MAINTENANCE FILTER ===== */
    @Bean
    public MaintenanceFilter maintenanceFilter() {
        return new MaintenanceFilter(systemFlagsDAO);
    }

    /* ===== SECURITY FILTER CHAIN ===== */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                /* ===== AUTH RULES ===== */
                .authorizeHttpRequests(auth -> auth
                        // PUBLIC
                        .requestMatchers(
                                "/", "/index", "/login", "/register",
                                "/maintenance",
                                "/css/**", "/js/**", "/assets/**", "/webjars/**"
                        ).permitAll()

                        // ADMIN
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        // USER
                        .requestMatchers("/user/**").hasRole("USER")

                        // MAIN REDIRECT
                        .requestMatchers("/main").authenticated()

                        // EVERYTHING ELSE
                        .anyRequest().authenticated()
                )

                /* ===== LOGIN ===== */
                .formLogin(form -> form
                        .loginPage("/login")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/main", true)
                        .failureUrl("/login?error=true")
                        .permitAll()
                )

                /* ===== LOGOUT ===== */
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/index")
                        .permitAll()
                )

                /* ===== MAINTENANCE FILTER - AFTER AUTHENTICATION ===== */
                .addFilterAfter(  // ← KLUCZOWA ZMIANA!
                        maintenanceFilter(),
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    /* ===== AUTH MANAGER ===== */
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {

        AuthenticationManagerBuilder builder =
                http.getSharedObject(AuthenticationManagerBuilder.class);

        builder
                .userDetailsService(unifiedUserDetailsService)
                .passwordEncoder(passwordEncoder());

        return builder.build();
    }
}