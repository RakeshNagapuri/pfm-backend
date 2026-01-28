package com.pfm.backend.dto.response;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MonthlySummaryResponseDto {
	private String month;
    private BigDecimal income;
    private BigDecimal expenses;
    private BigDecimal savings;
}
