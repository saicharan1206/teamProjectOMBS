package com.jspiders.ombs.entity;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.jspiders.ombs.util.Auditing;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
public class Product extends Auditing{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int productId;
	private String productName;
	private double productPrice; 
	private int productQuantity;
	@ManyToOne
	private User user;
}
