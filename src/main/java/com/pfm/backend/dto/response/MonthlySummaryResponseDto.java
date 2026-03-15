package com.pfm.backend.dto.response;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonthlySummaryResponseDto implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String month;
    private BigDecimal income;
    private BigDecimal expenses;
    private BigDecimal savings;
}
