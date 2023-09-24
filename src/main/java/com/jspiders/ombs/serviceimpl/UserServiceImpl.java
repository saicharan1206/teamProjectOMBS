package com.jspiders.ombs.serviceimpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import com.jspiders.ombs.dto.DeleteByIdRequest;
import com.jspiders.ombs.dto.ForgotPasswordEmail;
import com.jspiders.ombs.dto.ForgotPasswordEmailResponse;
import com.jspiders.ombs.dto.LoginResponse;
import com.jspiders.ombs.dto.LoginVerification;
import com.jspiders.ombs.dto.PasswordResetRequest;
import com.jspiders.ombs.dto.ProductsRequest;
import com.jspiders.ombs.dto.ProductsResponse;
import com.jspiders.ombs.dto.UpdateEmail;
import com.jspiders.ombs.dto.UserRequestDTO;
import com.jspiders.ombs.dto.UserResponseDTO;
import com.jspiders.ombs.entity.IsDeleted;
import com.jspiders.ombs.entity.Products;
import com.jspiders.ombs.entity.User;
import com.jspiders.ombs.entity.User_Role;
import com.jspiders.ombs.repository.ProductsRepository;
import com.jspiders.ombs.repository.UserRepository;
import com.jspiders.ombs.repository.User_RoleRepository;
import com.jspiders.ombs.service.ProductsService;
import com.jspiders.ombs.service.UserService;
import com.jspiders.ombs.util.ResponseStructure;
import com.jspiders.ombs.util.exception.CreateNewPasswordExceptOldPassword;
import com.jspiders.ombs.util.exception.NoProductsPresentException;
import com.jspiders.ombs.util.exception.PasswordMissmatchException;
import com.jspiders.ombs.util.exception.ProductAlreadyExistsException;
import com.jspiders.ombs.util.exception.ProductNotFoundByIdException;
import com.jspiders.ombs.util.exception.UserAlreadyExistsException;
import com.jspiders.ombs.util.exception.UserNotFoundByEmailException;
import com.jspiders.ombs.util.exception.UserNotFoundByIdException;
import com.jspiders.ombs.util.exception.WrongPasswordException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class UserServiceImpl implements UserService, ProductsService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private User_RoleRepository user_RoleRepository;
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	@Autowired
	private ProductsRepository productsRepository;

	private String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public ResponseEntity<ResponseStructure<UserResponseDTO>> saveData (UserRequestDTO userRequestDTO) {

		User email = userRepository.findByUserEmail(userRequestDTO.getUserEmail());

		if (email == null) {
			User user = new User();

			user.setFirstName(userRequestDTO.getFirstName());
			user.setLastName(userRequestDTO.getLastName());
			user.setUserEmail(userRequestDTO.getUserEmail());
			user.setUserPassword(userRequestDTO.getUserPassword());
			user.setIsDeleted(IsDeleted.FALSE);

			if (user_RoleRepository.findByUserRole(userRequestDTO.getUser_Role().getUserRole()) != null)
			{
				User_Role user_Role = user_RoleRepository
						.save(user_RoleRepository.findByUserRole(userRequestDTO.getUser_Role().getUserRole()));

				user.setUser_Role(user_Role);
			}
			else
			{
				User_Role user_Role = new User_Role();

				user_Role.setUserRole(userRequestDTO.getUser_Role().getUserRole());

				user_Role = user_RoleRepository.save(user_Role);

				user.setUser_Role(user_Role);
			}

			User save = userRepository.save(user);

			UserResponseDTO responseDTO = new UserResponseDTO();

			responseDTO.setUserId(save.getUserId());
			responseDTO.setUserFirstName(save.getFirstName());
			responseDTO.setUserLastName(save.getLastName());
			responseDTO.setUserEmail(save.getUserEmail());
			responseDTO.setCreatedBy(save.getCreatedBy());
			responseDTO.setCreatedDate(save.getCreatedDate());
			responseDTO.setLastUpdatedBy(save.getLastUpdatedBy());
			responseDTO.setLastUpdatedDate(save.getLastUpdatedDate());
			responseDTO.setUserRole(save.getUser_Role());

			ResponseStructure<UserResponseDTO> structure = new ResponseStructure<UserResponseDTO>();

			structure.setData(responseDTO);
			structure.setMessage("Data saved successfully !!");
			structure.setStatusCode(HttpStatus.CREATED.value());

			SimpleMailMessage mailMessage = new SimpleMailMessage();
			mailMessage.setTo(save.getUserEmail());
			mailMessage.setSubject("Successfully Account Created");
			mailMessage.setText(save.getFirstName() + " " + save.getLastName() + " " + "You have created Account as a "
					+ save.getUser_Role().getUserRole() + " !! \n\nThanks & Regards" + "\nTeam OMBS + \nBangalore");
			mailMessage.setSentDate(new Date());

			javaMailSender.send(mailMessage);

			return new ResponseEntity<ResponseStructure<UserResponseDTO>>(structure, HttpStatus.CREATED);
		}
		else
		{
			throw new UserAlreadyExistsException("User is already exists !!");
		}
	}

	@Override
	public ResponseEntity<ResponseStructure<LoginResponse>> loginVer(LoginVerification loginVerification) {

		User findByUserEmail = userRepository.findByUserEmail(loginVerification.getUserEmail());

		if (findByUserEmail != null)
		{
			if (findByUserEmail.getUserPassword().equals(loginVerification.getUserPassword()))
			{
				String userRole = findByUserEmail.getUser_Role().getUserRole();

				LoginResponse loginResponse = new LoginResponse();
				loginResponse.setUserRole(userRole);

				ResponseStructure<LoginResponse> structure = new ResponseStructure<LoginResponse>();
				structure.setData(loginResponse);
				structure.setMessage("User Role is fetched successfully !!");
				structure.setStatusCode(HttpStatus.FOUND.value());

				return new ResponseEntity<ResponseStructure<LoginResponse>>(structure, HttpStatus.FOUND);
			}
			else
			{
				throw new WrongPasswordException("Wrong Password !!");
			}
		}
		else
		{
			throw new UserNotFoundByEmailException("User not found by this mail id !!");
		}
	}

	public ResponseEntity<ResponseStructure<ForgotPasswordEmailResponse>> forgotPassword(
			ForgotPasswordEmail forgotPasswordEmail) throws MessagingException {
		User byUserEmail = userRepository.findByUserEmail(forgotPasswordEmail.getUserEmail());

		if (byUserEmail != null) 
		{
			MimeMessage mimeMessage = javaMailSender.createMimeMessage();
			MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

			messageHelper.setTo(forgotPasswordEmail.getUserEmail());
			messageHelper.setSubject("Verification Code !!");
			String emailBody = "<html><h1>Please click below link to reset your password !!</h1>"
					+ "<br><a href='http://localhost:3000/PasswordReset'>http://localhost:3000/PasswordReset</a><br><br><p>Thanks & Regards</p><br><p>Team OMBS</p><br><p>Bangalore</p>";
			messageHelper.setText(emailBody, true);
			messageHelper.setSentDate(new Date());

			javaMailSender.send(mimeMessage);
			
			setEmail(forgotPasswordEmail.getUserEmail());

			ForgotPasswordEmailResponse emailResponse = new ForgotPasswordEmailResponse();
			emailResponse.setForgotPwdResponse("sent link to the given mail " + forgotPasswordEmail.getUserEmail());

			ResponseStructure<ForgotPasswordEmailResponse> structure = new ResponseStructure<ForgotPasswordEmailResponse>();
			structure.setStatusCode(HttpStatus.OK.value());
			structure.setMessage("Response sent to the respected email id !! " + forgotPasswordEmail.getUserEmail());
			structure.setData(emailResponse);

			return new ResponseEntity<ResponseStructure<ForgotPasswordEmailResponse>>(structure, HttpStatus.OK);
		} 
		else 
		{
			throw new UserNotFoundByEmailException("Invalid Email id !!");
		}
	}

	@Override
	public ResponseEntity<ResponseStructure<UserResponseDTO>> deleteData(DeleteByIdRequest deleteByIdRequest) {

		Optional<User> findById = userRepository.findById(deleteByIdRequest.getId());
		User user = findById.get();

		if (user != null)
		{
			if (user.getIsDeleted() == IsDeleted.FALSE)
			{
				user.setIsDeleted(IsDeleted.TRUE);
				user = userRepository.save(user);

				UserResponseDTO responseDTO = new UserResponseDTO();

				responseDTO.setUserId(user.getUserId());
				responseDTO.setUserFirstName(user.getFirstName());
				responseDTO.setUserLastName(user.getLastName());
				responseDTO.setUserEmail(user.getUserEmail());
				responseDTO.setCreatedBy(user.getCreatedBy());
				responseDTO.setCreatedDate(user.getCreatedDate());
				responseDTO.setLastUpdatedBy(user.getLastUpdatedBy());
				responseDTO.setLastUpdatedDate(user.getLastUpdatedDate());
				responseDTO.setUserRole(user.getUser_Role());

				ResponseStructure<UserResponseDTO> structure = new ResponseStructure<UserResponseDTO>();

				structure.setStatusCode(HttpStatus.OK.value());
				structure.setData(responseDTO);
				structure.setMessage("Data deleted successfully !!");

				return new ResponseEntity<ResponseStructure<UserResponseDTO>>(structure, HttpStatus.OK);
			} 
			else 
			{
				throw new UserNotFoundByIdException("User of this id not prensent !!");
			}
		} 
		else
		{
			throw new UserNotFoundByIdException("User not present in the database !!");
		}
	}

	@Override
	public ResponseEntity<ResponseStructure<UserResponseDTO>> updateEmail(UpdateEmail updateEmail) {

		Optional<User> findById = userRepository.findById(updateEmail.getUserId());
		User user = findById.get();

		User email = userRepository.findByUserEmail(updateEmail.getUserEmail());

		if (user != null)
		{
			if (user.getIsDeleted() == IsDeleted.FALSE) 
			{
				if (email == null)
				{
					user.setUserEmail(updateEmail.getUserEmail());

					user = userRepository.save(user);

					UserResponseDTO responseDTO = new UserResponseDTO();

					responseDTO.setUserId(user.getUserId());
					responseDTO.setUserFirstName(user.getFirstName());
					responseDTO.setUserLastName(user.getLastName());
					responseDTO.setUserEmail(user.getUserEmail());
					responseDTO.setCreatedBy(user.getCreatedBy());
					responseDTO.setCreatedDate(user.getCreatedDate());
					responseDTO.setLastUpdatedBy(user.getLastUpdatedBy());
					responseDTO.setLastUpdatedDate(user.getLastUpdatedDate());
					responseDTO.setUserRole(user.getUser_Role());

					ResponseStructure<UserResponseDTO> structure = new ResponseStructure<UserResponseDTO>();

					structure.setStatusCode(HttpStatus.OK.value());
					structure.setData(responseDTO);
					structure.setMessage("Data updated successfully !!");

					SimpleMailMessage mailMessage = new SimpleMailMessage();
					mailMessage.setTo(user.getUserEmail());
					mailMessage.setSubject("Successfully Email updated");
					mailMessage.setText(user.getFirstName() + " " + user.getLastName() + " "
							+ "You have updated Email id successfully !!  \n\nThanks & Regards"
							+ "\nTeam OMBS + \nBangalore");
					mailMessage.setSentDate(new Date());

					javaMailSender.send(mailMessage);

					return new ResponseEntity<ResponseStructure<UserResponseDTO>>(structure, HttpStatus.OK);
				} 
				else 
				{
					throw new UserAlreadyExistsException("User with email id is already exists !!");
				}
			} 
			else 
			{
				throw new UserNotFoundByIdException("User not present with this id !!");
			}
		}
		else
		{
			throw new UserNotFoundByIdException("User Not Present !!");
		}
	}

	@Override
	public ResponseEntity<ResponseStructure<UserResponseDTO>> fetchAllDetailsById(DeleteByIdRequest deleteByIdRequest) {

		User user = userRepository.findByUserId(deleteByIdRequest.getId());

		if (user != null && user.getIsDeleted() == IsDeleted.FALSE) 
		{
			UserResponseDTO responseDTO = new UserResponseDTO();

			responseDTO.setUserId(user.getUserId());
			responseDTO.setUserFirstName(user.getFirstName());
			responseDTO.setUserLastName(user.getLastName());
			responseDTO.setUserEmail(user.getUserEmail());
			responseDTO.setCreatedBy(user.getCreatedBy());
			responseDTO.setCreatedDate(user.getCreatedDate());
			responseDTO.setLastUpdatedBy(user.getLastUpdatedBy());
			responseDTO.setLastUpdatedDate(user.getLastUpdatedDate());
			responseDTO.setUserRole(user.getUser_Role());

			ResponseStructure<UserResponseDTO> structure = new ResponseStructure<UserResponseDTO>();
			structure.setData(responseDTO);
			structure.setStatusCode(HttpStatus.FOUND.value());
			structure.setMessage("Employee details fetched successfully !!!");

			return new ResponseEntity<ResponseStructure<UserResponseDTO>>(structure, HttpStatus.FOUND);
		} 
		else 
		{
			throw new UserNotFoundByIdException("No User found by this id !!");
		}
	}

	@Override
	public ResponseEntity<ResponseStructure<ForgotPasswordEmailResponse>> updatePassword (
			PasswordResetRequest passwordResetRequest) {

		User findByUserEmail = userRepository.findByUserEmail(getEmail());
		
		if (findByUserEmail != null)
		{
			if (passwordResetRequest.getUserPassword().equals(passwordResetRequest.getConfirmPassword()))
			{
				if (findByUserEmail.getUserPassword().equals(passwordResetRequest.getUserPassword()))
				{
					throw new CreateNewPasswordExceptOldPassword ("Use new password instead of old password !!");
				}
				else
				{
					findByUserEmail.setUserPassword(passwordResetRequest.getUserPassword());
					
					userRepository.save(findByUserEmail);
					
					ForgotPasswordEmailResponse emailResponse = new ForgotPasswordEmailResponse();
					emailResponse.setForgotPwdResponse("New password saved !!");
					
					ResponseStructure<ForgotPasswordEmailResponse> structure = new ResponseStructure<ForgotPasswordEmailResponse>();
					structure.setData(emailResponse);
					structure.setMessage("New Password saved successfully !!");
					structure.setStatusCode(HttpStatus.ACCEPTED.value());
					
					return new ResponseEntity<ResponseStructure<ForgotPasswordEmailResponse>> (structure, HttpStatus.ACCEPTED);
				}
			}
			else
			{
				throw new PasswordMissmatchException ("Password missmatch !!");
			}
		}
		else
		{
			throw new UserNotFoundByEmailException("Not Found !!");
		}
	}
	
	
	/*
	 * PRODUCTS SERVICE IMPLEMENTATION LOGIC
	 */
	
	@Override
	public ResponseEntity<ResponseStructure<ProductsResponse>> saveProduct (ProductsRequest productsRequest) {

		Products findByProductName = productsRepository.findByProductName(productsRequest.getProductName());
		
		if (findByProductName == null)
		{
			Products products = new Products();
			products.setProductName(productsRequest.getProductName());
			products.setProductQuantity(productsRequest.getProductQuantity());
			products.setProductPrice(productsRequest.getProductPrice());
			products.setIsDeleted(IsDeleted.FALSE);
			
			products = productsRepository.save(products);
			
			ProductsResponse productsResponse = new ProductsResponse();
			productsResponse.setProductId(products.getProductId());
			productsResponse.setProductName(products.getProductName());
			productsResponse.setProductQuantity(products.getProductQuantity());
			productsResponse.setProductPrice(products.getProductPrice());
			
			ResponseStructure<ProductsResponse> structure = new ResponseStructure<ProductsResponse>();
			
			structure.setData(productsResponse);
			structure.setMessage("Product saved successfully !!");
			structure.setStatusCode(HttpStatus.CREATED.value());
			
			return new ResponseEntity<ResponseStructure<ProductsResponse>> (structure, HttpStatus.CREATED);
		}
		else
		{
			throw new ProductAlreadyExistsException ("Product is already present !!");
		}
	}

	@Override
	public ResponseEntity<ResponseStructure<List<ProductsResponse>>> fetchAllProducts() {
		
		List<Products> findAll = productsRepository.findAll();
		
		if (findAll != null)
		{
			List<ProductsResponse> productsResponses = new ArrayList<ProductsResponse>();
			
			for (Products products : findAll)
			{
				if (products.getIsDeleted() == IsDeleted.FALSE)
				{
					ProductsResponse productsResponse = new ProductsResponse();
					
					productsResponse.setProductId(products.getProductId());
					productsResponse.setProductName(products.getProductName());
					productsResponse.setProductQuantity(products.getProductQuantity());
					productsResponse.setProductPrice(products.getProductPrice());
					
					productsResponses.add(productsResponse);
				}
			}
			
			ResponseStructure<List<ProductsResponse>> structure = new ResponseStructure<List<ProductsResponse>>();
			
			structure.setMessage("All products fetched successfully !!");
			structure.setStatusCode(HttpStatus.OK.value());
			structure.setData(productsResponses);
			
			return new ResponseEntity<ResponseStructure<List<ProductsResponse>>> (structure, HttpStatus.OK);
		}
		else
		{
			throw new NoProductsPresentException ("No products are present in the database !!");
		}
	}

	@Override
	public ResponseEntity<ResponseStructure<ProductsResponse>> updateProduct(ProductsRequest productsRequest, int productId) {
		
		Optional<Products> optional = productsRepository.findById(productId);
		Products products = optional.get();
		
		if (products != null)
		{
			products.setProductName(productsRequest.getProductName());
			products.setProductQuantity(productsRequest.getProductQuantity());
			products.setProductPrice(productsRequest.getProductPrice());
			
			products = productsRepository.save(products);
			
			ProductsResponse productsResponse = new ProductsResponse();
			
			productsResponse.setProductId(products.getProductId());
			productsResponse.setProductName(productsRequest.getProductName());
			productsResponse.setProductQuantity(products.getProductQuantity());
			productsResponse.setProductPrice(products.getProductPrice());
			
			ResponseStructure<ProductsResponse> structure = new ResponseStructure<ProductsResponse>();
			
			structure.setData(productsResponse);
			structure.setMessage("Product updated successfully !!");
			structure.setStatusCode(HttpStatus.CREATED.value());
			
			return new ResponseEntity<ResponseStructure<ProductsResponse>> (structure, HttpStatus.CREATED);
		}
		else
		{
			throw new ProductNotFoundByIdException ("Product Not Found by Id !!");
		}
		
	}

	@Override
	public ResponseEntity<ResponseStructure<ProductsResponse>> deleteProduct (int productId) {
		
		Optional<Products> optional = productsRepository.findById(productId);
		Products products = optional.get();
		
		if (products != null)
		{
			if (products.getIsDeleted() == IsDeleted.TRUE)
			{
				throw new ProductNotFoundByIdException("Product Not Found By Id !!");
			}
			else
			{
				products.setIsDeleted(IsDeleted.TRUE);
				
				products = productsRepository.save(products);
				
				ProductsResponse productsResponse = new ProductsResponse();
				
				productsResponse.setProductId(products.getProductId());
				productsResponse.setProductName(products.getProductName());
				productsResponse.setProductQuantity(products.getProductQuantity());
				productsResponse.setProductPrice(products.getProductPrice());
				
				ResponseStructure<ProductsResponse> structure = new ResponseStructure<ProductsResponse>();
				
				structure.setData(productsResponse);
				structure.setMessage("Product deleted successfully !!");
				structure.setStatusCode(HttpStatus.OK.value());
				
				return new ResponseEntity<ResponseStructure<ProductsResponse>> (structure, HttpStatus.OK);
			}
		}
		else
		{
			throw new ProductNotFoundByIdException("Product Not Found By Id !!");
		}
	}

	@Override
	public ResponseEntity<ResponseStructure<ProductsResponse>> fetchById (int productId) {
		
		Optional<Products> optional = productsRepository.findById(productId);
		Products products = optional.get();
		
		if (products != null)
		{
			if (products.getIsDeleted() == IsDeleted.FALSE)
			{
				ProductsResponse productsResponse = new ProductsResponse();
				
				productsResponse.setProductId(products.getProductId());
				productsResponse.setProductName(products.getProductName());
				productsResponse.setProductQuantity(products.getProductQuantity());
				productsResponse.setProductPrice(products.getProductPrice());
				
				ResponseStructure<ProductsResponse> structure = new ResponseStructure<ProductsResponse>();
				
				structure.setData(productsResponse);
				structure.setMessage("Data fetched successfully !!");
				structure.setStatusCode(HttpStatus.FOUND.value());
				
				return new ResponseEntity<ResponseStructure<ProductsResponse>> (structure, HttpStatus.FOUND);
			}
			else
			{
				throw new ProductNotFoundByIdException("Product Not Found By Id !!");
			}
		}
		else
		{
			throw new ProductNotFoundByIdException("Product Not Found By Id !!");
		}
		
	}

}
