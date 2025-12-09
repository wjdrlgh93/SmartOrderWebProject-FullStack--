package org.spring.backendspring.payment.repository;

import org.spring.backendspring.payment.entity.PaymentItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentItemRepository extends JpaRepository<PaymentItemEntity, Long> {
    
    List<PaymentItemEntity> findByPayment_PaymentId(Long paymentId);
}
