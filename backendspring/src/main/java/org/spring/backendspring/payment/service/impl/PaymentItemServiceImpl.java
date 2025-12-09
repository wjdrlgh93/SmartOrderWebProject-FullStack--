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

    //결제 상품 아이템 등록
    @Override
    public PaymentItemEntity createPaymentItem(PaymentItemEntity item) {
       
        item.setCreateTime(LocalDateTime.now());
        item.setUpdateTime(LocalDateTime.now());
        
        return itemRepository.save(item);
    }

    //특정 결제에 포함된 아이템 목록 조회
    @Override
    public List<PaymentItemEntity> getItemsByPaymentId(Long paymentId) {
        return itemRepository.findByPayment_PaymentId(paymentId);
    }

    //특정 아이템 조회
    @Override
    public PaymentItemEntity getPaymentItem(Long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("결제 상품 정보를 찾을 수 없습니다."));
    }

    //결제 아이템 수정
    @Override
    public PaymentItemEntity updatePaymentItem(Long id, PaymentItemEntity item) {
        item.setId(id);
        item.setUpdateTime(LocalDateTime.now());
        return itemRepository.save(item);
    }

    //결제 아이템 삭제
    @Override
    public void deletePaymentItem(Long id) {
        itemRepository.deleteById(id);
    }
}