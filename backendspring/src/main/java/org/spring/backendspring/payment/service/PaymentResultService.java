package org.spring.backendspring.payment.service;

import lombok.RequiredArgsConstructor;
import org.spring.backendspring.payment.dto.PaymentResultDto;
import org.spring.backendspring.payment.entity.PaymentResultEntity;
import org.spring.backendspring.payment.repository.PaymentResultRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentResultService {

    private final PaymentResultRepository paymentResultRepository;

    public PaymentResultDto dbInsert(PaymentResultDto dto) {
        Long id = paymentResultRepository.save(PaymentResultEntity.builder()
                .paymentId(dto.getPaymentId())
                .productPrice(dto.getProductPrice())
                .memberId(dto.getMemberId())
                .productName(dto.getProductName())
                .build()).getPaymentResultId();

        PaymentResultEntity pay = paymentResultRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);

        return PaymentResultDto.builder()
                .paymentResultId(pay.getPaymentResultId())
                .paymentId(pay.getPaymentId())
                .memberId(pay.getMemberId())
                .productName(pay.getProductName())
                .productPrice(pay.getProductPrice())
                .build();
    }

    public List<PaymentResultDto> getList() {
        List<PaymentResultEntity> pays = paymentResultRepository.findAll();
        return pays.stream().map(pay -> PaymentResultDto.builder()
                .paymentResultId(pay.getPaymentResultId())
                .paymentId(pay.getPaymentId())
                .memberId(pay.getMemberId())
                .productName(pay.getProductName())
                .productPrice(pay.getProductPrice())
                .build()).collect(Collectors.toList());
    }
}
