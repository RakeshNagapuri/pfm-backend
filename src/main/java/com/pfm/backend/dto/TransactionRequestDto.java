package com.pfm.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;

@Data
public class TransactionRequestDto {
	
	private BigDecimal amount;
	private String type;
	private LocalDate transactionDate;
	private String description;
	private Long categoryId;
}
