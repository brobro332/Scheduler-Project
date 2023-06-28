package kr.co.scheduler.global.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final String[] AUTH_WHITELIST = {
            "/", "/js/**", "/css/**", "/image/**"
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
                                .defaultSuccessUrl("/", false)
                                .permitAll())
                            .logout()
                                .logoutSuccessUrl("/");

        return http.build();
    }

    @Bean
    BCryptPasswordEncoder passwordEncode() {
        return new BCryptPasswordEncoder();
    }

}
