package org.spring.backendspring.checkout.service;

import org.spring.backendspring.payment.entity.PaymentEntity;

public interface CheckoutService {

    /**
     * 장바구니를 결제로 변환
     */
    PaymentEntity checkoutCart(Long cartId, String paymentAddr, String paymentMethod);
}
