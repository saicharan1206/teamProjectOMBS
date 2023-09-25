package com.jspiders.ombs.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.jspiders.ombs.dto.ProductRequestDTO;
import com.jspiders.ombs.dto.ProductResponseDTO;
import com.jspiders.ombs.util.ResponseStructure;

public interface ProductService {

	ResponseEntity<ResponseStructure<ProductResponseDTO>> saveProduct(ProductRequestDTO requestDTO, int userid);

	ResponseEntity<ResponseStructure<ProductResponseDTO>> updateProduct(ProductRequestDTO requestDTO, int productid);

	ResponseEntity<ResponseStructure<List<ProductResponseDTO>>> products(int userid);

	ResponseEntity<ResponseStructure<ProductResponseDTO>> deleteProduct(int userid, int productid);

}
