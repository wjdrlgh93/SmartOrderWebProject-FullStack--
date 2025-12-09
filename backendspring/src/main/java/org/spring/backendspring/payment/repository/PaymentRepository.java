package org.spring.backendspring.payment.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.spring.backendspring.payment.entity.PaymentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param; // ⭐️ import가 되어 있음
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {

    @Query("SELECT p FROM PaymentEntity p JOIN FETCH p.paymentItemEntities WHERE p.paymentId = :paymentId")
    Optional<PaymentEntity> findByIdWithItems(@Param("paymentId") Long paymentId);

    // pgToken 업데이트
    @Modifying
    @Transactional
    @Query("UPDATE PaymentEntity p SET p.pgToken = :pgToken WHERE p.paymentId = :paymentId")
    void updatePgToken(@Param("paymentId") Long paymentId, @Param("pgToken") String pgToken); // ⭐️ @Param 추가

    // 결제 성공 여부 업데이트 (isSucceeded 같은 컬럼)
    @Modifying
    @Transactional
    @Query("UPDATE PaymentEntity p SET p.isSucceeded = :status WHERE p.paymentId = :paymentId")
    void updateIsSucced(@Param("paymentId") Long paymentId, @Param("status") int status); // ⭐️ @Param 추가

    // ⭐️ [추가] Payment 목록 조회 시 PaymentItemEntity를 EAGER 로딩 (N+1 해결)
    @Query(value = "SELECT p FROM PaymentEntity p JOIN FETCH p.paymentItemEntities", countQuery = "SELECT COUNT(p) FROM PaymentEntity p")
    Page<PaymentEntity> findAllWithItems(Pageable pageable);

    // ⭐️ [추가] 검색어 포함 시 PaymentItemEntity를 EAGER 로딩 (N+1 해결)
    @Query(value = "SELECT p FROM PaymentEntity p JOIN FETCH p.paymentItemEntities WHERE LOWER(p.paymentType) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(p.paymentPost) LIKE LOWER(CONCAT('%', :keyword, '%'))", countQuery = "SELECT COUNT(p) FROM PaymentEntity p WHERE LOWER(p.paymentType) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(p.paymentPost) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<PaymentEntity> findByKeywordWithItems(@Param("keyword") String keyword, Pageable pageable);

    Page<PaymentEntity> findByMemberId(Pageable pageable, Long memberId);

    @Query(value = "SELECT p FROM PaymentEntity p JOIN FETCH p.paymentItemEntities item " +
            "WHERE p.memberId = :memberId AND item.title LIKE %:keyword%", countQuery = "SELECT COUNT(DISTINCT p) FROM PaymentEntity p JOIN p.paymentItemEntities item "
                    +
                    "WHERE p.memberId = :memberId AND item.title LIKE %:keyword%")
    Page<PaymentEntity> findByMemberIdAndTitleContaining(Pageable pageable, @Param("keyword") String keyword,
            @Param("memberId") Long memberId); // ⭐️ 이 메서드도 @Param 추가 권장

}