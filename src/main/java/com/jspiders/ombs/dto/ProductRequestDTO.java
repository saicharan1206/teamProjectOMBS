package com.jspiders.ombs.dto;

import lombok.Data;

@Data
public class ProductRequestDTO {
	private String productName;
	private double productPrice; 
	private int productQuantity;
}
