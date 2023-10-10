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
import com.jspiders.ombs.entity.Products;
import com.jspiders.ombs.entity.User;
import com.jspiders.ombs.repository.ProductsRepo;
import com.jspiders.ombs.repository.UserRepo;
import com.jspiders.ombs.service.ProductService;
import com.jspiders.ombs.util.ResponseStructure;
import com.jspiders.ombs.util.exception.ForbiddenOperationException;
import com.jspiders.ombs.util.exception.NotAuthorizedToAddProductException;
import com.jspiders.ombs.util.exception.ProductAlreadyPresentException;
import com.jspiders.ombs.util.exception.ProductNotFoundByIdException;
import com.jspiders.ombs.util.exception.UserNotFoundByIdException;

@Service
public class ProductServiceImpl implements ProductService{

	@Autowired
	UserRepo repo;
	
	@Autowired
	ProductsRepo productsRepo;
	
	@Override
	public ResponseEntity<ResponseStructure> addProduct(ProductRequestDTO productRequestDTO, Integer userId) {
		Products products = new Products();
		ProductResponseDTO productResponseDTO =new ProductResponseDTO();

		String productName;
		
		Optional<User> findById = repo.findById(userId);
		
		if(findById.isPresent()) {
			User user2 = findById.get();
			
			if(user2.getUserRole().getId()==1) {
				List<Products> findAllProducts = productsRepo.findAll();
					if(products!=null) {
						for (Products products2 : findAllProducts) {
							productName = products2.getProductName();
							if(productRequestDTO.getProductName().equalsIgnoreCase(productName)) {
								throw new ProductAlreadyPresentException("This product is already present in database");
							}
						}
					}
						products.setProductName(productRequestDTO.getProductName());
						products.setProductPrice(productRequestDTO.getProductPrice());
						products.setQuantity(productRequestDTO.getQuantity());
						products.setUser(user2);
					    Products save = productsRepo.save(products);
						
						productResponseDTO.setProductId(save.getProductId());
						productResponseDTO.setProductName(save.getProductName());
						productResponseDTO.setQuantity(save.getQuantity());
						productResponseDTO.setProductPrice(save.getProductPrice());
						
			}
			else {
				throw new NotAuthorizedToAddProductException("This Operation in forbidden");
			}
		}
		else {
			throw new UserNotFoundByIdException("User not found");
		}
				
		ResponseStructure responseStructure= new ResponseStructure();
		responseStructure.setData(productResponseDTO);
		responseStructure.setMessage("Product added to database");
		responseStructure.setStatusCode(HttpStatus.CREATED.value());
		
		return new ResponseEntity<ResponseStructure>(responseStructure,HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<ResponseStructure<List<ProductResponseDTO>>> fetchProducts() {
		List<Products> findAllProducts = productsRepo.findAll();
		
		List<ProductResponseDTO>productResponseDTOs = new ArrayList<ProductResponseDTO>();
		
		for (Products product : findAllProducts) {
			ProductResponseDTO productResponseDTO = new ProductResponseDTO();
			productResponseDTO.setProductId(product.getProductId());
			productResponseDTO.setProductName(product.getProductName());
			productResponseDTO.setQuantity(product.getQuantity());
			productResponseDTO.setProductPrice(product.getProductPrice());
			productResponseDTOs.add(productResponseDTO);
		}
		
		ResponseStructure<List<ProductResponseDTO>> responseStructure= new ResponseStructure<List<ProductResponseDTO>>();
		responseStructure.setData(productResponseDTOs);
		responseStructure.setMessage("List of Products Found");
		responseStructure.setStatusCode(HttpStatus.FOUND.value());
		
		return new ResponseEntity<ResponseStructure<List<ProductResponseDTO>>>(responseStructure,HttpStatus.FOUND);
	}

	@Override
	public ResponseEntity<ResponseStructure<String>> deleteProducts(Integer... prodIds) {
		for (Integer prodtId : prodIds) {
			Optional<Products> product = productsRepo.findById(prodtId);
			if(product.isEmpty()) {
				throw new ProductNotFoundByIdException("This product is not present");
			}
			
			productsRepo.delete(product.get());
		}
		
		ResponseStructure<String> responseStructure= new ResponseStructure<String>();
		responseStructure.setData("Deleted");
		responseStructure.setMessage("Products deleted");
		responseStructure.setStatusCode(HttpStatus.OK.value());
		
		return new ResponseEntity<ResponseStructure<String>>(responseStructure,HttpStatus.OK);
	}

}
