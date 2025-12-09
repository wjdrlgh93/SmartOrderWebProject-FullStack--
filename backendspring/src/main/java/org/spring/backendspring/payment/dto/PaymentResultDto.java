package org.spring.backendspring.payment.dto;

import jakarta.persistence.*;
import lombok.*;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentResultDto {

    private Long paymentResultId;

    private String paymentId;

    private String memberId;
    
    private String productName;
   
    private String productPrice;   
}
