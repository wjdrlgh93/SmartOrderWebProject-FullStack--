package org.spring.backendspring.payment.service;

import org.spring.backendspring.common.dto.PagedResponse;
import org.spring.backendspring.payment.PaymentStatus;
import org.spring.backendspring.payment.dto.PaymentDto;
import org.spring.backendspring.payment.entity.PaymentEntity;
import org.spring.backendspring.payment.entity.PaymentItemEntity;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PaymentService {
    PaymentEntity createPayment(PaymentEntity payment);

    PaymentEntity getPayment(Long paymentId);

    List<PaymentEntity> getAllPayments();

    PaymentEntity updatePayment(Long paymentId, PaymentEntity payment);

    void deletePayment(Long paymentId);

    void paymentApproval(String pgToken, Long paymentId, Long productPrice, String productName, Long memberId);

    // ⭐️ [수정된 부분] PaymentServiceImpl과 시그니처를 일치시킵니다.
    String pgRequest(String pg, PaymentDto paymentDto); 

    String getJsonDb();

    Page<PaymentEntity> getPayments(int page, int size, String keyword);

    PagedResponse<PaymentDto> findMyPaymentList(String keyword, Long memberId,  int page, int size);

}