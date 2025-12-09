package org.spring.backendspring.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.spring.backendspring.payment.PaymentStatus;
import org.spring.backendspring.payment.entity.PaymentEntity;
import org.spring.backendspring.payment.entity.PaymentItemEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDto {
    private Long paymentId;
    private Long memberId;
    
    // ⭐️ [추가] 수령인 정보 필드
    private String paymentReceiver;
    private String paymentPhone; 
    
    private String paymentAddr;
    private String paymentMethod;
    private String paymentPost;
    private String paymentResult;
    private String paymentType;
    private PaymentStatus paymentStatus;
    private Long productPrice;
    private String tid;
    private String pgToken;
    private String paymentReadyJson;
    private Integer isSucceeded;
    private LocalDateTime createTime;

    // 결제 상품 목록 (Request/Response 시 사용)
    private List<PaymentItemDto> paymentItems; 

    public PaymentEntity toEntity() {
        return PaymentEntity.builder()
                .paymentId(this.paymentId)
                .memberId(this.memberId)
                
                // ⭐️ [매핑] DTO -> Entity 매핑
                .paymentReceiver(this.paymentReceiver)
                .paymentPhone(this.paymentPhone)
                
                .paymentAddr(this.paymentAddr)
                .paymentMethod(this.paymentMethod)
                .paymentPost(this.paymentPost)
                .paymentResult(this.paymentResult)
                .paymentType(this.paymentType)
                .paymentStatus(this.paymentStatus)
                .productPrice(this.productPrice)
                .tid(this.tid)
                .pgToken(this.pgToken)
                .paymentReadyJson(this.paymentReadyJson)
                .isSucceeded(this.isSucceeded)
                .build();
    }

    public static PaymentDto fromEntity(PaymentEntity entity) {
        // PaymentItemEntity를 PaymentItemDto로 변환
        List<PaymentItemDto> itemDtos = entity.getPaymentItemEntities().stream()
                .map(PaymentItemDto::fromEntity)
                .collect(Collectors.toList());

        return PaymentDto.builder()
                .paymentId(entity.getPaymentId())
                .memberId(entity.getMemberId())
                
                // ⭐️ [매핑] Entity -> DTO 매핑
                .paymentReceiver(entity.getPaymentReceiver())
                .paymentPhone(entity.getPaymentPhone())
                
                .paymentAddr(entity.getPaymentAddr())
                .paymentMethod(entity.getPaymentMethod())
                .paymentPost(entity.getPaymentPost())
                .paymentResult(entity.getPaymentResult())
                .paymentType(entity.getPaymentType())
                .paymentStatus(entity.getPaymentStatus())
                .productPrice(entity.getProductPrice())
                .tid(entity.getTid())
                .pgToken(entity.getPgToken())
                .paymentReadyJson(entity.getPaymentReadyJson())
                .isSucceeded(entity.getIsSucceeded())
                .createTime(entity.getCreateTime())
                .paymentItems(itemDtos)
                .build();
    }
}