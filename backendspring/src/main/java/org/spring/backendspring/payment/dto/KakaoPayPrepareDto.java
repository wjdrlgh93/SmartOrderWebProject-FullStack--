package org.spring.backendspring.payment.dto;


import org.spring.backendspring.payment.entity.KakaoPayprepareEntity;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KakaoPayPrepareDto {

    private String tid;
    private String tms_result;
    private String next_redirect_app_url;
    private String next_redirect_mobile_url;
    private String next_redirect_pc_url;
    private String android_app_scheme;
    private String ios_app_scheme;
    private String created_at;

    public static KakaoPayPrepareDto toDto(KakaoPayprepareEntity kakaoPayPrepareEntity) {

        return KakaoPayPrepareDto.builder()
                .android_app_scheme(kakaoPayPrepareEntity.getAndroidAppScheme())
                .created_at(kakaoPayPrepareEntity.getCreatedAt())
                .ios_app_scheme(kakaoPayPrepareEntity.getIosAppScheme())
                .tid(kakaoPayPrepareEntity.getTid())
                .next_redirect_app_url(kakaoPayPrepareEntity.getNextRedirectAppUrl())
                .next_redirect_mobile_url(kakaoPayPrepareEntity.getNextRedirectMobileUrl())
                .next_redirect_pc_url(kakaoPayPrepareEntity.getNextRedirectPcUrl())
                .tms_result(kakaoPayPrepareEntity.getTmsResult())
                .build();
    }

}