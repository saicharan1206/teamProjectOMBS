package com.jspiders.ombs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.jspiders.ombs.entity.Products;

public interface ProductsRepo extends JpaRepository<Products, Integer>{

}
