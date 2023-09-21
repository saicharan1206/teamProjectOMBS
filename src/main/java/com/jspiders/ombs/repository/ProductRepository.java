package com.jspiders.ombs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jspiders.ombs.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

}
