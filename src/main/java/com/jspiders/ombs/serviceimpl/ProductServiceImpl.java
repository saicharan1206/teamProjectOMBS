package com.jspiders.ombs.serviceimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.jspiders.ombs.dto.ProductRequestDTO;
import com.jspiders.ombs.dto.ProductResponseDTO;
import com.jspiders.ombs.entity.Product;
import com.jspiders.ombs.repository.ProductRepository;
import com.jspiders.ombs.service.ProductService;
import com.jspiders.ombs.util.ResponseStructure;
import com.jspiders.ombs.util.exception.ProdcutNotFoundException;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	ProductRepository productRepository;
	
	@Override
	public ResponseEntity<ResponseStructure<ProductResponseDTO>> addProduct(ProductRequestDTO request) {
		Product product = new Product();
		product.setProductName(request.getProductName());
		product.setProductPrice(request.getProductPrice());
		product.setProductQuantity(request.getProductQuantityl());
		Product save = productRepository.save(product);
		
		ProductResponseDTO response = new ProductResponseDTO();
		response.setProductName(save.getProductName());
		response.setProductPrice(save.getProductPrice());
		response.setProductID(save.getProductId());
		
		ResponseStructure<ProductResponseDTO> responseStructure = new ResponseStructure<>();
		responseStructure.setData(response);
		responseStructure.setMessage("product created sucessuffly");
		responseStructure.setStatusCode(HttpStatus.CREATED.value());
		return new ResponseEntity(responseStructure, HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<ResponseStructure<ProductResponseDTO>> updateProduct(ProductRequestDTO request, int productId) {
		Optional<Product> product = productRepository.findById(productId);
		if(product.isPresent()) {
			Product pro = product.get();
			pro.setProductName(request.getProductName());
			pro.setProductPrice(request.getProductPrice());
			pro.setProductQuantity(request.getProductQuantityl());
			Product save = productRepository.save(pro);
			
			ProductResponseDTO response = new ProductResponseDTO();
			response.setProductName(save.getProductName());
			response.setProductPrice(save.getProductPrice());
			response.setProductID(save.getProductId());
			
			ResponseStructure<ProductResponseDTO> responseStructure = new ResponseStructure<>();
			responseStructure.setData(response);
			responseStructure.setMessage("product created sucessuffly");
			responseStructure.setStatusCode(HttpStatus.CREATED.value());
			return new ResponseEntity(responseStructure, HttpStatus.CREATED);
		}
		
		throw new ProdcutNotFoundException("product not found");
	}

	@Override
	public ResponseEntity<ResponseStructure<ProductResponseDTO>> products() {
		List<Product> allProduct = productRepository.findAll();
		List<ProductResponseDTO> allprodcuts = new ArrayList<>();
		if(!allProduct.isEmpty()) {
			for (Product product : allProduct) {
				ProductResponseDTO response = new ProductResponseDTO();
				response.setProductName(product.getProductName());
				response.setProductPrice(product.getProductPrice());
				response.setProductID(product.getProductId());
				allprodcuts.add(response);
			}
			ResponseStructure<List<ProductResponseDTO>> responseStructure = new ResponseStructure<>();
			responseStructure.setData(allprodcuts);
			responseStructure.setStatusCode(HttpStatus.OK.value());
			responseStructure.setMessage("product found sucessfully");
			
			
			return new ResponseEntity(responseStructure, HttpStatus.OK);
		}
		throw new ProdcutNotFoundException("data base is empty");
		
	}

	@Override
	public ResponseEntity<ResponseStructure<ProductResponseDTO>> deleteProduct(int prodcutid) {
		
		Optional<Product> product = productRepository.findById(prodcutid);
		if(product.isPresent()) {
			productRepository.delete(product.get());
			
			ProductResponseDTO response = new ProductResponseDTO();
			response.setProductName(product.get().getProductName());
			response.setProductPrice(product.get().getProductPrice());
			response.setProductID(product.get().getProductId());
			
			
			ResponseStructure<ProductResponseDTO> responseStructure = new ResponseStructure<>();
			responseStructure.setData(response);
			responseStructure.setStatusCode(HttpStatus.OK.value());
			responseStructure.setMessage("deleted sucessfully");
			
			return new ResponseEntity(responseStructure, HttpStatus.OK);
		}
		throw new ProdcutNotFoundException("product not found"); 
		
	}

}
