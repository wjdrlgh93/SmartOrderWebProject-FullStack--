package org.spring.backendspring.admin.controller;

import org.spring.backendspring.admin.dto.WeeklySalesDto;
import org.spring.backendspring.admin.repository.AdminPaymentRepository;
import org.spring.backendspring.admin.service.AdminPaymentService;
import org.spring.backendspring.common.dto.PagedResponse;
import org.spring.backendspring.payment.dto.PaymentDto;
import org.spring.backendspring.payment.dto.PaymentItemDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/payments")
public class AdminPaymentController {

    private final AdminPaymentService adminPaymentService;
    private final AdminPaymentRepository adminPaymentRepository;

    @GetMapping
    public ResponseEntity<PagedResponse<PaymentDto>> getPayment(
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {

        // 서비스에서 페이징 처리된 결제 목록을 가져 온다
        PagedResponse<PaymentDto> paymentList = adminPaymentService.getAllPayments(keyword, page, size);

        return ResponseEntity.ok(paymentList);
    }

    @PutMapping("/updateStatus/{paymentId}")
    public ResponseEntity<String> updateStatus(
            @PathVariable Long paymentId,
            @RequestBody Map<String, String> request) {
        String status = request.get("status");
        adminPaymentService.updateStatus(paymentId, status);
        return ResponseEntity.ok("결제상태가 변경되었습니다");
    }

    @GetMapping("/total")
    public long getTotalPayments() {
        return adminPaymentRepository.countAll();
    }

    @GetMapping("/today")
    public long getTodayPayments() {
        return adminPaymentRepository.countToday();
    }

    @GetMapping("/totalSales")
    public long getTotalSales() {
        return adminPaymentRepository.totalSales();
    }

    @GetMapping("/todaySales")
    public long getTodaySales() {
        return adminPaymentRepository.todaySales();
    }

    @GetMapping("/weeklySales")
    public ResponseEntity<WeeklySalesDto> getWeeklySales() {
        return ResponseEntity.ok(adminPaymentService.getWeeklySales());
    }

    @GetMapping("/{paymentId:\\d+}")
    public ResponseEntity<?> getPaymentDetail(@PathVariable Long paymentId) {
        return ResponseEntity.ok(
                adminPaymentService.getPayment(paymentId));
    }

}
