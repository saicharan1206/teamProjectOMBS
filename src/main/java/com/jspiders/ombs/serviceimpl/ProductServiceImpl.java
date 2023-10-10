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
import com.jspiders.ombs.entity.User;
import com.jspiders.ombs.repository.ProductRepo;
import com.jspiders.ombs.repository.UserRepo;
import com.jspiders.ombs.service.ProductService;
import com.jspiders.ombs.util.ResponseStructure;
import com.jspiders.ombs.util.exception.ForbiddenOperationException;
import com.jspiders.ombs.util.exception.ProductAlreadyExistException;
import com.jspiders.ombs.util.exception.ProductNotFoundByIdException;
import com.jspiders.ombs.util.exception.UserNotFoundByIdException;

@Service
public class ProductServiceImpl implements ProductService {

@Autowired
private UserRepo userRepo;
@Autowired
private ProductRepo productRepo;
	@Override
	public ResponseEntity<ResponseStructure<ProductResponseDTO>> addProduct(ProductRequestDTO productRequestDTO, Integer userId) {
	Optional<User> optional = userRepo.findById(userId);
	if (optional.isEmpty()) {	
		throw new UserNotFoundByIdException("User not found by this Id");	
	}
	User user = optional.get();
	
		if (user.getUserRole().getId()!=1) {
			throw new ForbiddenOperationException("Only Admins can add products");
		}
		List<Product> products = productRepo.findAll();
		if (products!=null) {
			for (Product product : products) {
				if (product.getProductName().equalsIgnoreCase(productRequestDTO.getProductName())) {
					throw new ProductAlreadyExistException("Product Already present in DB");
				}
			}
		}
		Product product = new Product();
		product.setProductName(productRequestDTO.getProductName());
		product.setProductPrice(productRequestDTO.getProductPrice());
		product.setProductQuantity(productRequestDTO.getProductQuantity());
		product.setUser(user);
		
		Product product2 = productRepo.save(product);
		ResponseStructure<ProductResponseDTO>responseStructure= new ResponseStructure<ProductResponseDTO>();
		ProductResponseDTO responseDTO = new ProductResponseDTO();
		responseDTO.setProductId(product2.getProductId());
		responseDTO.setProductName(product2.getProductName());
		responseDTO.setProductPrice(product2.getProductPrice());
		responseDTO.setProductQuantity(product2.getProductQuantity());
		responseStructure.setData(responseDTO);
		responseStructure.setMessage("Product Added to DB");
		responseStructure.setStatusCode(HttpStatus.CREATED.value());
		return  new ResponseEntity<ResponseStructure<ProductResponseDTO>>(responseStructure,HttpStatus.CREATED.OK);
	}
	@Override
	public ResponseEntity<ResponseStructure<List<ProductResponseDTO>>> findAllProducts() {
		List<Product> products = productRepo.findAll();
	
		List<ProductResponseDTO>productResponseDTOs = new ArrayList<ProductResponseDTO>();
		for (Product product : products) {
			ProductResponseDTO productResponseDTO= new  ProductResponseDTO();
			productResponseDTO.setProductId(product.getProductId());
			productResponseDTO.setProductName(product.getProductName());
			productResponseDTO.setProductPrice(product.getProductPrice());
			productResponseDTO.setProductQuantity(product.getProductQuantity());
			productResponseDTOs.add(productResponseDTO);
		}
		ResponseStructure<List<ProductResponseDTO>>responseStructure = new ResponseStructure<List<ProductResponseDTO>>();
		responseStructure.setData(productResponseDTOs);
		responseStructure.setMessage("All Products fetched successfully");
		responseStructure.setStatusCode(HttpStatus.FOUND.value());
		return new ResponseEntity<ResponseStructure<List<ProductResponseDTO>>>(responseStructure,HttpStatus.FOUND);
	}
	@Override
	public ResponseEntity<ResponseStructure<String>> deleteProducts(Integer... productIds) {
		 
		for (Integer productId : productIds) {
		  Optional<Product> product = productRepo.findById(productId);
		  if (product.isEmpty()) {
			throw new ProductNotFoundByIdException("Product with this Id Not Found");
		}
		  productRepo.delete(product.get());
		 
		}
		ResponseStructure<String>structure =new ResponseStructure<String>();
		structure.setData("products Deleted Successfully");
		structure.setMessage("Selected Products deleted Successfully");
		structure.setStatusCode(HttpStatus.OK.value());
		return new ResponseEntity<ResponseStructure<String>>(structure,HttpStatus.OK);
	}

}
