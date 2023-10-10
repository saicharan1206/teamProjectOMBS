package com.jspiders.ombs.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.jspiders.ombs.dto.ProductRequestDTO;
import com.jspiders.ombs.dto.ProductResponseDTO;
import com.jspiders.ombs.entity.Product;
import com.jspiders.ombs.util.ResponseStructure;

public interface ProductService {

	public ResponseEntity<ResponseStructure<ProductResponseDTO>> addProduct(ProductRequestDTO productRequestDTO,Integer userId);

	public ResponseEntity<ResponseStructure<List<ProductResponseDTO>>> findAllProducts() ;
	public ResponseEntity<ResponseStructure<String>> deleteProducts(Integer...productIds ) ;
}
