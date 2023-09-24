package com.jspiders.ombs.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.jspiders.ombs.dto.ProductsRequest;
import com.jspiders.ombs.dto.ProductsResponse;
import com.jspiders.ombs.util.ResponseStructure;

public interface ProductsService {

	public ResponseEntity<ResponseStructure<ProductsResponse>> saveProduct (ProductsRequest productsRequest);
	
	public ResponseEntity<ResponseStructure<List<ProductsResponse>>> fetchAllProducts ();
	
	public ResponseEntity<ResponseStructure<ProductsResponse>> updateProduct (ProductsRequest productsRequest, int productId);
	
	public ResponseEntity<ResponseStructure<ProductsResponse>> deleteProduct (int productId);
	
	public ResponseEntity<ResponseStructure<ProductsResponse>> fetchById (int productId);
	
}
