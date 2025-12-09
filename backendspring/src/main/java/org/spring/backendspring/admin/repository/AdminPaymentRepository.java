package org.spring.backendspring.admin.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.spring.backendspring.payment.dto.PaymentItemDto;
import org.spring.backendspring.payment.entity.PaymentEntity;
import org.spring.backendspring.payment.entity.PaymentItemEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AdminPaymentRepository extends JpaRepository<PaymentEntity, Long> {
    // search 용도
    Page<PaymentEntity> findByPaymentIdContainingIgnoreCase(
            String paymentIdKeyword,
            Pageable pageable);

    @Query("SELECT COUNT(p) FROM PaymentEntity p")
    long countAll();

    @Query("SELECT COUNT(p) FROM PaymentEntity p WHERE DATE(p.createTime) = CURRENT_DATE")
    long countToday();

    @Query("SELECT COALESCE(SUM(p.productPrice), 0) FROM PaymentEntity p")
    Long totalSales();

    @Query("SELECT COALESCE(SUM(p.productPrice), 0) FROM PaymentEntity p WHERE DATE(p.createTime) = CURRENT_DATE")
    Long todaySales();
    // paymentId로 결제 상품들 조회
    // List<PaymentItemEntity> findPaymentItemsByPaymentId(Long paymentId);

    @Query("""
        SELECT COALESCE(SUM(COALESCE(p.productPrice, 0)), 0)
        FROM PaymentEntity p
        WHERE p.createTime >= :start AND p.createTime < :end
    """)
    Long sumSalesByDate(LocalDateTime start, LocalDateTime end);
    
}