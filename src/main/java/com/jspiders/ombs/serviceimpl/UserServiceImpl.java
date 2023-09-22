package com.jspiders.ombs.serviceimpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.jspiders.ombs.dto.ProductRequestDTO;
import com.jspiders.ombs.dto.ProductResponseDTO;
import com.jspiders.ombs.dto.UserRequestDTO;
import com.jspiders.ombs.dto.UserResponseDTO;
import com.jspiders.ombs.entity.Product;
import com.jspiders.ombs.entity.Role;
import com.jspiders.ombs.entity.User;
import com.jspiders.ombs.entity.User.IsDeleted;
import com.jspiders.ombs.repository.ProductRepository;
import com.jspiders.ombs.repository.UserRepository;
import com.jspiders.ombs.repository.UserRoleRepository;
import com.jspiders.ombs.service.UserService;
import com.jspiders.ombs.util.ResponseStructure;
import com.jspiders.ombs.util.exception.EmailAlreadyFoundException;
import com.jspiders.ombs.util.exception.IncorrectPasswordException;
import com.jspiders.ombs.util.exception.ProductAlreadyExistsException;
import com.jspiders.ombs.util.exception.ProductNotFoundException;
import com.jspiders.ombs.util.exception.UnAuthorizedUserException;
import com.jspiders.ombs.util.exception.UserNotFoundByEmailException;
import com.jspiders.ombs.util.exception.UserNotFoundByIdException;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private UserRoleRepository roleRepo;
	
	@Autowired
	private ProductRepository productRepo;
	
	@Autowired
	private JavaMailSender javaMailSender;

// TO STORE EMAIL TO CHANGE PASSWORD BY USING FORGOT PASSWORD AND CONFIRM NEW PASSWORD
//	String mail;
	
