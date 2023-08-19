package com.example.springsecuritypractice.config.auth;

import com.example.springsecuritypractice.entity.User;
import com.example.springsecuritypractice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// 시큐리티 설정에서 loginProcessingUrl("/login")으로 설정했기에
// /login요청이 오면 자동으로 UserDetailsService 타입으로 Ioc되어 있는 loadUserByUsername 함수가 실행
@Service
@RequiredArgsConstructor
@Slf4j
public class CustomDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    // 이 떄 파라미터로 들어가는 username은 htmlform의 username과 동일해야한다.
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("username : {}",username);
        User userEntity = userRepository.findByUsername(username);
        if(userEntity != null){
            return new CustomDetails(userEntity);
        }
        return null;
    }
}

// 로그인 버튼 요청이 오면 Ioc 컨테이너에서 UserDetailsService의 loadUserByUsername을 호출한다.
