package com.jspiders.ombs.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jspiders.ombs.dto.ProductRequestDTO;
import com.jspiders.ombs.dto.ProductResponseDTO;
import com.jspiders.ombs.service.ProductService;
import com.jspiders.ombs.util.ResponseStructure;

@RestController
@CrossOrigin
public class ProductController {

	@Autowired
	private ProductService productService;
	
	@PostMapping("/users/{userid}/product")
	public ResponseEntity<ResponseStructure<ProductResponseDTO>> saveProduct(@PathVariable int userid,   @RequestBody ProductRequestDTO request){
		return productService.saveProduct(userid,request);
	}
	
	@PutMapping("/products/{prodcutid}/update")
	public ResponseEntity<ResponseStructure<ProductResponseDTO>> updateProduct(@RequestBody ProductRequestDTO request,@PathVariable int prodcutid){
		return productService.updateProduct(request,prodcutid);
	}
	
	@GetMapping("users/{userid}/products")
	public ResponseEntity<ResponseStructure<List<ProductResponseDTO>>> products(@PathVariable int userid){
		return productService.products(userid);
	}
	
	@DeleteMapping("users/{userid}/products/{prodcutid}/delete")
	public ResponseEntity<ResponseStructure<ProductResponseDTO>> deleteProduct(@PathVariable int userid,@PathVariable int prodcutid){
		return productService.deleteProduct(userid,prodcutid);
	}
}
