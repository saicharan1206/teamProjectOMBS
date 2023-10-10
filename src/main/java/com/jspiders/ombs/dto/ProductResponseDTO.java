package com.jspiders.ombs.dto;

import lombok.Data;

@Data
public class ProductResponseDTO {
	
	private Integer productId;
	private String productName;
	private Integer quantity;
	private Double productPrice;
}
