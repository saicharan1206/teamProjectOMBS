package com.jspiders.ombs.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ProductRequestDTO {
	
	@NotBlank(message = "Product Name cannot be Blank!!")
	@NotNull(message = "Product Name cannot be Null!!")
	private String productName;
	@NotBlank(message = "Product Price cannot be Blank!!")
	@NotNull(message = "Product Price cannot be Null!!")
	private double productPrice;
	@NotBlank(message = "Product Quantity cannot be Blank!!")
	@NotNull(message = "Product Quantity cannot be Null!!")
	private int productQty;
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public double getProductPrice() {
		return productPrice;
	}
	public void setProductPrice(double productPrice) {
		this.productPrice = productPrice;
	}
	public int getProductQty() {
		return productQty;
	}
	public void setProductQty(int productQty) {
		this.productQty = productQty;
	}

}
