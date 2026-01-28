package com.pfm.backend.dto.response;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategoryExpenseSummaryDto {
	private Long categoryId;
    private String categoryName;
    private BigDecimal totalSpent;
}
