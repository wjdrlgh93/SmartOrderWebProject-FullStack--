package org.spring.backendspring.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SummaryDto {
    
    private Long totalMembers;
    private Long todayMembers;

    private Long totalCrews;
    private Long todayCrews;

    private Long totalPayments;
    private Long todayPayments;

    private Long totalSales;
    private Long todaySales;

    private Long totalBoards;
    private Long todayBoards;
}
