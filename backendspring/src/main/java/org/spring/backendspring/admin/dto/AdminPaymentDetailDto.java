package org.spring.backendspring.admin.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.spring.backendspring.payment.PaymentStatus;
import org.spring.backendspring.payment.dto.PaymentItemDto;
import org.spring.backendspring.payment.entity.PaymentEntity;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class AdminPaymentDetailDto {
    private Long paymentId;
    private Long memberId;
    private String memberStatus;

    private String paymentReceiver;
    private String paymentPhone;
    private String paymentAddr;
    private String paymentMethod;
    private String paymentPost;
    private String paymentType;
    private PaymentStatus paymentStatus;
    private Long productPrice;

    private List<PaymentItemDto> paymentItems;

    private LocalDateTime createTime;

    public void setMemberStatus(String memberStatus) {
        this.memberStatus = memberStatus;
    }

    public static AdminPaymentDetailDto toDto(PaymentEntity paymentEntity) {
        List<PaymentItemDto> itemDtos = paymentEntity.getPaymentItemEntities().stream()
                .map(PaymentItemDto::fromEntity)
                .collect(Collectors.toList());

        return AdminPaymentDetailDto.builder()
                .paymentId(paymentEntity.getPaymentId())
                .memberId(paymentEntity.getMemberId())
                .paymentReceiver(paymentEntity.getPaymentReceiver())
                .paymentPhone(paymentEntity.getPaymentPhone())
                .paymentAddr(paymentEntity.getPaymentAddr())
                .paymentMethod(paymentEntity.getPaymentMethod())
                .paymentPost(paymentEntity.getPaymentPost())
                .paymentType(paymentEntity.getPaymentType())
                .paymentStatus(paymentEntity.getPaymentStatus())
                .productPrice(paymentEntity.getProductPrice())
                .paymentItems(itemDtos)
                .createTime(paymentEntity.getCreateTime())
                .build();
    }
}
