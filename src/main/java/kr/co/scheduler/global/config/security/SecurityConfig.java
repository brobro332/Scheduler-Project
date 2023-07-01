package kr.co.scheduler.global.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final PrincipalDetailsService principalDetailsService;
    private final AuthenticationFailureHandler customFailureHandler;
    private static final String[] AUTH_WHITELIST = {
            "/", "/js/**", "/css/**", "/image/**", "/api/**"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests(authorize -> authorize
                        .shouldFilterAllDispatcherTypes(false)
                        .requestMatchers(AUTH_WHITELIST).permitAll()
                        .requestMatchers("/signIn", "/signUp").permitAll()
                        .requestMatchers("/signInForm", "/signUpForm").permitAll()
                        .requestMatchers("/user/**").authenticated()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated())
                            .formLogin(login -> login
                                .loginPage("/signInForm")
                                .loginProcessingUrl("/signIn")
                                .usernameParameter("email")
                                .passwordParameter("password")
                                .failureHandler(customFailureHandler)
                                .defaultSuccessUrl("/", false)
                                .permitAll())
                            .logout()
                                .logoutSuccessUrl("/")
                                .invalidateHttpSession(true).deleteCookies("JSESSIONID");

        return http.build();
    }

    private void filterChain(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(principalDetailsService).passwordEncoder(Encoder());
    }

    @Bean
    BCryptPasswordEncoder Encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {

        return authenticationConfiguration.getAuthenticationManager();
    }
}
