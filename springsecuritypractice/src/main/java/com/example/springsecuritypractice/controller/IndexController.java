package com.example.springsecuritypractice.controller;

import com.example.springsecuritypractice.config.auth.CustomDetails;
import com.example.springsecuritypractice.entity.User;
import com.example.springsecuritypractice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
@Slf4j
public class IndexController {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @ResponseBody
    @GetMapping("/test/login")
    public String loginTest(Authentication authentication, @AuthenticationPrincipal UserDetails userDetails){
        log.info("/test/login ======================");
        CustomDetails customDetails = (CustomDetails) authentication.getPrincipal();
        log.info("authentication : {}", customDetails.getUser());
        log.info("userDetails : {}",userDetails.getUsername());
        return "세션 정보 확인하기";

    }
    @ResponseBody
    @GetMapping("/test/Oauth2login")
    public String oauth2loginTest(Authentication authentication){
        log.info("/test/login ======================");
        OAuth2User customDetails = (OAuth2User) authentication.getPrincipal();
        log.info("authentication : {}", customDetails.getAuthorities());
        return "세션 정보 확인하기";

    }

    @GetMapping({"", "/"})
    public String index(){
        return "index";
    }
    @ResponseBody
    @GetMapping("/user")
    public String user(@AuthenticationPrincipal CustomDetails customDetails){
        System.out.println("customDetails = " + customDetails.getUser());;
        return "user";
    }
    @ResponseBody
    @GetMapping("/manageer")
    public String manager(){
        return "manager";
    }
    @ResponseBody
    @GetMapping("/admin")
    public String admin(){
        return "admin";
    }
    @GetMapping("/loginForm")
    public String loginForm(){
        return "loginForm";
    }
    @GetMapping("/joinForm")
    public String joinForm(){
        return "joinForm";
    }
    @ResponseBody
    @PostMapping("/join")
    public  String join(@ModelAttribute User user){
        System.out.println(user);
        log.info("회원가입 페이지 도달!");
        user.setRole("ROLE_USER");
        user.setCreateDate(LocalDateTime.now());
        String rawPassword = user.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        user.setPassword(encPassword);
        userRepository.save(user); // 비밀번호가 :1234로 저장되게되는데 이경우에는 시큐리티로 로그인을 할 수 없다 (암호화가 안되있기 떄문이다)
        return "join";
    }
    @ResponseBody
    @GetMapping("/joinProc")
    public String joinProc(){
        return "joinProc";
    }
    @Secured("ROLE_ADMIN")
    @GetMapping("/info")
    public @ResponseBody String info(){
        return "개인정보";
    }

    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/data")
    public @ResponseBody String data(){
        return "개인정보";
    }

}
