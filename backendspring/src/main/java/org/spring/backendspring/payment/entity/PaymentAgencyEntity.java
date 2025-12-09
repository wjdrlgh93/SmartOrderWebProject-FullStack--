package org.spring.backendspring.payment.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "payment_agency_tb")
public class PaymentAgencyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long agencyId;

    @Column(name = "payment_agency")
    private String paymentAgency; //kakao 인지 naver인지
}
