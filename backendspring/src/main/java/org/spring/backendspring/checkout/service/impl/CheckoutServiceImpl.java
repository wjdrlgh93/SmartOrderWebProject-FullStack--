package org.spring.backendspring.checkout.service.impl;

import lombok.RequiredArgsConstructor;
import org.spring.backendspring.cart.entity.CartEntity;
import org.spring.backendspring.cart.entity.CartItemEntity;
import org.spring.backendspring.cart.repository.CartRepository;
import org.spring.backendspring.checkout.service.CheckoutService;
import org.spring.backendspring.payment.entity.PaymentEntity;
import org.spring.backendspring.payment.entity.PaymentItemEntity;
import org.spring.backendspring.payment.repository.PaymentRepository;
import org.spring.backendspring.payment.repository.PaymentItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class CheckoutServiceImpl implements CheckoutService {

    private final CartRepository cartRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentItemRepository paymentItemRepository;

    @Override
    public PaymentEntity checkoutCart(Long cartId, String paymentAddr, String paymentMethod) {
        // 1️⃣ 장바구니 조회: Fetch Join 메서드 사용 (N+1 방지)
        CartEntity cart = cartRepository.findByIdWithItems(cartId)
                .orElseThrow(() -> new RuntimeException("장바구니를 찾을 수 없습니다."));

        // 2️⃣ 결제 정보 생성
        PaymentEntity payment = PaymentEntity.builder()
                .memberId(cart.getMemberId())
                .paymentAddr(paymentAddr)
                .paymentMethod(paymentMethod)
                .paymentResult("READY")
                .paymentType("CARD")
                .build();

        PaymentEntity savedPayment = paymentRepository.save(payment);

        // 3️⃣ 장바구니 상품 → 결제상품으로 변환
        for (CartItemEntity cartItem : cart.getCartItemEntities()) {
            PaymentItemEntity paymentItem = PaymentItemEntity.builder()
                    .payment(savedPayment)
                    .itemId(cartItem.getItemEntity() != null ? cartItem.getItemEntity().getId() : null)
                    .size(cartItem.getItemSize())
                    .price(cartItem.getItemEntity() != null ? cartItem.getItemEntity().getItemPrice() : 0)
                    .title(cartItem.getItemEntity() != null ? cartItem.getItemEntity().getItemTitle() : "상품")
                    .s3file(cartItem.getItemEntity() != null && !cartItem.getItemEntity().getItemImgEntities().isEmpty()
                            ? cartItem.getItemEntity().getItemImgEntities().get(0).getNewName()
                            : null)
                    .createTime(LocalDateTime.now())
                    .updateTime(LocalDateTime.now())
                    .build();

            // ⭐️ 수정: Payment 엔티티에 Item을 추가하여 양방향 관계의 무결성 유지
            savedPayment.getPaymentItemEntities().add(paymentItem); 

            paymentItemRepository.save(paymentItem);
        }

        return savedPayment;
    }
}