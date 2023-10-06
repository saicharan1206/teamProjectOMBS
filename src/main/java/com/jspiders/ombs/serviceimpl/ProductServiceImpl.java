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
import com.jspiders.ombs.repository.ProductRepository;
import com.jspiders.ombs.repository.UserRepository;
import com.jspiders.ombs.service.ProductService;
import com.jspiders.ombs.util.ResponseStructure;
import com.jspiders.ombs.util.exception.ProductNotFoundException;
import com.jspiders.ombs.util.exception.UserNotFoundException;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository repo;

	@Autowired
	private UserRepository repo1;

	@Override
	public ResponseEntity<ResponseStructure<ProductResponseDTO>> saveProduct(ProductRequestDTO requestDTO, int userid) {
		Optional<User> findById = repo1.findById(userid);
		boolean equals = findById.get().getRole().getUserRole().equals("admin");
		if (equals) {
			Product pro = new Product();
			pro.setProductName(requestDTO.getProductName());
			pro.setProductPrice(requestDTO.getProductPrice());
			pro.setProductQuantity(requestDTO.getProductQuantity());
			pro.setUser(findById.get());
			pro = repo.save(pro);
			
			ProductResponseDTO dto = new ProductResponseDTO();
			dto.setProductId(pro.getProductId());
			dto.setProductName(pro.getProductName());
			dto.setProductPrice(pro.getProductPrice());

			ResponseStructure<ProductResponseDTO> responseStructure = new ResponseStructure<>();
			responseStructure.setData(dto);
			responseStructure.setMessage("Product added successfully!!");
			responseStructure.setStatusCode(HttpStatus.CREATED.value());
			return new ResponseEntity<ResponseStructure<ProductResponseDTO>>(responseStructure, HttpStatus.CREATED);
		}
		throw new UserNotFoundException("user id not found!!");
	}

	@Override
	public ResponseEntity<ResponseStructure<ProductResponseDTO>> updateProduct(ProductRequestDTO requestDTO, int id) {
		Optional<Product> findById = repo.findById(id);
		if (findById.isPresent()) {
			Product product = findById.get();
			product.setProductPrice(requestDTO.getProductPrice());
			product.setProductQuantity(requestDTO.getProductQuantity());
			repo.save(product);

			ProductResponseDTO dto = new ProductResponseDTO();
			dto.setProductId(product.getProductId());
			dto.setProductName(product.getProductName());
			dto.setProductPrice(product.getProductPrice());

			ResponseStructure<ProductResponseDTO> responseStructure = new ResponseStructure<>();
			responseStructure.setData(dto);
			responseStructure.setMessage("Product added successfully!!");
			responseStructure.setStatusCode(HttpStatus.CREATED.value());
			return new ResponseEntity<ResponseStructure<ProductResponseDTO>>(responseStructure, HttpStatus.CREATED);
		}
		throw new ProductNotFoundException("product not found!!");
	}

	@Override
	public ResponseEntity<ResponseStructure<List<ProductResponseDTO>>> products(int userid) {
		List<Product> all = repo.findAll();
		List<ProductResponseDTO> list = new ArrayList<>();
		if (!all.isEmpty()) {
			for (Product product : all) {
				if(product.getUser().getUserId()==userid)
				{
				ProductResponseDTO dto = new ProductResponseDTO();
				dto.setProductId(product.getProductId());
				dto.setProductName(product.getProductName());
				dto.setProductPrice(product.getProductPrice());
				list.add(dto);
				}
			}
			ResponseStructure<List<ProductResponseDTO>> structure = new ResponseStructure<>();
			structure.setData(list);
			structure.setMessage("Product fetched successfully!!!");
			structure.setStatusCode(HttpStatus.OK.value());
			return new ResponseEntity<ResponseStructure<List<ProductResponseDTO>>>(structure, HttpStatus.OK);
		}
		throw new ProductNotFoundException("product not found!!");
	}

	@Override
	public ResponseEntity<ResponseStructure<ProductResponseDTO>> deleteProduct(int userid, int productid) {
		Optional<User> findBy = repo1.findById(userid);
		boolean equals = findBy.get().getRole().getUserRole().equals("admin");
		Optional<Product> findById = repo.findById(productid);
		if (equals) {
			if (findById.isPresent()) {
				Product product = findById.get();
				repo.delete(product);

				ProductResponseDTO dto = new ProductResponseDTO();
				dto.setProductId(product.getProductId());
				dto.setProductName(product.getProductName());
				dto.setProductPrice(product.getProductPrice());

				ResponseStructure<ProductResponseDTO> responseStructure = new ResponseStructure<>();
				responseStructure.setData(dto);
				responseStructure.setMessage("Product deleted successfully!!");
				responseStructure.setStatusCode(HttpStatus.ACCEPTED.value());
				return new ResponseEntity<ResponseStructure<ProductResponseDTO>>(responseStructure,
						HttpStatus.ACCEPTED);
			}
			throw new ProductNotFoundException("product not found!!");
		}
		throw new UserNotFoundException("user id not found!!");
	}

	@Override
	public ResponseEntity<ResponseStructure<ProductResponseDTO>> findallproducts() {
		List<Product> findallproducts = repo.findAll();
		
		List<ProductResponseDTO> responseDtos = new ArrayList<>();
		if(!findallproducts.isEmpty()) {
			for (Product product : findallproducts) {
				ProductResponseDTO dto = new ProductResponseDTO();
				dto.setProductId(product.getProductId());
				dto.setProductName(product.getProductName());
				dto.setProductPrice(product.getProductPrice());
				dto.setProductQuantity(product.getProductQuantity());
				
				responseDtos.add(dto);
			}
			
			ResponseStructure<List<ProductResponseDTO>> responseStructure = new ResponseStructure<>();
			responseStructure.setData(responseDtos);
			responseStructure.setMessage("Product deleted successfully!!");
			responseStructure.setStatusCode(HttpStatus.ACCEPTED.value());
			return new ResponseEntity(responseStructure, HttpStatus.FOUND);
		}
		throw new UserNotFoundException("user id not found!!");
	}

	@Override
	public ResponseEntity<ResponseStructure<ProductResponseDTO>> deleteAllProducts(int[] productid) {
		
		List<ProductResponseDTO> dtos = new ArrayList<>();
		for (int i : productid) {
			Optional<Product> findById = repo.findById(i);
			if(findById.isPresent()) {
				repo.delete(findById.get());
				ProductResponseDTO dto = new ProductResponseDTO();
				dto.setProductId(findById.get().getProductId());
				dto.setProductName(findById.get().getProductName());
				dto.setProductPrice(findById.get().getProductPrice());
				dto.setProductQuantity(findById.get().getProductQuantity());
				
				dtos.add(dto);
				
			}
			else {
				throw new UserNotFoundException("user id not found!!");
			}
		}
		
		ResponseStructure<List<ProductResponseDTO>> responseStructure = new ResponseStructure<>();
		responseStructure.setData(dtos);
		responseStructure.setMessage("Product deleted successfully!!");
		responseStructure.setStatusCode(HttpStatus.OK.value());
		return new ResponseEntity(responseStructure, HttpStatus.OK);
		
	}

	
}
