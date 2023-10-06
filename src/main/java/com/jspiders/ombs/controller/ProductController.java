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
	private ProductService service;
	
	@PostMapping("/users/{userid}/products")
	public ResponseEntity<ResponseStructure<ProductResponseDTO>> saveProduct(@RequestBody ProductRequestDTO requestDTO,@PathVariable int userid)
	{
		return service.saveProduct(requestDTO,userid);
	}
	
	@PutMapping("/products/{productid}")
	public ResponseEntity<ResponseStructure<ProductResponseDTO>> updateProduct(@RequestBody ProductRequestDTO requestDTO,@PathVariable int productid)
	{
		return service.updateProduct(requestDTO,productid);
	}
	@GetMapping("/users/{userid}/products")
	public ResponseEntity<ResponseStructure<List<ProductResponseDTO>>> products(@PathVariable int userid)
	{
		return service.products(userid);
	}
	@DeleteMapping("/users/{userid}/products/{productid}")
	public ResponseEntity<ResponseStructure<ProductResponseDTO>> deleteProduct(@PathVariable int userid,@PathVariable int productid)
	{
		return service.deleteProduct(userid,productid);
	}
	@GetMapping("/products")
	public ResponseEntity<ResponseStructure<ProductResponseDTO>> findallproducts()
	{
		return service.findallproducts();
	}
	@DeleteMapping("/products")
	public ResponseEntity<ResponseStructure<ProductResponseDTO>> deleteAllProducts(@RequestParam int[] productid)
	{
		return service.deleteAllProducts(productid);
	}
	
	
}
