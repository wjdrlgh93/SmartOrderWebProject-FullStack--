package org.spring.backendspring.admin.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class WeeklySalesDto {
    private long[] thisWeek;  // 월~일 7개
    private long[] lastWeek;  // 월~일 7개
}
