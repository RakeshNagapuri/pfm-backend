package com.pfm.backend.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class BudgetRequestDto {
	private String month;
	private BigDecimal amount;
	private Long categoryId;
}
