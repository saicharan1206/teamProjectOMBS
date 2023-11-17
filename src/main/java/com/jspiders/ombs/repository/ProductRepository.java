package com.jspiders.ombs.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jspiders.ombs.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Integer>
{
	public Optional<Product> findById(int productId);
}
