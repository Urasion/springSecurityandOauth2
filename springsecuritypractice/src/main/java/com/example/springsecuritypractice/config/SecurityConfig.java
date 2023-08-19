package com.example.springsecuritypractice.config;

import com.example.springsecuritypractice.config.auth.CustomOauth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

// 1. 인증코드를 받음 2. 엑세스토큰을 받음 3. 엑세스토큰을 통해서 사용자정보 접근 가능 4. 사용자 정보를 토대로 회원가입 처리가능

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) // secured 어노테이션 활성화
// preAuthorize 어노테이션 활성화
public class SecurityConfig{
    private final CustomOauth2UserService customOauth2UserService;
    @Bean
    public BCryptPasswordEncoder encodePwd(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        http
                .csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable())

                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/user/**").authenticated()
                        .requestMatchers("/manager/**").hasAnyRole("MANAGER", "ADMIN")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().permitAll()
                ).formLogin(form -> form
                        .loginPage("/loginForm")
                        .loginProcessingUrl("/login")
                        .permitAll()
                        .defaultSuccessUrl("/")


                ).oauth2Login(oath2 -> oath2
                        .userInfoEndpoint(info -> info.userService(customOauth2UserService)));
        // 구글 로그인이 완료된 후의 후처리가 필요하다
        // 구글 로그인 완료시 코드를 받는 것이 아닌 엑세스토큰 + 사용자정보를 받음


        return http.build();
    }
}
