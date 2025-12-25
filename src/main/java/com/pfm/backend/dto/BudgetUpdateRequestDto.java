package com.pfm.backend.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class BudgetUpdateRequestDto {
	private BigDecimal amount;
}
