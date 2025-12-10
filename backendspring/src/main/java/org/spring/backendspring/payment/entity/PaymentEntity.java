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



    @OneToMany(mappedBy = "payment", cascade = { CascadeType.REMOVE, CascadeType.PERSIST }, fetch = FetchType.LAZY)
    @Builder.Default 
    private List<PaymentItemEntity> paymentItemEntities = new ArrayList<>();


    private Long productPrice; // 총금액
    private String tid;
    private String pgToken;

    @Column(columnDefinition = "TEXT")
    private String paymentReadyJson;

    @Builder.Default
    private Integer isSucceeded = 0;



    public void addPaymentItem(PaymentItemEntity item) {

        this.paymentItemEntities.add(item); 
        item.setPayment(this); // 👈 PaymentItemEntity의 payment_id 외래 키를 설정
    }
}