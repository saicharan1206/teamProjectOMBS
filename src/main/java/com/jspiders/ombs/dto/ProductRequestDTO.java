package com.jspiders.ombs.dto;

import jakarta.validation.constraints.NotBlank;

public class ProductRequestDTO {

	@NotBlank(message = "ProductName required")
	private String productName;
	@NotBlank(message = "ProductPrice required")
	private Double productPrice;
	@NotBlank(message = "ProductQuantity required")
	private Integer productQuantity;
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public Double getProductPrice() {
		return productPrice;
	}
	public void setProductPrice(Double productPrice) {
		this.productPrice = productPrice;
	}
	public Integer getProductQuantity() {
		return productQuantity;
	}
	public void setProductQuantity(Integer productQuantity) {
		this.productQuantity = productQuantity;
	}
	
	
}
