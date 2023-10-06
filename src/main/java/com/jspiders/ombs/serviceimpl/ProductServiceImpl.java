package com.jspiders.ombs.serviceimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.support.Repositories;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.jspiders.ombs.dto.ProductRequestDTO;
import com.jspiders.ombs.dto.ProductResponseDTO;
import com.jspiders.ombs.entity.Product;
import com.jspiders.ombs.entity.User;
import com.jspiders.ombs.repository.ProductRepository;
import com.jspiders.ombs.repository.UserRepository;
import com.jspiders.ombs.service.ProductService;
import com.jspiders.ombs.util.ResponseStructure;
import com.jspiders.ombs.util.exception.ProdcutNotFoundException;
import com.jspiders.ombs.util.exception.UserNotFoundException;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	ProductRepository productRepository;
	
	@Autowired
	UserRepository repo;
	
	@Override
	public ResponseEntity<ResponseStructure<ProductResponseDTO>> saveProduct(int userid,  ProductRequestDTO request) {
	
		Optional<User> findById = repo.findById(userid);
		
		if(findById.isPresent()) {
			Product product = new Product();
			product.setProductName(request.getProductName());
			product.setProductPrice(request.getProductPrice());
			product.setProductQuantity(request.getProductQuantityl());
			product.setUser(findById.get());
			Product save = productRepository.save(product);
			
			
			
			ProductResponseDTO response = new ProductResponseDTO();
			response.setProductName(save.getProductName());
			response.setProductPrice(save.getProductPrice());
			response.setProductQuantity(save.getProductQuantity());
			response.setProductID(save.getProductId());
			
			ResponseStructure<ProductResponseDTO> responseStructure = new ResponseStructure<>();
			responseStructure.setData(response);
			responseStructure.setMessage("product created sucessuffly");
			responseStructure.setStatusCode(HttpStatus.CREATED.value());
			return new ResponseEntity(responseStructure, HttpStatus.CREATED);
		}
		throw new UserNotFoundException("no user with this id");
		
		
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
			response.setProductQuantity(save.getProductQuantity());
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
	public ResponseEntity<ResponseStructure<List<ProductResponseDTO>>> products(int userid) {
		Optional<User> findById = repo.findById(userid);
		if(findById.isPresent()) {
			
		
		List<Product> products = findById.get().getProducts();
		List<ProductResponseDTO> allprodcuts = new ArrayList<>();
		if(!products.isEmpty()) {
			for (Product product : products) {
				ProductResponseDTO response = new ProductResponseDTO();
				response.setProductName(product.getProductName());
				response.setProductPrice(product.getProductPrice());
				response.setProductID(product.getProductId());
				response.setProductQuantity(product.getProductQuantity());
				allprodcuts.add(response);
			}
			ResponseStructure<List<ProductResponseDTO>> responseStructure = new ResponseStructure<>();
			responseStructure.setData(allprodcuts);
			responseStructure.setStatusCode(HttpStatus.OK.value());
			responseStructure.setMessage("product found sucessfully");
			
			
			return new ResponseEntity(responseStructure, HttpStatus.OK);
		}
	}
		throw new ProdcutNotFoundException("data base is empty");
		
	}

	@Override
	public ResponseEntity<ResponseStructure<ProductResponseDTO>> deleteProduct(int prodcutid, int userid) {
		Optional<User> findById = repo.findById(userid);
		if(findById.get().getRole().getRole().equals("admin")){
			
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
		}
		
		
		throw new ProdcutNotFoundException("product not found"); 
		
	}

	@Override
	public ResponseEntity<ResponseStructure<ProductResponseDTO>> allProducts() {
		List<Product> findAll = productRepository.findAll();
		
		if(!findAll.isEmpty()) {
			List<ProductResponseDTO> allprodcuts = new ArrayList<>();
		
			for (Product product : findAll) {
				ProductResponseDTO response = new ProductResponseDTO();
				response.setProductName(product.getProductName());
				response.setProductPrice(product.getProductPrice());
				response.setProductID(product.getProductId());
				response.setProductQuantity(product.getProductQuantity());
				allprodcuts.add(response);
			}
			ResponseStructure<List<ProductResponseDTO>> responseStructure = new ResponseStructure<>();
			responseStructure.setData(allprodcuts);
			responseStructure.setStatusCode(HttpStatus.OK.value());
			responseStructure.setMessage("product found sucessfully");
			
			
			return new ResponseEntity(responseStructure, HttpStatus.OK);
		}
		throw new ProdcutNotFoundException("database is empty"); 
	}

	@Override
	public ResponseEntity<ResponseStructure<ProductResponseDTO>> deleteAllProducts(int[] id) {
		List<ProductResponseDTO> allprodcuts = new ArrayList<>();
		for (int i : id) {
			Optional<Product> findById = productRepository.findById(i);
			
			if(findById.isPresent()) {
				productRepository.delete(findById.get());
				
				ProductResponseDTO response = new ProductResponseDTO();
				response.setProductName(findById.get().getProductName());
				response.setProductPrice(findById.get().getProductPrice());
				response.setProductID(findById.get().getProductId());
				response.setProductQuantity(findById.get().getProductQuantity());
				allprodcuts.add(response);
				
			}
			else {
				throw new ProdcutNotFoundException("database is empty"); 
			}
			
			
		}
		ResponseStructure<List<ProductResponseDTO>> responseStructure = new ResponseStructure<>();
		responseStructure.setData(allprodcuts);
		responseStructure.setStatusCode(HttpStatus.OK.value());
		responseStructure.setMessage("product deleted sucessfully");
		return new ResponseEntity(responseStructure, HttpStatus.OK);
	}
	
	

}
