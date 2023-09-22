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
import org.springframework.web.bind.annotation.RestController;

import com.jspiders.ombs.dto.ProductRequestDTO;
import com.jspiders.ombs.dto.ProductResponseDTO;
import com.jspiders.ombs.dto.UserRequestDTO;
import com.jspiders.ombs.dto.UserResponseDTO;
import com.jspiders.ombs.entity.Product;
import com.jspiders.ombs.service.ProductService;
import com.jspiders.ombs.util.ResponseStructure;

@CrossOrigin
@RestController
public class ProductController {

	@Autowired
	private ProductService service;
	
	@PostMapping("/product")
	public ResponseEntity<ResponseStructure<ProductResponseDTO>> addProduct(@RequestBody ProductRequestDTO requestDTO)
	{
		return service.addProduct(requestDTO);
	}
	
	@PutMapping("/prduct/{productid}/update")
	public ResponseEntity<ResponseStructure<ProductResponseDTO>> updateProduct(@RequestBody ProductRequestDTO requestDTO,@PathVariable int productid)
	{
		return service.updateProduct(requestDTO,productid);
	}
	@GetMapping("/products")
	public ResponseEntity<ResponseStructure<List<ProductResponseDTO>>> products()
	{
		return service.products();
	}
	@DeleteMapping("/product/{productid}/delete")
	public ResponseEntity<ResponseStructure<ProductResponseDTO>> deleteProduct(@PathVariable int productid)
	{
		return service.deleteProduct(productid);
	}	
}
