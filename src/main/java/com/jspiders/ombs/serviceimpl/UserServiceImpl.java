package com.jspiders.ombs.serviceimpl;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.hibernate.engine.spi.Resolution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.jspiders.ombs.dto.ForgotRequest;
import com.jspiders.ombs.dto.LoginRequest;
import com.jspiders.ombs.dto.LoginResponse;
import com.jspiders.ombs.dto.ProductRequest;
import com.jspiders.ombs.dto.ProductResponse;
import com.jspiders.ombs.dto.UserRequestDTO;
import com.jspiders.ombs.dto.UserResponseDTO;
import com.jspiders.ombs.entity.IsDeleted;
import com.jspiders.ombs.entity.Product;
import com.jspiders.ombs.entity.User;
import com.jspiders.ombs.entity.UserRole;
import com.jspiders.ombs.repository.ProductRepository;
import com.jspiders.ombs.repository.UserRepository;
import com.jspiders.ombs.repository.UserRoleRepo;
import com.jspiders.ombs.service.UserService;
import com.jspiders.ombs.util.ResponseStructure;
import com.jspiders.ombs.util.exception.ProductNotFoundException;
import com.jspiders.ombs.util.exception.UserAlreadyExistException;
import com.jspiders.ombs.util.exception.UserDoesNotExistException;
import com.jspiders.ombs.util.exception.UserNotFoundByIdException;
import com.jspiders.ombs.util.exception.WrongPasswordException;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;


