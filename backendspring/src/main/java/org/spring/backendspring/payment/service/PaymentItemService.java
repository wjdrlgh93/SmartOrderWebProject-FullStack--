package org.spring.backendspring.payment.service;

import org.spring.backendspring.payment.entity.PaymentItemEntity;

import java.util.List;

public interface PaymentItemService {
    PaymentItemEntity createPaymentItem(PaymentItemEntity item);
    List<PaymentItemEntity> getItemsByPaymentId(Long paymentId);
    PaymentItemEntity getPaymentItem(Long id);
    PaymentItemEntity updatePaymentItem(Long id, PaymentItemEntity item);
    void deletePaymentItem(Long id);
}
