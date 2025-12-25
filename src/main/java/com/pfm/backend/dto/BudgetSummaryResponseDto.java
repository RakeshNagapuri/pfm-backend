package com.pfm.backend.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BudgetSummaryResponseDto {
	private String month;
	private BigDecimal budget;
	private BigDecimal spent;
	private BigDecimal remaining;
	private boolean overBudget; 
}
