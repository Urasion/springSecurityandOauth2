package com.example.springsecuritypractice.config.auth;

import com.example.springsecuritypractice.entity.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

// 시큐리티가 /login 주소 요청이 오면 낚아채서 로그인을 진행시키고 시큐리티 session을 만들어줍니다.
// 시큐리티는 자신만의 session 공간이 있다. Security ContextHolder라는 키값을 세션 정보에 저장을 한다.
// 시큐리티 Session 공간에 저장될 수 있는 정보는 Authentication 타입 객체이고
// Authentication 객체 안에는 User 정보가 있어야 된다.
// User 오브젝트 타입은 UserDetails 타입 객체이다.
// Security Session => Authentication => UserDetails
@Data
public class CustomDetails implements UserDetails, OAuth2User {
    private User user;

    private Map<String, Object> attributes;

    // 일반 로그인
    public CustomDetails(User user) {
        this.user = user;
    }


    // OAuth 로그인
    public CustomDetails(User user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }

    // 해당 유저의 권한을 return
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRole();
            }
        });
        return collect;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }
    // 만료여부
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    // 잠금여부
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    // 계정 휴면 여부
    // 유저 로그인 시간 정보 저장을 따로 하면 오늘 - 마지막 접속일 계산해서 유저를 휴면상태로 만들 수 있음.
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 계정 활성화 여부
    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return null;
    }
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }
}
