package org.spring.backendspring.admin.service.impl;

import org.spring.backendspring.admin.dto.SummaryDto;
import org.spring.backendspring.admin.repository.AdminBoardRepository;
import org.spring.backendspring.admin.repository.AdminCrewRepository;
import org.spring.backendspring.admin.repository.AdminMemberRepository;
import org.spring.backendspring.admin.repository.AdminPaymentRepository;
import org.spring.backendspring.admin.service.AdminSummaryService;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminSummaryServiceImpl implements AdminSummaryService {

    private final AdminMemberRepository adminMemberRepository;
    private final AdminCrewRepository adminCrewRepository;
    private final AdminPaymentRepository adminPaymentRepository;
    private final AdminBoardRepository adminBoardRepository;

    @Override
    public SummaryDto getSummary() {

        Long totalMembers = adminMemberRepository.countAll();
        Long todayMembers = adminMemberRepository.countToday();

        Long totalCrews = adminCrewRepository.countAll();
        Long todayCrews = adminCrewRepository.countToday();

        Long totalPayments = adminPaymentRepository.countAll();
        Long todayPayments = adminPaymentRepository.countToday();
        Long totalSales = adminPaymentRepository.totalSales();
        Long todaySales = adminPaymentRepository.todaySales();

        Long totalBoards = adminBoardRepository.countAll();
        Long todayBoards = adminBoardRepository.countToday();

        return SummaryDto.builder()
                .totalMembers(totalMembers)
                .todayMembers(todayMembers)
                .totalCrews(totalCrews)
                .todayCrews(todayCrews)
                .totalPayments(totalPayments)
                .todayPayments(todayPayments)
                .totalSales(totalSales)
                .todaySales(todaySales)
                .totalBoards(totalBoards)
                .todayBoards(todayBoards)
                .build();
    }
}
