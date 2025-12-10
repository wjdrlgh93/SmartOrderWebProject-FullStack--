package org.spring.backendspring.payment.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.spring.backendspring.cart.entity.CartEntity;
import org.spring.backendspring.cart.repository.CartItemRepository;
import org.spring.backendspring.cart.repository.CartRepository;
import org.spring.backendspring.common.dto.PagedResponse;
import org.spring.backendspring.payment.PaymentStatus; 
import org.spring.backendspring.payment.dto.KakaoPayPrepareDto;
import org.spring.backendspring.payment.dto.PaymentDto;
import org.spring.backendspring.payment.dto.PaymentItemDto;

import org.spring.backendspring.payment.entity.PaymentEntity;
import org.spring.backendspring.payment.entity.PaymentItemEntity;
import org.spring.backendspring.payment.repository.PaymentRepository;
import org.spring.backendspring.payment.repository.PaymentResultRepository;
import org.spring.backendspring.payment.service.PaymentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private static final String KAKAO_AUTH_KEY = "5153d372489b6c481c38dab7bb500441";
    private static final String KAKAO_CID = "TC0ONETIME";

    private final PaymentRepository paymentRepository;
    private final PaymentResultRepository paymentResultRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final EntityManager entityManager;



    @Override
    public PaymentEntity createPayment(PaymentEntity payment) {
        return paymentRepository.save(
                PaymentEntity.builder()
                        .memberId(payment.getMemberId())
                        .paymentAddr(payment.getPaymentAddr())
                        .paymentMethod(payment.getPaymentMethod())
                        .paymentPost(payment.getPaymentPost())
                        .paymentResult(payment.getPaymentResult())
                        .paymentType(payment.getPaymentType())
                        .paymentStatus(payment.getPaymentStatus())
                        .build());
    }

    @Override
    public PaymentEntity getPayment(Long paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("결제 정보를 찾을 수 없습니다."));
    }

    @Override
    public List<PaymentEntity> getAllPayments() {
        return paymentRepository.findAll();
    }

    @Override
    public PaymentEntity updatePayment(Long paymentId, PaymentEntity payment) {
        PaymentEntity existing = getPayment(paymentId);
        existing.setPaymentAddr(payment.getPaymentAddr());
        existing.setPaymentMethod(payment.getPaymentMethod());
        existing.setPaymentPost(payment.getPaymentPost());
        existing.setPaymentResult(payment.getPaymentResult());
        existing.setPaymentType(payment.getPaymentType());
        existing.setPaymentStatus(payment.getPaymentStatus());
        return paymentRepository.save(existing);
    }

    @Override
    public void deletePayment(Long paymentId) {
        paymentRepository.deleteById(paymentId);
    }



    @Override
    @Transactional
    public void paymentApproval(String pgToken, Long paymentId, Long productPrice, String productName, Long memberId) {
        paymentRepository.updatePgToken(paymentId, pgToken);
        PaymentEntity paymentEntity = paymentRepository.findById(paymentId).orElseThrow();

        PaymentDto paymentDto = PaymentDto.fromEntity(paymentEntity);

        PaymentDto getTidPaymentDto = jsonToObject(paymentDto);
        paymentDto.setTid(getTidPaymentDto.getTid());
        paymentDto.setPgToken(pgToken); // DTO에 pgToken 설정

        if (pgToken == null)
            throw new RuntimeException("pgToken이 존재하지 않습니다.");


        int isSucceeded = paymentApproveKakao(paymentDto, paymentId, productPrice, productName, memberId);


        if (isSucceeded == 1) {
            removeCartByMemberId(memberId);
        }
    }

    private PaymentDto jsonToObject(PaymentDto dto) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            return objectMapper.readValue(dto.getPaymentReadyJson(), PaymentDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    private int paymentApproveKakao(PaymentDto paymentDto, Long paymentId, Long productPrice, String productName,
                                    Long memberId) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "KakaoAK " + KAKAO_AUTH_KEY); 

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("cid", KAKAO_CID); 
        params.add("tid", paymentDto.getTid());
        params.add("partner_order_id", String.valueOf(paymentId));
        params.add("partner_user_id", String.valueOf(memberId));
        params.add("pg_token", paymentDto.getPgToken());
        params.add("total_amount", String.valueOf(productPrice));

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);


        ResponseEntity<String> result = restTemplate.postForEntity(
                "https://kapi.kakao.com/v1/payment/approve",
                entity,
                String.class);

        System.out.println("결제 승인 응답: " + result.getBody());

        if (result.getStatusCode() == HttpStatus.OK) {
            paymentRepository.updateIsSucced(paymentId, 1); 
            return 1; 
        } else {
            paymentRepository.updateIsSucced(paymentId, 0); 
            return 0; 
        }
    }

    @Transactional
    private void removeCartByMemberId(Long memberId) {
        CartEntity cart = cartRepository.findByMemberId(memberId).orElse(null);

        if (cart != null) {
            Long cartId = cart.getId();

            cartItemRepository.deleteByCartId(cartId);

            cartRepository.deleteByMemberId(memberId);


            System.out.println("결제 완료 후 회원 ID(" + memberId + ")의 장바구니 전체(ID: " + cartId + ")를 삭제했습니다.");
        }
    }



    @Override
    @Transactional
    public String pgRequest(String pg, PaymentDto paymentDto) {
        
        List<PaymentItemDto> itemDtos = paymentDto.getPaymentItems(); 


        String paymentType = paymentDto.getPaymentType();
        if (paymentType.equals("CARD") || paymentType.equals("CASH")) {
            
            PaymentEntity paymentEntity = paymentDto.toEntity();
            
            for (PaymentItemDto itemDto : itemDtos) {
                PaymentItemEntity itemEntity = itemDto.toEntity(); 
                paymentEntity.addPaymentItem(itemEntity); 
            }

            paymentEntity.setPaymentStatus(PaymentStatus.COMPLETED); // PENDING -> COMPLETED
            paymentEntity.setIsSucceeded(1);

            paymentRepository.save(paymentEntity);

            removeCartByMemberId(paymentDto.getMemberId());

            return "http://localhost:3000/payment/success"; 
        }


        if (!pg.equals("kakao"))
            throw new RuntimeException("제휴되지 않은 결제 업체 입니다.");

        Long memberId = paymentDto.getMemberId();
        long totalAmount = paymentDto.getProductPrice();


        String mainItemName = itemDtos.size() > 1
                ? itemDtos.get(0).getTitle() + " 외 " + (itemDtos.size() - 1) + "건"
                : itemDtos.get(0).getTitle();


        PaymentEntity paymentEntity = paymentDto.toEntity(); 
        paymentEntity.setPaymentType("KAKAO");
        paymentEntity.setProductPrice(totalAmount);
        
        for (PaymentItemDto itemDto : itemDtos) {
            PaymentItemEntity itemEntity = itemDto.toEntity(); 
            paymentEntity.addPaymentItem(itemEntity);
        }

        Long paymentId = paymentRepository.save(paymentEntity).getPaymentId();


        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "KakaoAK " + KAKAO_AUTH_KEY);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("cid", KAKAO_CID);
        params.add("partner_order_id", String.valueOf(paymentId));
        params.add("partner_user_id", String.valueOf(memberId));
        params.add("item_name", mainItemName);
        params.add("quantity", String.valueOf(itemDtos.size())); 
        params.add("total_amount", String.valueOf(totalAmount));
        params.add("tax_free_amount", "0");

        String encodedItemName = URLEncoder.encode(mainItemName, StandardCharsets.UTF_8);

        params.add("approval_url",
                "http://localhost:8088/api/payments/approval/"
                        + paymentId + "/" + totalAmount + "/" + memberId
                        + "?productName=" + encodedItemName);

        params.add("cancel_url", "http://localhost:3000/payment/cancel");
        params.add("fail_url", "http://localhost:3000/payment/fail");

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);


        ResponseEntity<KakaoPayPrepareDto> result = restTemplate.postForEntity(
                "https://kapi.kakao.com/v1/payment/ready",
                entity,
                KakaoPayPrepareDto.class);


        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String kakaoJsonString = objectMapper.writeValueAsString(result.getBody());
            paymentEntity.setPaymentReadyJson(kakaoJsonString);
            paymentRepository.save(paymentEntity);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("카카오 결제 요청 변환 오류", e);
        }


        return result.getBody().getNext_redirect_pc_url();
    }



    @Override
    public String getJsonDb() {
        List<PaymentEntity> list = paymentRepository.findAll();
        List<PaymentDto> jsonDb = list.stream()
                .map(el -> PaymentDto.builder()
                        .paymentReadyJson(el.getPaymentReadyJson())
                        .build())
                .collect(Collectors.toList());

        return "" + jsonDb;
    }

    @Override
    public Page<PaymentEntity> getPayments(int page, int size, String keyword) {
        Pageable pageable = PageRequest.of(page, size);
        if (keyword == null || keyword.isEmpty()) {
            return paymentRepository.findAllWithItems(pageable);
        } else {
            return paymentRepository.findByKeywordWithItems(keyword, pageable);
        }
    }

    @Override
    public PagedResponse<PaymentDto> findMyPaymentList(String keyword, Long memberId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("paymentId").descending());
        Page<PaymentEntity> paymentEntities;
        if (keyword == null || keyword.trim().isEmpty()) {
            paymentEntities = paymentRepository.findByMemberId(pageable, memberId);
        } else {
            paymentEntities = paymentRepository.findByMemberIdAndTitleContaining(pageable, keyword, memberId);
        }
        return PagedResponse.of(paymentEntities.map(PaymentDto::fromEntity));
    }
}