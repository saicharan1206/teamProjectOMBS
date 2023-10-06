package com.jspiders.ombs.dto;

import com.jspiders.ombs.util.Auditing;

import lombok.Data;

@Data
public class ProductResponseDTO extends Auditing{
	private int productId;
	private String productName;
	private double productPrice;
	private int productQuantity;
}
