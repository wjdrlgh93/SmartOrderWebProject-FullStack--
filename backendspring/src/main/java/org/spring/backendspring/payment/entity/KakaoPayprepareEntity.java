package org.spring.backendspring.payment.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.spring.backendspring.payment.dto.KakaoPayPrepareDto;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "kakao_pay_prepare_tb")
public class KakaoPayprepareEntity {

    @Id
    private String tid;

    private String tmsResult;
    private String nextRedirectAppUrl;
    private String nextRedirectMobileUrl;
    private String nextRedirectPcUrl;
    private String androidAppScheme;
    private String iosAppScheme;
    private String createdAt;

    public static KakaoPayprepareEntity toEntity(KakaoPayPrepareDto dto) {
        return KakaoPayprepareEntity.builder()
                .tid(dto.getTid())
                .tmsResult(dto.getTms_result())
                .nextRedirectAppUrl(dto.getNext_redirect_app_url())
                .nextRedirectMobileUrl(dto.getNext_redirect_mobile_url())
                .nextRedirectPcUrl(dto.getNext_redirect_pc_url())
                .androidAppScheme(dto.getAndroid_app_scheme())
                .iosAppScheme(dto.getIos_app_scheme())
                .createdAt(dto.getCreated_at())
                .build();
    }
}
