package com.jspiders.ombs.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jspiders.ombs.entity.Product;

public interface ProductRepo extends JpaRepository<Product, Integer> {

}
