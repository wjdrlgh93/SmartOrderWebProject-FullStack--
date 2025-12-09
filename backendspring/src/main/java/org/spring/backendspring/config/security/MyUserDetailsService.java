package org.spring.backendspring.config.security;

import lombok.RequiredArgsConstructor;
import org.spring.backendspring.member.entity.MemberEntity;
import org.spring.backendspring.member.repository.MemberRepository;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MemberEntity memberEntity = memberRepository.findByUserEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("없는 이메일입니다."));

        if (memberEntity.isDeleted()) {
            throw new DisabledException("이메일 또는 비밀번호가 맞지 않습니다.");
        }

        return new MyUserDetails(memberEntity);
    }
}
