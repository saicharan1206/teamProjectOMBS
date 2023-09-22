package com.jspiders.ombs.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ProductRequestDTO {
	
	@NotBlank
	@NotNull(message = "Product Code field should not be empty")
	private String productCode;
	
	@NotBlank
	@NotNull(message = "Product name field should not be empty")
	private String productName;
	
//	@NotBlank
	@NotNull(message = "Quantity field should not be empty")
	private int productQuantity;
	
//	@NotBlank
	@NotNull(message = "Product Price field should not be empty")
	private double productPrize;
	
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public int getProductQuantity() {
		return productQuantity;
	}
	public void setProductQuantity(int productQuantity) {
		this.productQuantity = productQuantity;
	}
	public double getProductPrize() {
		return productPrize;
	}
	public void setProductPrize(double productPrize) {
		this.productPrize = productPrize;
	}
}
