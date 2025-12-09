package org.spring.backendspring.config.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.spring.backendspring.member.entity.MemberEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Getter
@Setter
public class MyUserDetails implements UserDetails, OAuth2User {

    private MemberEntity memberEntity;
    private Map<String, Object> getAttributes;

    public MyUserDetails(MemberEntity memberEntity) {
        this.memberEntity = memberEntity;
    }

    public MyUserDetails(MemberEntity memberEntity,
                         Map<String, Object> getAttributes) {
        this.memberEntity = memberEntity;
        this.getAttributes = getAttributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return "ROLE_" + memberEntity.getRole().toString();
            }
        });
        return authorities;
    }

    // 소셜 로그인 정보
    @Override
    public Map<String, Object> getAttributes() {
        return getAttributes;
    }

    // 비밀번호
    @Override
    public String getPassword() {
        return memberEntity.getUserPassword();
    }

    // 이메일
    @Override
    public String getUsername() {
        return memberEntity.getUserEmail();
    }

    public Long getMemberId() {
        return memberEntity.getId();
    }

    public String getNickName() {
        return memberEntity.getNickName();
    }

    // 소셜 이름
    @Override
    public String getName() {
        return memberEntity.getUserName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
