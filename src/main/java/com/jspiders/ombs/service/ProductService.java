package com.jspiders.ombs.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.jspiders.ombs.dto.ProductRequestDTO;
import com.jspiders.ombs.dto.ProductResponseDTO;
import com.jspiders.ombs.util.ResponseStructure;


public interface ProductService {

	public ResponseEntity<ResponseStructure> addProduct(ProductRequestDTO productRequestDTO, Integer userId);
	
	public ResponseEntity<ResponseStructure<List<ProductResponseDTO>>> fetchProducts();
	
	public ResponseEntity<ResponseStructure<String>> deleteProducts(Integer...prodIds);
}
