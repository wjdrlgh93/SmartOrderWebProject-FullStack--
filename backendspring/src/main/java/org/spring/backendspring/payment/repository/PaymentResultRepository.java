package org.spring.backendspring.payment.repository;

import org.spring.backendspring.payment.entity.PaymentResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentResultRepository  extends JpaRepository<PaymentResultEntity,Long>{
  
}