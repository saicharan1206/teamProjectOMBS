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
import com.jspiders.ombs.util.exception.ProductNotFoundException;

@Service
public class ProductServiceImpl implements ProductService{

	@Autowired
	private ProductRepository repo;
	
	@Override
	public ResponseEntity<ResponseStructure<ProductResponseDTO>> addProduct(ProductRequestDTO requestDTO) {
		Product product = new Product();
		product.setProductName(requestDTO.getProductName());
		product.setProductPrice(requestDTO.getProductPrice());
		product.setProductQuantity(requestDTO.getProductQuantity());
		
		product = repo.save(product);
		ProductResponseDTO dto = new ProductResponseDTO();
		dto.setProductId(product.getProductId());
		dto.setProductName(product.getProductName());
		dto.setProductPrice(product.getProductPrice());
		
		ResponseStructure<ProductResponseDTO> responseStructure = new ResponseStructure<>();
		responseStructure.setData(dto);
		responseStructure.setMessage("Product added successfully!!");
		responseStructure.setStatusCode(HttpStatus.CREATED.value());
		return new ResponseEntity<ResponseStructure<ProductResponseDTO>>(responseStructure,HttpStatus.CREATED);
	}
	
	@Override
	public ResponseEntity<ResponseStructure<ProductResponseDTO>> updateProduct(ProductRequestDTO requestDTO,
			int productid) {
		// TODO Auto-generated method stub
		Optional<Product> findById = repo.findById(productid);
		if(findById.isPresent())
		{
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
			return new ResponseEntity<ResponseStructure<ProductResponseDTO>>(responseStructure,HttpStatus.CREATED);
		}
		
		throw new ProductNotFoundException("product not found!!");	
    }
	
	@Override
	public ResponseEntity<ResponseStructure<List<ProductResponseDTO>>> products() {
		List<Product> all = repo.findAll();
		List<ProductResponseDTO> list = new ArrayList<>();
		if(!all.isEmpty())
		{
			for (Product product : all) {
				ProductResponseDTO dto = new ProductResponseDTO();
				dto.setProductId(product.getProductId());
				dto.setProductName(product.getProductName());
				dto.setProductPrice(product.getProductPrice());
				list.add(dto);
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
	public ResponseEntity<ResponseStructure<ProductResponseDTO>> deleteProduct(int productid) {
		Optional<Product> findById = repo.findById(productid);
		if(findById.isPresent())
		{
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
			return new ResponseEntity<ResponseStructure<ProductResponseDTO>>(responseStructure,HttpStatus.ACCEPTED);
			
		}
		throw new ProductNotFoundException("product not found!!");
	}

}
