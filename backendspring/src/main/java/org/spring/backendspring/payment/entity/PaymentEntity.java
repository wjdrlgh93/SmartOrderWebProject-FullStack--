package org.spring.backendspring.payment.entity;

import jakarta.persistence.*;
import lombok.*;
import org.spring.backendspring.common.BasicTime;
import org.spring.backendspring.payment.PaymentStatus;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "payment_tb")
public class PaymentEntity extends BasicTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    private Long memberId;
    private String paymentAddr;
    private String paymentMethod;
    private String paymentPost;
    private String paymentResult;
    private String paymentType;

    private String paymentReceiver;
    private String paymentPhone;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    @Builder.Default
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    // ⭐️ [수정: @Builder.Default 추가] 
    // Builder 패턴 사용 시에도 new ArrayList<>()가 호출되도록 보장합니다.
    @OneToMany(mappedBy = "payment", cascade = { CascadeType.REMOVE, CascadeType.PERSIST }, fetch = FetchType.LAZY)
    @Builder.Default 
    private List<PaymentItemEntity> paymentItemEntities = new ArrayList<>();

    // ---------------- KakaoPay 관련 ----------------
    private Long productPrice; // 총금액
    private String tid;
    private String pgToken;

    @Column(columnDefinition = "TEXT")
    private String paymentReadyJson;

    @Builder.Default
    private Integer isSucceeded = 0;

    // ---------------- PaymentItemEntity 연관관계 편의 메서드 ----------------
    // ServiceImpl에서 이 메서드를 호출하여 PaymentItemEntity에 PaymentEntity 참조를 설정합니다.
    public void addPaymentItem(PaymentItemEntity item) {
        // 이 라인에서 NullPointerException이 발생하지 않도록, 위에서 리스트를 초기화했습니다.
        this.paymentItemEntities.add(item); 
        item.setPayment(this); // 👈 PaymentItemEntity의 payment_id 외래 키를 설정
    }
}