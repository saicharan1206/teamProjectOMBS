package com.jspiders.ombs.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ProductRequestDTO {
	
	@NotBlank(message = "Product Name cannot be Blank!!")
	@NotNull(message = "Product Name cannot be Null!!")
	private String prodName;
	@NotBlank(message = "Product Price cannot be Blank!!")
	@NotNull(message = "Product Price cannot be Null!!")
	private double prodPrice;
	@NotBlank(message = "Product Quantity cannot be Blank!!")
	@NotNull(message = "Product Quantity cannot be Null!!")
	private int prodQty;
	public String getProdName() {
		return prodName;
	}
	public void setProdName(String prodName) {
		this.prodName = prodName;
	}
	public double getProdPrice() {
		return prodPrice;
	}
	public void setProdPrice(double prodPrice) {
		this.prodPrice = prodPrice;
	}
	public int getProdQty() {
		return prodQty;
	}
	public void setProdQty(int prodQty) {
		this.prodQty = prodQty;
	}

}
