package com.pfm.backend.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransactionResponseDto {
	private Long id;
	private BigDecimal amount;
	private String type;
	private LocalDate transactionDate;
	private String description;
	private CategoryResponseDto category;
}
