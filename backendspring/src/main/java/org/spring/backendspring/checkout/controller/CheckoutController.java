package org.spring.backendspring.checkout.controller;

import lombok.RequiredArgsConstructor;
import org.spring.backendspring.checkout.service.CheckoutService;
import org.spring.backendspring.payment.entity.PaymentEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/checkout")
@RequiredArgsConstructor
public class CheckoutController {

    private final CheckoutService checkoutService;

    // POST /api/checkout/{cartId}?paymentAddr=...&paymentMethod=...
    @PostMapping("/{cartId}")
    public PaymentEntity checkoutCart(
            @PathVariable("cartId") Long cartId, 
            @RequestParam("paymentAddr") String paymentAddr, // -parameters 플래그 오류 방지
            @RequestParam("paymentMethod") String paymentMethod) { // -parameters 플래그 오류 방지

        return checkoutService.checkoutCart(cartId, paymentAddr, paymentMethod);
    }
}