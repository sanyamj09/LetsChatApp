package com.ChatApp.ChatAppSecurity.configuration;

import com.ChatApp.ChatAppSecurity.Service.CustomUserDetailsService;
import com.ChatApp.ChatAppSecurity.jwt.AuthEntryPointJwt;
import com.ChatApp.ChatAppSecurity.jwt.AuthTokenFilter;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    DataSource dataSource;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authTokenFilter(){
        return new AuthTokenFilter();
    }

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((requests) ->
                requests.requestMatchers("/signin").permitAll()
                        .requestMatchers("/signup").permitAll()
                        .requestMatchers("/resetAccessToken").permitAll()
                        .requestMatchers("/actuator/health").permitAll()
                        .requestMatchers("actuator/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
        );
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler));
        http.logout(logout -> logout
                .logoutUrl("/logout")
                .disable() // Disable default LogoutFilter
        );

        http.csrf(csrf -> csrf
                .ignoringRequestMatchers("/signin")
                .ignoringRequestMatchers("/signup")
                .ignoringRequestMatchers("/resetAccessToken")
                .ignoringRequestMatchers("/logout","/userprofile"));
        http.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));
        http.addFilterBefore(authTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Autowired
    private CustomUserDetailsService customUserDetailsService;



    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(provider);
    }

    // Removed the AuthenticationManager bean to prevent proxy recursion
    // @Bean
    // public AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception {
    //     return builder.getAuthenticationManager();
    // }
}