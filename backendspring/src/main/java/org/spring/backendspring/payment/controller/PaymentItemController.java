package org.spring.backendspring.payment.controller;

import lombok.RequiredArgsConstructor;
import org.spring.backendspring.payment.dto.PaymentItemDto; 
import org.spring.backendspring.payment.entity.PaymentItemEntity;
import org.spring.backendspring.payment.service.PaymentItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/payment-items")
@RequiredArgsConstructor
public class PaymentItemController {

    private final PaymentItemService itemService;


    @PostMapping
    public PaymentItemDto create(@RequestBody PaymentItemDto itemDto) {
        PaymentItemEntity entity = itemService.createPaymentItem(itemDto.toEntity());
        return PaymentItemDto.fromEntity(entity);
    }


    @GetMapping("/{id}")
    public PaymentItemDto get(@PathVariable("id") Long id) {
        PaymentItemEntity entity = itemService.getPaymentItem(id);
        return PaymentItemDto.fromEntity(entity);
    }


    @GetMapping("/by-payment/{paymentId}")
    public List<PaymentItemDto> getByPayment(@PathVariable("paymentId") Long paymentId) {
        return itemService.getItemsByPaymentId(paymentId).stream()
                .map(PaymentItemDto::fromEntity)
                .collect(Collectors.toList());
    }


    @PutMapping("/{id}")
    public PaymentItemDto update(@PathVariable("id") Long id, @RequestBody PaymentItemDto itemDto) {
        PaymentItemEntity entity = itemService.updatePaymentItem(id, itemDto.toEntity());
        return PaymentItemDto.fromEntity(entity);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        itemService.deletePaymentItem(id);
        return ResponseEntity.noContent().build();
    }
}