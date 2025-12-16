package com.pfm.backend.dto;

import lombok.Data;

@Data
public class CategoryRequestDto {
	private String name;
	private String type; // INCOME / EXPENSE
}
