package com.jspiders.ombs.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jspiders.ombs.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
	
	public Product findByProdName(String prodName);

}
