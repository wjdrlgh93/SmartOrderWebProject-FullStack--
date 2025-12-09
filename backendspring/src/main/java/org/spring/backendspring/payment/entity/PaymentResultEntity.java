package org.spring.backendspring.payment.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Setter
@Getter
@Table(name = "payment_result_tb")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentResultEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentResultId;

    private String paymentId;

    private String memberId;
    
    private String productName;
   
    private String productPrice;   

}
