package org.spring.backendspring.payment.controller;

import lombok.RequiredArgsConstructor;
import org.spring.backendspring.common.dto.PagedResponse;
import org.spring.backendspring.payment.dto.PaymentDto;
import org.spring.backendspring.payment.dto.PaymentResultDto;
import org.spring.backendspring.payment.entity.PaymentEntity;
import org.spring.backendspring.payment.service.PaymentService;
import org.spring.backendspring.payment.service.PaymentResultService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final PaymentResultService paymentResultService;

    // -----------------
    // CRUD 기본 메서드
    // -----------------
    @PostMapping
    public PaymentDto create(@RequestBody PaymentDto paymentDto) {
        return PaymentDto.fromEntity(paymentService.createPayment(paymentDto.toEntity()));
    }

    @GetMapping("/{paymentId}")
    public PaymentDto get(@PathVariable("paymentId") Long paymentId) {
        return PaymentDto.fromEntity(paymentService.getPayment(paymentId));
    }

    @GetMapping
    public List<PaymentDto> getAll() {
        return paymentService.getAllPayments()
                .stream()
                .map(PaymentDto::fromEntity)
                .collect(Collectors.toList());
    }

    @PutMapping("/{paymentId}")
    public PaymentDto update(@PathVariable("paymentId") Long paymentId,
             @RequestBody PaymentDto paymentDto) {
        return PaymentDto.fromEntity(paymentService.updatePayment(paymentId, paymentDto.toEntity()));
    }

    @DeleteMapping("/{paymentId}")
    public String delete(@PathVariable("paymentId") Long paymentId) {
        paymentService.deletePayment(paymentId);
        return "삭제 완료";
    }

    // -----------------
    // Kakao Pay 관련
    // -----------------

    @GetMapping("/approval/{paymentId}/{productPrice}/{memberId}")
    public ResponseEntity<Void> approval(
            // ⭐️ 수정: @PathVariable 이름 명시
            @PathVariable("paymentId") Long paymentId, 
            @PathVariable("productPrice") Long productPrice,
            @PathVariable("memberId") Long memberId,
            @RequestParam("pg_token") String pgToken,
            @RequestParam("productName") String productName) {
        try {
            paymentService.paymentApproval(pgToken, paymentId, productPrice, productName, memberId);

            // 성공 시 프론트의 /payment/success로 redirect
            String redirectUrl = "http://localhost:3000/payment/success" +
                    "?paymentId=" + paymentId +
                    "&productPrice=" + productPrice +
                    "&memberId=" + memberId +
                    "&productName=" + URLEncoder.encode(productName, StandardCharsets.UTF_8);

            return ResponseEntity.status(HttpStatus.FOUND)
                    .header("Location", redirectUrl)
                    .build();
        } catch (Exception e) {
            // 실패 시 프론트의 /payment/fail로 redirect
            String failUrl = "http://localhost:3000/payment/fail?paymentId=" + paymentId;
            return ResponseEntity.status(HttpStatus.FOUND)
                    .header("Location", failUrl)
                    .build();
        }
    }

    @PostMapping("/pg/{pg}")
    public Map<String, Object> pgRequest(
            @PathVariable("pg") String pg,
            @RequestBody PaymentDto paymentDto) {

        Map<String, Object> map = new HashMap<>();

        String approvalUrl = paymentService.pgRequest(pg, paymentDto); 

        map.put("approvalUrl", approvalUrl);
        return map;
    }

    @PostMapping("/fail")
    public Map<String, Object> fail(
            @RequestBody PaymentDto paymentDto,
            @RequestParam(value = "memberId") String memberId) { 
        Map<String, Object> map = new HashMap<>();
        map.put("status", "fail 처리 완료");
        return map;
    }

    @GetMapping("/db")
    public Map<String, Object> getDb() {
        Map<String, Object> map = new HashMap<>();
        String dbJsonData = paymentService.getJsonDb();
        map.put("kakaoData", dbJsonData);
        return map;
    }

    @PostMapping("/insert")
    public Map<String, Object> dbInsert(@RequestBody PaymentResultDto dto) {
        Map<String, Object> map = new HashMap<>();
        PaymentResultDto payResult = paymentResultService.dbInsert(dto);
        map.put("payResult", payResult);
        return map;
    }

    @GetMapping("/list")
    public Map<String, Object> getList() {
        Map<String, Object> map = new HashMap<>();
        List<PaymentResultDto> lists = paymentResultService.getList();
        map.put("payRsList", lists);
        return map;
    }

    @GetMapping("/page")
    public PagedResponse<PaymentDto> getPayments(
            // ⭐️ 수정: @RequestParam 이름 명시
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "keyword", required = false) String keyword) {

        Page<PaymentEntity> pageResult = paymentService.getPayments(page, size, keyword);
        Page<PaymentDto> dtoPage = pageResult.map(PaymentDto::fromEntity);
        return PagedResponse.of(dtoPage);
    }

    // 회원의 결제 목록을 가져옵니다.
    @GetMapping("/myPayment/{memberId}")
    public ResponseEntity<?> getMemberPaymentList(@PathVariable("memberId") Long memberId,
                                                 @RequestParam(name = "keyword", required = false) String keyword,
                                                 @RequestParam(name = "page", defaultValue = "0") int page,
                                                 @RequestParam(name = "size", defaultValue = "4") int size) {
        PagedResponse<PaymentDto> myPaymentList = paymentService.findMyPaymentList(keyword, memberId, page, size);
        return ResponseEntity.ok(myPaymentList);
    }
}