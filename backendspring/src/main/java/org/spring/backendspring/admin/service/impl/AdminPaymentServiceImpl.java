package org.spring.backendspring.admin.service.impl;

import java.util.Optional;
import org.spring.backendspring.admin.dto.AdminPaymentDetailDto;
import org.spring.backendspring.admin.dto.WeeklySalesDto;
import org.spring.backendspring.admin.repository.AdminPaymentRepository;
import org.spring.backendspring.admin.service.AdminPaymentService;
import org.spring.backendspring.common.dto.PagedResponse;
import org.spring.backendspring.member.entity.MemberEntity;
import org.spring.backendspring.member.repository.MemberRepository;
import org.spring.backendspring.payment.PaymentStatus;
import org.spring.backendspring.payment.dto.PaymentDto;
import org.spring.backendspring.payment.dto.PaymentItemDto;
import org.spring.backendspring.payment.entity.PaymentEntity;
import org.spring.backendspring.payment.entity.PaymentItemEntity;
import org.spring.backendspring.payment.repository.PaymentItemRepository;
import org.spring.backendspring.payment.repository.PaymentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminPaymentServiceImpl implements AdminPaymentService {

    private final AdminPaymentRepository adminPaymentRepository;
    private final PaymentItemRepository paymentItemRepository;
    private final MemberRepository memberRepository;

    @Override
    public PagedResponse<PaymentDto> getAllPayments(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("paymentId").descending());
        Page<PaymentDto> paymentPage;

        if (keyword == null || keyword.trim().isEmpty()) {
            paymentPage = adminPaymentRepository.findAll(pageable)
                    .map(entity -> PaymentDto.fromEntity(entity));
        } else {
            paymentPage = adminPaymentRepository
                    .findByPaymentIdContainingIgnoreCase(keyword, pageable)
                    .map(entity -> PaymentDto.fromEntity(entity));
        }
        return PagedResponse.of(paymentPage);
    }

    @Override
    public List<PaymentItemDto> getPaymentItemsByPaymentId(Long paymentId) {
        List<PaymentItemEntity> items = paymentItemRepository.findByPayment_PaymentId(paymentId);

        return items.stream()
                .map(PaymentItemDto::fromEntity)
                .toList();
    }

    @Override
    public AdminPaymentDetailDto getPayment(Long paymentId) {
        AdminPaymentDetailDto detailDto = adminPaymentRepository.findById(paymentId)
                .map(AdminPaymentDetailDto::toDto)
                .orElseThrow(() -> new RuntimeException("결제 정보 없음"));

        Optional<MemberEntity> memberEntity = memberRepository.findById(detailDto.getMemberId());
        if (memberEntity.isEmpty()) {
            detailDto.setMemberStatus("탈퇴회원");
        }
        return detailDto;
    }

    @Override
    public void updateStatus(Long paymentId, String status) {

        PaymentEntity payment = adminPaymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("결제 정보 찾을 수 없음"));

        // String → Enum 변환
        PaymentStatus newStatus = PaymentStatus.valueOf(status);

        payment.setPaymentStatus(newStatus);

        adminPaymentRepository.save(payment);
    }

    @Override
public WeeklySalesDto getWeeklySales() {

    LocalDate today = LocalDate.now();

    LocalDate thisWeekStart = today.with(DayOfWeek.MONDAY);
    LocalDate lastWeekStart = thisWeekStart.minusWeeks(1);

    long[] thisWeek = new long[7];
    long[] lastWeek = new long[7];

    // 이번주
    for (int i = 0; i < 7; i++) {
        LocalDate day = thisWeekStart.plusDays(i);
        LocalDateTime start = day.atStartOfDay();
        LocalDateTime end = day.plusDays(1).atStartOfDay();

        Long sum = adminPaymentRepository.sumSalesByDate(start, end);
        thisWeek[i] = (sum != null) ? sum : 0L;
    }

    // 지난주
    for (int i = 0; i < 7; i++) {
        LocalDate day = lastWeekStart.plusDays(i);
        LocalDateTime start = day.atStartOfDay();
        LocalDateTime end = day.plusDays(1).atStartOfDay();

        Long sum = adminPaymentRepository.sumSalesByDate(start, end);
        lastWeek[i] = (sum != null) ? sum : 0L;
    }

    return WeeklySalesDto.builder()
            .thisWeek(thisWeek)
            .lastWeek(lastWeek)
            .build();
}

}
