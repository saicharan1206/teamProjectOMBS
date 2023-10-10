package com.jspiders.ombs.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProductRequestDTO {
	
	@NotBlank(message = "Product Name required")
	private String productName;
	
	@NotBlank(message = "Quantity required")
	private Integer quantity;
	
	@NotBlank(message = "price required")
	private Double productPrice;
}
