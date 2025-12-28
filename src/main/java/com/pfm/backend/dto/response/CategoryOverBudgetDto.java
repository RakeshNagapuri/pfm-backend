package com.pfm.backend.dto.response;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategoryOverBudgetDto {
	private CategoryResponseDto category;
	private BigDecimal exceededAmount;
}
