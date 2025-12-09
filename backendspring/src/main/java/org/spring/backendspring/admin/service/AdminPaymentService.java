package org.spring.backendspring.admin.service;

import org.spring.backendspring.admin.dto.AdminPaymentDetailDto;
import org.spring.backendspring.admin.dto.WeeklySalesDto;
import org.spring.backendspring.common.dto.PagedResponse;
import org.spring.backendspring.payment.dto.PaymentDto;
import org.spring.backendspring.payment.dto.PaymentItemDto;

import java.util.List;

public interface AdminPaymentService {

    PagedResponse<PaymentDto> getAllPayments(String keyword, int page, int size);

    List<PaymentItemDto> getPaymentItemsByPaymentId(Long paymentId);

    public AdminPaymentDetailDto getPayment(Long paymentId);

    void updateStatus(Long paymentId, String status);

    public WeeklySalesDto getWeeklySales();
}
