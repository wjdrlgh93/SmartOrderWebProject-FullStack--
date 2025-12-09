package org.spring.backendspring.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class AuthResponse {

    private String accessToken;
    private Long id;
    private String userEmail;
    private String nickName;
    private String role;
}
