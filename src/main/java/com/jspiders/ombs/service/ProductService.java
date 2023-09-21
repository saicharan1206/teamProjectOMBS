package com.jspiders.ombs.service;

import org.springframework.http.ResponseEntity;

import com.jspiders.ombs.dto.ProductRequestDTO;
import com.jspiders.ombs.dto.ProductResponseDTO;
import com.jspiders.ombs.util.ResponseStructure;

public interface ProductService {

	ResponseEntity<ResponseStructure<ProductResponseDTO>> addProduct(ProductRequestDTO request);

	ResponseEntity<ResponseStructure<ProductResponseDTO>> updateProduct(ProductRequestDTO request, int productId);

	ResponseEntity<ResponseStructure<ProductResponseDTO>> products();

	ResponseEntity<ResponseStructure<ProductResponseDTO>> deleteProduct(int prodcutid);


}
