package org.spring.backendspring.config.security;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.spring.backendspring.common.exception.CustomException;
import org.spring.backendspring.common.exception.ErrorCode;
import org.spring.backendspring.member.MemberMapper;
import org.spring.backendspring.member.entity.MemberEntity;
import org.spring.backendspring.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class MyDefaultOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        ClientRegistration clientRegistration = userRequest.getClientRegistration();
        String registrationId = clientRegistration.getRegistrationId();

        Map<String, Object> attribute = oAuth2User.getAttributes();

        return oauth2UserSuccess(oAuth2User, registrationId);
    }

    private OAuth2User oauth2UserSuccess(OAuth2User oAuth2User, String registrationId) {
        String userEmail = "";
        String userName = "";
        String userPw = "";

        if (registrationId.equals("google")) {
            userEmail = oAuth2User.getAttribute("email");
            userName = oAuth2User.getAttribute("name");
            System.out.println(userEmail + " <<< userEmail");
            System.out.println(userName + " <<< userName");
        } else if (registrationId.equals("naver")) {
            Map<String, Object> response = oAuth2User.getAttribute("response");
            userEmail = (String) response.get("email");
            userName = (String) response.get("name");
        } else if (registrationId.equals("kakao")) {
            Map<String, Object> kakaoAccount = oAuth2User.getAttribute("kakao_account");
            Map<String, Object> properties = oAuth2User.getAttribute("properties");
            userEmail = (String) kakaoAccount.get("email");
            userName = (String) properties.get("nickname");
        }

        // 있으면 로그인 처리
        Optional<MemberEntity> memberEntity = memberRepository.findByUserEmail(userEmail);

        if (memberEntity.isPresent()) {
            if (memberEntity.get().getSocialLogin() == 0) {
                throw new CustomException(ErrorCode.ALREADY_REGISTERED_LOCAL_USER);
            }
            return new MyUserDetails(memberEntity.get());
        }

        // 없으면 가입
        userPw = passwordEncoder.encode(UUID.randomUUID().toString());
        // 소셜 로그인은 임시값을 넣어줍니다.
        MemberEntity socialEntity = MemberMapper.toSocialEntity(userEmail, userName, userPw);
        memberRepository.save(socialEntity);
        return new MyUserDetails(socialEntity, oAuth2User.getAttributes());
    }
}