//	String userAdmin;
	
	@Override
	public ResponseEntity<ResponseStructure<UserResponseDTO>> saveUser(UserRequestDTO userRequest) throws MessagingException{
		
		String email = userRequest.getUserEmail().toLowerCase();
		String roleName = userRequest.getUserRole().toLowerCase();
		
		// User Entity
		User object = userRepo.findByUserEmail(email);
		
		Role userRole = new Role();
		
		if(object==null){
		
			// Role Entity
			userRole = roleRepo.findByRoleName(roleName); // Sales	
			
			if(userRole == null) {
				userRole = new Role();
				userRole.setRoleName(roleName);
				roleRepo.save(userRole);
			}
				
				User user = new User();
				user.setUserFirstName(userRequest.getUserFirstName());
				user.setUserLastName(userRequest.getUserLastName());
				user.setUserRole(userRole);
				user.setUserEmail(email);
				user.setUserPassword(userRequest.getUserPassword());	
				user.setIsDeleted(IsDeleted.FALSE);
				
		 /** Because of the EnableJpaAuditing & @EntityListeners(AuditingEntityListener.class) **/
				/** Created Date -- user.setCreatedDate(LocalDateTime.now());**/
				/** Updated Date -- user.setUpdatedDate(LocalDateTime.now());**/
				// UpdatedBy
				
//				Role role = roleRepo.save(userRole);
				User user2 = userRepo.save(user);
							
				UserResponseDTO response = new UserResponseDTO();
				response.setUserId(user2.getUserId());
				response.setUserFirstName(user2.getUserFirstName());
				response.getUserRole(userRole.getRoleName());
				
				ResponseStructure<UserResponseDTO> structure = new ResponseStructure<UserResponseDTO>();
				structure.setStatusCode(HttpStatus.CREATED.value());
				structure.setMessage("User Data inserted Successfully");
				structure.setData(response);
				
		
		/******* MAIL : ACCOUNT CREATED SUCCESSFULLY *********/
				
				MimeMessage mime = javaMailSender.createMimeMessage();
				MimeMessageHelper message = new MimeMessageHelper(mime, true);
				
				message.setTo(userRequest.getUserEmail());
				message.setSubject("Account Creation Response");

				String emailBody = "Hi "+user2.getUserFirstName()+", your account has been created successfully."					 
									
						+"<br><h4>Thanks & Regards</h4>"
						+"<h4>Admin Jspiders</h4>" 
						+"<h4>Jspiders Basavanagudi, Bangalore</h4>"
						+"<img src=\"https://media.istockphoto.com/id/1365830421/vector/hands-holding-house-symbol-with-heart-shape-thick-line-icon-with-pointed-corners-and-edges.jpg?s=1024x1024&w=is&k=20&c=SUp17dtO-N7qhENwnqxxEYD3SIFwfcu5-e9RCp4vlLw=\" width=\"120\">";

				message.setText(emailBody , true); 
				message.setSentDate(new Date());
				
				javaMailSender.send(mime);
				
				return new ResponseEntity<ResponseStructure<UserResponseDTO>>(structure, HttpStatus.CREATED);
			}
		throw new EmailAlreadyFoundException("This Email is not applicable");	
	}

	@Override
	public ResponseEntity<ResponseStructure<String>> logInUser(String email, String password) {

		User user = userRepo.findByUserEmail(email.toLowerCase());		
		ResponseStructure<String> structure = new ResponseStructure<String>();
		
		if(user != null) {
			
			/** If user email is Correct **/
			System.out.println(email + " " + password);
			System.out.println(user.getUserEmail() + " " + user.getUserPassword());
			
//			if(user.getUserRole().getRoleName().equals("admin")) {
//				userAdmin = user.getUserEmail();
//			}
			
			String pword = user.getUserPassword();
			
			if(password.equals(pword)) {
				
				/** password == pword (Compares address).. 
				 * 	password.equals(pword) (Compares Values)
				 */
				
				/** If user password is Correct **/
				System.out.println("Password is Correct");
				
				structure.setStatusCode(HttpStatus.FOUND.value());
				structure.setMessage("Login Successfully !!");
				structure.setData("Welcome Home, " +user.getUserRole().getRoleName());
				
				return new ResponseEntity<ResponseStructure<String>>(structure , HttpStatus.FOUND);
			}
			
			/** If user password is inCorrect **/
			System.out.println("Password is InCorrect");
			
			structure.setStatusCode(HttpStatus.NOT_FOUND.value());
			structure.setMessage("Sorry!! Please Enter Valid Password");
			structure.setData("No User Found for this Credentials");
			
			return new ResponseEntity<ResponseStructure<String>>(structure , HttpStatus.NOT_FOUND);
		}
		/** If user email is inCorrect **/
		throw new UserNotFoundByEmailException("Invalid Email !!");
	}
	
	@Override
	public ResponseEntity<String> changePassword(String email) throws MessagingException {
		
//		mail = email;
		User user = userRepo.findByUserEmail(email);
		
		if(user != null) {
			MimeMessage mime = javaMailSender.createMimeMessage();
			MimeMessageHelper message = new MimeMessageHelper(mime, true);
			
//			String link = "http://192.168.43.212:5500/";
		
			String link = "http://192.168.43.212:5500/confirmPassword.html";
			
			message.setTo(user.getUserEmail());
			message.setSubject("Link to change password");
			
			String emailBody = "Hi "+user.getUserFirstName()+", you can change the password using this below link.\n"	
								+ "<a href=\"" +link+"\">Click Here</a>\r\n"
			
								+"<br><h4>Thanks & Regards</h4>"
								+"<h4>Admin Jspiders</h4>" 
								+"<h4>Jspiders Basavanagudi, Bangalore</h4>"
								+"<img src=\"https://media.istockphoto.com/id/1365830421/vector/hands-holding-house-symbol-with-heart-shape-thick-line-icon-with-pointed-corners-and-edges.jpg?s=1024x1024&w=is&k=20&c=SUp17dtO-N7qhENwnqxxEYD3SIFwfcu5-e9RCp4vlLw=\" width=\"120\">";
				
			message.setText(emailBody, true);
			message.setSentDate(new Date());
			
			javaMailSender.send(mime);

			return new ResponseEntity<String> ("Mail has been sent, check your mail" , HttpStatus.OK);
		}
		
		throw new UserNotFoundByEmailException("Please, Enter Valid Email");
	}

	@Override
	public ResponseEntity<ResponseStructure<String>> confirmDeleteMyAccount(int id, String password) {
		
		Optional<User> user = userRepo.findById(id);
		
		if(user.isPresent()) {
			User u1 = user.get();
			if(u1.getUserPassword().equals(password)) {
				u1.setIsDeleted(IsDeleted.TRUE);
				userRepo.save(u1);
				
				ResponseStructure<String> structure = new ResponseStructure<String>();
				structure.setStatusCode(HttpStatus.OK.value());
				structure.setMessage("Your Account has been deleted Successfully!!");
				structure.setData("Data DELETED");	
				return new ResponseEntity<ResponseStructure<String>>(structure, HttpStatus.OK);
			}
			throw new IncorrectPasswordException("Please check your password !!");
		}
		throw new UserNotFoundByIdException("Invalid user Id");
	}	

	public ResponseEntity<ResponseStructure<String>> confirmNewPassword(int id, String newPassword){
//		 System.out.println(mail);
//		 User user = userRepo.findByUserEmail(mail);
//		 user.setUserPassword(newPassword);
//		 userRepo.save(user);
//		 mail = null;
		
		 Optional<User> userOptional = userRepo.findById(id);
		 
		 if(userOptional.isPresent()) {
			 User user = userOptional.get();
			 user.setUserPassword(newPassword);
			 userRepo.save(user);
			 
			 ResponseStructure<String> structure = new ResponseStructure<String>();
			 structure.setStatusCode(HttpStatus.OK.value());
		  	 structure.setMessage("New Password Validated");
		 	 structure.setData("PASSWORD changed Successfully !!");	
			 return new ResponseEntity<ResponseStructure<String>>(structure,HttpStatus.OK);	
		 }
		 throw new UserNotFoundByEmailException("Invalid User");
	}
	
	@Override
	public ResponseEntity<ResponseStructure<ProductResponseDTO>> addProduct(int userId, ProductRequestDTO prodRequest) {
		
		Optional<User> userObj = userRepo.findById(userId);
		
		if(userObj.isPresent()) {	
			User user = userObj.get();
			
			if(user.getUserRole().getRoleName().equals("admin")) {
				String prodCode = prodRequest.getProductCode();
				Product productIs = productRepo.findByProductCode(prodCode);
				
				if(productIs == null) {
					
					Product product = new Product();	
					product.setProductName(prodRequest.getProductName());
					product.setProductCode(prodCode);
					product.setProductPrize(prodRequest.getProductPrize());
					product.setProductQuantity(prodRequest.getProductQuantity());
					product.setUser(user);
					
					Product savedProd = productRepo.save(product);
					
					ProductResponseDTO response = new ProductResponseDTO();
					response.setProductCode(savedProd.getProductCode());
					response.setProductName(savedProd.getProductName());
					response.setProductQuantity(savedProd.getProductQuantity());
					
					ResponseStructure<ProductResponseDTO> structure = new ResponseStructure<ProductResponseDTO>();
					structure.setStatusCode(HttpStatus.CREATED.value());
					structure.setMessage("New Product Added");
					structure.setData(response);

					return new ResponseEntity<ResponseStructure<ProductResponseDTO>>(structure, HttpStatus.CREATED);
				}
				throw new ProductAlreadyExistsException("This Product cannot be inserted");
			}
			throw new UnAuthorizedUserException("Invalid Operation");
		}
		throw new UserNotFoundByIdException("No User Found");
	}
	
	@Override
	public ResponseEntity<ResponseStructure<String>> deleteProduct(int userId, int productId) {
		Optional<User> userObj = userRepo.findById(userId);
		
		if(userObj.isPresent()) {
			
			User user = userObj.get();
			
			if(user.getUserRole().getRoleName().equals("admin")) {
		
				Optional<Product> product = productRepo.findById(productId);
		
				if(product.isPresent()) {
				Product prod = product.get();
				productRepo.delete(prod);
				
				ResponseStructure<String> structure = new ResponseStructure<String>();
				structure.setStatusCode(HttpStatus.OK.value());
				structure.setMessage("Product DELETED");
				structure.setData(prod.getProductName()+" is removed from List");
				
				return new ResponseEntity<ResponseStructure<String>>(structure , HttpStatus.OK);
				}
				throw new ProductNotFoundException("Product Deletion is not Supported !!");
			}
			throw new UnAuthorizedUserException("Invalid Operation");
		}
		throw new UserNotFoundByIdException("No User Found");
	}
	
	@Override
	public ResponseEntity<ResponseStructure<ProductResponseDTO>> updateProduct(int userId,ProductRequestDTO prodRequest) {
		Optional<User> userObj = userRepo.findById(userId);
		if(userObj.isPresent()) {
			
			User user = userObj.get();
			
			if(user.getUserRole().getRoleName().equals("admin")) {
					Product product = productRepo.findByProductCode(prodRequest.getProductCode());
					System.out.println(product +" "+prodRequest.getProductCode());
						if(product != null) {
							product.setProductName(prodRequest.getProductName());
							product.setProductCode(prodRequest.getProductCode());
							product.setProductPrize(prodRequest.getProductPrize());
							product.setProductQuantity(prodRequest.getProductQuantity());
							product.setUser(user);
							Product savedProd = productRepo.save(product);
						
							ProductResponseDTO response = new ProductResponseDTO();
							response.setProductCode(savedProd.getProductCode());
							response.setProductName(savedProd.getProductName());
							response.setProductQuantity(savedProd.getProductQuantity());
							
							ResponseStructure<ProductResponseDTO> structure = new ResponseStructure<ProductResponseDTO>();
							structure.setStatusCode(HttpStatus.OK.value());
							structure.setMessage("Product Updated");
							structure.setData(response);
						
						return new ResponseEntity<ResponseStructure<ProductResponseDTO>>(structure , HttpStatus.OK);
					}
					throw new ProductNotFoundException("Product Updation is not Supported !!");
//				}
//				throw new UnAuthorizedUserException("Invalid Operation");
			}
			throw new UnAuthorizedUserException("Invalid Operation");
		}
		throw new UserNotFoundByIdException("No User Found");
	}

	
	@Override
	public ResponseEntity<ResponseStructure<List<ProductResponseDTO>>> productList() {
		
		List<Product> listOfProduct = productRepo.findAll();
		if(!listOfProduct.isEmpty()) {
			
			List<ProductResponseDTO> listOfResponse = new ArrayList<ProductResponseDTO>();
			
				for(Product product : listOfProduct) {
					ProductResponseDTO response = new ProductResponseDTO();
					response.setProductCode(product.getProductCode());
					response.setProductName(product.getProductName());
					response.setProductQuantity(product.getProductQuantity());
					listOfResponse.add(response);
				}
				ResponseStructure<List<ProductResponseDTO>> structure = new ResponseStructure<List<ProductResponseDTO>>();
				structure.setStatusCode(HttpStatus.FOUND.value());
				structure.setMessage("List of Products");
				structure.setData(listOfResponse);
				
				return new ResponseEntity<ResponseStructure<List<ProductResponseDTO>>>(structure , HttpStatus.FOUND);
			}
		throw new ProductNotFoundException("No Product Found");
	}	
}


