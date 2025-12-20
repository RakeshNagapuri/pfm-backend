package com.pfm.backend.dto.response;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BudgetResponseDto {
	private Long id;
	private String month;
	private BigDecimal amount;
	private CategoryResponseDto category;
}
