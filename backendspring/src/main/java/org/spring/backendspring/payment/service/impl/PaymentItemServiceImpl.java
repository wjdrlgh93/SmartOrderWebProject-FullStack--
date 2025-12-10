package org.spring.backendspring.payment.service.impl;

import lombok.RequiredArgsConstructor;
import org.spring.backendspring.payment.entity.PaymentItemEntity;
import org.spring.backendspring.payment.repository.PaymentItemRepository;
import org.spring.backendspring.payment.service.PaymentItemService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentItemServiceImpl implements PaymentItemService {

    private final PaymentItemRepository itemRepository;


    @Override
    public PaymentItemEntity createPaymentItem(PaymentItemEntity item) {
       
        item.setCreateTime(LocalDateTime.now());
        item.setUpdateTime(LocalDateTime.now());
        
        return itemRepository.save(item);
    }


    @Override
    public List<PaymentItemEntity> getItemsByPaymentId(Long paymentId) {
        return itemRepository.findByPayment_PaymentId(paymentId);
    }


    @Override
    public PaymentItemEntity getPaymentItem(Long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("결제 상품 정보를 찾을 수 없습니다."));
    }


    @Override
    public PaymentItemEntity updatePaymentItem(Long id, PaymentItemEntity item) {
        item.setId(id);
        item.setUpdateTime(LocalDateTime.now());
        return itemRepository.save(item);
    }


    @Override
    public void deletePaymentItem(Long id) {
        itemRepository.deleteById(id);
    }
}