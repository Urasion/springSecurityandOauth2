package com.example.springsecuritypractice.config.auth;

import com.example.springsecuritypractice.entity.User;
import com.example.springsecuritypractice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service

public class CustomOauth2UserService extends DefaultOAuth2UserService {

    public CustomOauth2UserService(@Lazy BCryptPasswordEncoder bCryptPasswordEncoder,@Lazy UserRepository userRepository) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
    }

    @Lazy
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;
    // 구글로 부터 받은 userRequest 데이터에 대한 후처리 되는 함수
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("userRequest = " + userRequest);

        // 구글로그인버튼 -> 구글로그인창 -> 로그인 완료시 code를 리턴받음(Oauth2-Client 라이브러리)
        // -> 이후 code로 AccessToken을 요청하는데 여기까지가 userRequest 정보
        // userRequest정보를 통해 회원프로필을 받아야하는데 이 때 loadUser함수가 필요하다.
        // 이후 loadUser함수를 통해 회원프로필을 받아볼 수 있다.
        OAuth2User oAuth2User = super.loadUser(userRequest);

        System.out.println("oAuth2User = " + oAuth2User.getAttributes());

        String provider = userRequest.getClientRegistration().getClientId(); // google
        String providerId = oAuth2User.getAttribute("sub");
        String email = oAuth2User.getAttribute("email");
        String username = provider + "_" + providerId; // google_sub(숫자)
        String password = bCryptPasswordEncoder.encode("신토불이");
        String role = "ROLE_USER";
        User userEntity = userRepository.findByUsername(username);
        if(userEntity == null){
            userEntity = User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .role(role)
                    .provider(provider)
                    .providerId(providerId)
                    .createDate(LocalDateTime.now())
                    .build();
            userRepository.save(userEntity);
        }
        return new CustomDetails(userEntity, oAuth2User.getAttributes());
    }
}
