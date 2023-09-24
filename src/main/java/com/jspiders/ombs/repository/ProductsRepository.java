package com.jspiders.ombs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.jspiders.ombs.entity.Products;

public interface ProductsRepository extends JpaRepository<Products, Integer> {
	
	public Products findByProductName (String productName);
	
}
