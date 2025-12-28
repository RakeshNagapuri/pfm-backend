package com.pfm.backend.dto.response;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OverBudgetSummaryResponseDto {
	private boolean overallOverBudget;
	private BigDecimal overallExceededAmount;
	private List<CategoryOverBudgetDto>categoryOverBudgets;
}