@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepository repo;
	
	@Autowired
	private UserRoleRepo userRoleRepo;
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	@Autowired
	private ProductRepository productrepo;

	//*****Save the User*****//
	@Override
	public ResponseEntity<ResponseStructure<UserResponseDTO>> saveData(UserRequestDTO user) {
		User user2 = new User();
		if (repo.findByUserEmail(user.getUserEmail()) == null) {
			user2.setUserFirstName(user.getUserFirstName());
			user2.setUserLastName(user.getUserLastName());
			user2.setUserEmail(user.getUserEmail().toLowerCase());
			user2.setUserPassword(user.getUserPassword());
			user2.setIsDeleted(IsDeleted.FALSE);			
			String userRoleName = user.getUserRole().getUserRoleName();
			UserRole userRole = userRoleRepo.findByUserRoleName(userRoleName);
			
			if(userRole==null)
			{
				UserRole ur = new UserRole();
				ur.setUserRoleName(userRoleName);
				userRole = userRoleRepo.save(ur);
				user2.setUserRole(userRole);
			}
			else {
				user2.setUserRole(userRole);
			}
			
			//*******Send the mail for account creation*********//
			
			String userEmail = user.getUserEmail(); 
            SimpleMailMessage message = new SimpleMailMessage();
			message.setTo(userEmail);
			message.setSubject("Account Creation sucessfully");
			message.setText("Hai " + user.getUserFirstName() + ", your account is sucessfully created on " + userRoleName 
					+"\n\n Thanks & Regards"+"\n"+
					"Arpitha"+"\n"+
					"Basvanagudi Bangalore");
			
			javaMailSender.send(message);
			
			User user1 = repo.save(user2);

			UserResponseDTO response = new UserResponseDTO();
			response.setUserId(user1.getUserId());
			response.setUserFirstName(user1.getUserFirstName());
			response.setUserLastName(user1.getUserLastName());
			response.setUserEmail(user1.getUserEmail());
			response.setUserPassword(user1.getUserPassword());
			response.setUserRole(user1.getUserRole());
			response.setCreatedDate(user1.getCreatedDate());
			response.setCreatedBy(user1.getCreatedBy());
			response.setLastUpdatedBy(user1.getLastUpdatedBy());
			response.setLastUpdatedDate(user1.getLastUpdatedDate());

			ResponseStructure<UserResponseDTO> structure = new ResponseStructure<UserResponseDTO>();
			structure.setStatusCode(HttpStatus.CREATED.value());
			structure.setMessage("user data sucessfully");
			structure.setData(response);
			return new ResponseEntity<ResponseStructure<UserResponseDTO>>(structure, HttpStatus.CREATED);
		} 
		else 
		{
			throw new UserAlreadyExistException("user is already exists");
		}

	}

	//***** Login for user or admin*******//
	@Override
	public ResponseEntity<ResponseStructure<LoginResponse>> loginUser(LoginRequest login) 
	{
		User user = repo.findByUserEmail(login.getUserEmail());
		if(user!=null)
		{
			if (user.getUserPassword().equals(login.getUserPassword()))
			{
				String userRoleName = user.getUserRole().getUserRoleName();
			    if(userRoleName.equals("Admin"))
				{
			    	LoginResponse response = new LoginResponse();
			    	response.setUserRoleName(userRoleName);
			    	ResponseStructure<LoginResponse> structure = new ResponseStructure<LoginResponse>();
					structure.setStatusCode(HttpStatus.CREATED.value());
					structure.setMessage("Admin login sucessfully");
					structure.setData(response);
					return new ResponseEntity<ResponseStructure<LoginResponse>>(structure, HttpStatus.OK);
				}
				else
				{
			    	LoginResponse response = new LoginResponse();
			    	response.setUserRoleName(userRoleName);
			    	ResponseStructure<LoginResponse> structure = new ResponseStructure<LoginResponse>();
					structure.setStatusCode(HttpStatus.CREATED.value());
					structure.setMessage("User login sucessfully");
					structure.setData(response);
					return new ResponseEntity<ResponseStructure<LoginResponse>>(structure, HttpStatus.OK);
				}
			}
			else
			{
			   throw new WrongPasswordException ("Wrong Password !!");	
			}		
		}
		else
		{
			throw new UserDoesNotExistException("user does not exists");
		}
		
	}

	//*****forgot password****//
	@Override
	public ResponseEntity<String> forgotPassword(ForgotRequest forgot) throws MessagingException {
		   	String userEmail = forgot.getUserEmail(); 		   	
		   	MimeMessage mime  = javaMailSender.createMimeMessage();
			MimeMessageHelper message = new MimeMessageHelper(mime);
			message.setTo(userEmail);
			message.setSubject("Password reset");
			String emailBody = "Hai, please click the below link to reset password" + "<br>" + 
			"<a href=\'http://localhost:3000/Newpassword'>click here</a>"
					+ "<br><br><h4>Thanks & Regards<br>" + "</h4>" + "Arpitha";
			message.setText(emailBody,true);
			javaMailSender.send(mime);
		   	return new ResponseEntity<String>("Link sent sucessfully", HttpStatus.OK);
	}

	//*******update the user*************//
	@Override
	public ResponseEntity<ResponseStructure<UserResponseDTO>> updateUser(UserRequestDTO user, int userId) {
		Optional<User> optional = repo.findById(userId);
		if(optional.isPresent())
		{
			User user1 = new User();
			user1.setUserId(userId);
			user1.setUserFirstName(user.getUserFirstName());
			user1.setUserLastName(user.getUserLastName());
			user1.setUserEmail(user.getUserEmail());
			user1.setUserPassword(user.getUserPassword());
			
			user1 = repo.save(user1);
			UserResponseDTO response  = new UserResponseDTO();
			response.setUserId(user1.getUserId());
			response.setUserFirstName(user1.getUserFirstName());
			response.setUserLastName(user1.getUserLastName());
			response.setUserEmail(user1.getUserEmail());
			response.setUserPassword(user1.getUserPassword());
			
			ResponseStructure<UserResponseDTO> structure = new ResponseStructure<UserResponseDTO>();
			structure.setStatusCode(HttpStatus.OK.value());
			structure.setMessage("user data Update sucessfully");
			structure.setData(response);
			return new ResponseEntity<ResponseStructure<UserResponseDTO>>(structure,HttpStatus.OK); 
		}
		else
		{
			throw new UserNotFoundByIdException("user data does not exists");
		}
		
	}

	//***** delete account *****//
	@Override
	public ResponseEntity<ResponseStructure<LoginResponse>> deleteAccount(int userId) {
		Optional<User> optional = repo.findById(userId);
		if(optional.isPresent())
		{
			User user = optional.get();
			user.setIsDeleted(IsDeleted.TRUE);
			repo.save(user);
			
			LoginResponse response = new LoginResponse();
			response.setUserRoleName(user.getUserRole().getUserRoleName());
			
			ResponseStructure<LoginResponse> structure = new ResponseStructure<LoginResponse>();
			structure.setStatusCode(HttpStatus.OK.value());
			structure.setMessage("Account deleted sucessfully");
			structure.setData(response);
			
			return new ResponseEntity<ResponseStructure<LoginResponse>>(structure,HttpStatus.OK); 
			
		}
		else
		{
			throw new UserNotFoundByIdException("user data does not exists");
		}
	}
	
	@Override
	public ResponseEntity<ResponseStructure<String>> resetPassword(String userEmail, String newPassword, String confirmPassword) {
		String userEmail1 = userEmail;
		User user = repo.findByUserEmail(userEmail1);
		if(user!=null)
		{
			if(newPassword.equals(confirmPassword))
			{
			user.setUserPassword(newPassword);
			repo.save(user);
			ResponseStructure<String> structure = new ResponseStructure<String>();
			structure.setStatusCode(HttpStatus.OK.value());
			structure.setMessage("password reset sucessfuly");
			structure.setData("password reset sucessfully");
			return new ResponseEntity<ResponseStructure<String>>(structure,HttpStatus.FOUND);
			}
			else
			{
				throw new WrongPasswordException ("Wrong Password !!");
			}
		}
		else
		{
			throw new UserDoesNotExistException("user does not exists");
		}
	}

	@Override
	public ResponseEntity<ResponseStructure<ProductResponse>> saveProduct(ProductRequest product) {
		Product product1 = new Product();
		if(product!=null)
		{
			product1.setProductName(product.getProductName());
			product1.setProductQuantity(product.getProductQuantity());
			product1.setProductPrice(product.getProductPrice());
			Product product2 = productrepo.save(product1);
			
			ProductResponse response = new ProductResponse();
			response.setProductId(product2.getProductId());
			response.setProductName(product2.getProductName());
			response.setProductQuantity(product2.getProductQuantity());
			response.setProductPrice(product2.getProductPrice());
			
			ResponseStructure<ProductResponse> structure = new ResponseStructure<ProductResponse>();
			structure.setStatusCode(HttpStatus.CREATED.value());
			structure.setMessage("product add sucessfully");
			structure.setData(response);
			
			return new ResponseEntity<ResponseStructure<ProductResponse>>(structure,HttpStatus.CREATED);
			
		}
		else
		{
			throw new ProductNotFoundException("product does not exists");
		}
		
	}

	@Override
	public ResponseEntity<ResponseStructure<List<ProductResponse>>> findAllProducts() {
		List<Product> list = productrepo.findAll();
		List<ProductResponse> responseList = new ArrayList<>();
		for (Product p : list) {
			ProductResponse response = new ProductResponse();
			response.setProductId(p.getProductId());
			response.setProductName(p.getProductName());
			response.setProductQuantity(p.getProductQuantity());
			response.setProductPrice(p.getProductPrice());
			responseList.add(response);
		}

		ResponseStructure<List<ProductResponse>> structure = new ResponseStructure();
		structure.setStatusCode(HttpStatus.OK.value());
		structure.setMessage("Product details fetech sucessfully");
		structure.setData(responseList);
		return new ResponseEntity<ResponseStructure<List<ProductResponse>>>(structure,HttpStatus.FOUND);
	}

	@Override
	public ResponseEntity<ResponseStructure<ProductResponse>> updateProduct(ProductRequest product, int productId) {
		Optional<Product> optional = productrepo.findById(productId);
		Product product1 = optional.get();
		if (product1!=null) 
		{
			product1.setProductId(productId);
			product1.setProductName(product.getProductName());
			product1.setProductPrice(product.getProductPrice());
			product1.setProductQuantity(product.getProductQuantity());

		    product1 = productrepo.save(product1);

			ProductResponse response = new ProductResponse();
			response.setProductId(product1.getProductId());
			response.setProductName(product1.getProductName());
			response.setProductPrice(product1.getProductPrice());
			response.setProductQuantity(product1.getProductQuantity());

			ResponseStructure<ProductResponse> structure = new ResponseStructure<ProductResponse>();
			structure.setStatusCode(HttpStatus.CREATED.value());
			structure.setMessage("product data Update sucessfully");
			structure.setData(response);
			return new ResponseEntity<ResponseStructure<ProductResponse>>(structure, HttpStatus.CREATED);
		} 
		else 
		{
			return null;
		}
	}

	@Override
	public ResponseEntity<ResponseStructure<ProductResponse>> deleteProduct(int productId) {
		Optional<Product> optional = productrepo.findById(productId);
		Product product = optional.get();
		if(product!=null)
		{
		ProductResponse response = new ProductResponse();
		response.setProductId(productId);
		response.setProductName(product.getProductName());
		response.setProductPrice(product.getProductPrice());
		response.setProductQuantity(product.getProductQuantity());
	
		productrepo.deleteById(productId);
		ResponseStructure<ProductResponse> structure = new ResponseStructure<ProductResponse>();
		structure.setStatusCode(HttpStatus.OK.value());
		structure.setMessage("product data deleted sucessfully");
		structure.setData(response);
	
		return new ResponseEntity<ResponseStructure<ProductResponse>>(structure,HttpStatus.OK);
		}
		else
		{
			return null;
		}
	}

	@Override
	public ResponseEntity<ResponseStructure<ProductResponse>> findByIdProduct(int productId) {
		Optional<Product> optional = productrepo.findById(productId);
		if(optional.isPresent())
		{
            Product product = optional.get();
			ProductResponse response = new ProductResponse();
			response.setProductId(product.getProductId());
			response.setProductName(product.getProductName());
			response.setProductPrice(product.getProductPrice());
			response.setProductQuantity(product.getProductQuantity());
		
			ResponseStructure<ProductResponse> structure = new ResponseStructure<ProductResponse>();
			structure.setStatusCode(HttpStatus.OK.value());
			structure.setMessage("Student data feteched sucessfully");
			structure.setData(response);
			return new  ResponseEntity<ResponseStructure<ProductResponse>> (structure, HttpStatus.FOUND);
		}
		else {
		return null;
		}
	}
}
