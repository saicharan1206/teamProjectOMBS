package com.jspiders.ombs.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jspiders.ombs.dto.ProductRequestDTO;
import com.jspiders.ombs.dto.ProductResponseDTO;
import com.jspiders.ombs.service.ProductService;
import com.jspiders.ombs.util.ResponseStructure;

@CrossOrigin
@RestController
@Validated
public class ProductController {
	
	@Autowired
	ProductService productService;
	
	@PostMapping("/prod/addProduct/{userId}")
	public ResponseEntity<ResponseStructure> addProduct(@RequestBody ProductRequestDTO productRequestDTO, @PathVariable Integer userId){
		return productService.addProduct(productRequestDTO, userId);
	}
	
	@GetMapping("/prod/fetchAll")
	public ResponseEntity<ResponseStructure<List<ProductResponseDTO>>> fetchProducts(){
		return productService.fetchProducts();
	}
	
	@DeleteMapping("/prod/deleteProducts")
	public ResponseEntity<ResponseStructure<String>> deleteProducts(@RequestParam Integer...prodId){
		return productService.deleteProducts(prodId);
	}
}
