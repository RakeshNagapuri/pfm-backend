package com.pfm.backend.dto.response;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OverallBudgetVsActualDto {
	private String month;
	private BigDecimal budgeted;
	private BigDecimal spent;
	private BigDecimal remaining;
	private boolean overBudget;
}
