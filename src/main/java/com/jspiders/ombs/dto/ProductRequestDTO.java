package com.jspiders.ombs.dto;

import java.time.LocalDateTime;

public class ProductRequestDTO {
	private String pName;
	private int quantity;
	private double price;
	private LocalDateTime mDate;

	public String getpName() {
		return pName;
	}

	public void setpName(String pName) {
		this.pName = pName;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public LocalDateTime getmDate() {
		return mDate;
	}

	public void setmDate(LocalDateTime mDate) {
		this.mDate = mDate;
	}

}
