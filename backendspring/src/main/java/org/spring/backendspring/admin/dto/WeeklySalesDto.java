package org.spring.backendspring.admin.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class WeeklySalesDto {
    private long[] thisWeek;  
    private long[] lastWeek;  
}
