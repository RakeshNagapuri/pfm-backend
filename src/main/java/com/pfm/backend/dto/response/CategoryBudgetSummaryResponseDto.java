package com.pfm.backend.dto.response;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategoryBudgetSummaryResponseDto {
	private CategoryResponseDto category;
	private BigDecimal budget;
	private BigDecimal spent;
	private BigDecimal remaining;
	private boolean overBudget;
}
