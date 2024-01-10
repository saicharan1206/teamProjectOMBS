package com.jspiders.ombs.serviceimpl;

import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.jspiders.ombs.dto.MessageData;
import com.jspiders.ombs.dto.ProductRequestDTO;
import com.jspiders.ombs.dto.ProductResponseDTO;
import com.jspiders.ombs.dto.UserRequestDTO;
import com.jspiders.ombs.dto.UserResponseDTO;
import com.jspiders.ombs.entity.IsDeleted;
import com.jspiders.ombs.entity.Product;
import com.jspiders.ombs.entity.User;
import com.jspiders.ombs.entity.User_Role;
import com.jspiders.ombs.repository.ProductRepository;
import com.jspiders.ombs.repository.UserRepository;
import com.jspiders.ombs.repository.User_Role_Repository;
import com.jspiders.ombs.service.UserService;
import com.jspiders.ombs.util.ResponseStructure;
import com.jspiders.ombs.util.exception.EmailAlreadyExistException;
import com.jspiders.ombs.util.exception.IncorrectPasswordException;
import com.jspiders.ombs.util.exception.ProductAlreadyExistException;
import com.jspiders.ombs.util.exception.ProductNotFoundByProductName;
import com.jspiders.ombs.util.exception.ProductNotFoundException;
import com.jspiders.ombs.util.exception.UserEmailNotFoundInCookieException;
import com.jspiders.ombs.util.exception.UserNotFoundByEmailException;
import com.jspiders.ombs.util.exception.UserNotFoundByIdException;
import com.jspiders.ombs.util.exception.YouAreNotAAdminException;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private User_Role_Repository user_Role_Repository;
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	@Autowired
	private ProductRepository productRepository;

	@Override
	public ResponseEntity<ResponseStructure<UserResponseDTO>> saveUser(UserRequestDTO userRequestDTO) throws MessagingException {
		String email=userRequestDTO.getUserEmail().toLowerCase();
		User user1 = userRepo.findByUserEmail(email);
		User_Role userRole = new User_Role();
		System.out.println("wwwwwwwwwwww");
		if (user1==null) {
			userRole = user_Role_Repository.findByUserRoleName(userRequestDTO.getUserRole());
			
			if (userRole==null) {
			userRole = new User_Role();
			userRole.setUserRoleName(userRequestDTO.getUserRole());
			user_Role_Repository.save(userRole);
			}
			
			User user = new User();
			
			user.setUserEmail(email);
			user.setUserPassword(userRequestDTO.getUserPassword());
			user.setUserFirstName(userRequestDTO.getUserFirstName());
			user.setUserLastName(userRequestDTO.getUserLastName());
			
			user.setUserRole(userRole); 
			
			IsDeleted f=IsDeleted.FALSE;
			
			user.setIsDeleted(f);
			
			userRepo.save(user);
			
			UserResponseDTO respone = new UserResponseDTO();
			respone.setUserId(user.getUserId());
			respone.setUserEmail(user.getUserEmail());
			respone.setUserFirstName(user.getUserFirstName());
			respone.setUserLastName(user.getUserLastName());
			
			ResponseStructure<UserResponseDTO> structure = new ResponseStructure<UserResponseDTO>();
			structure.setStatusCode(HttpStatus.OK.value());
			structure.setMessage("User data inserted Successfully!!!");
			structure.setData(respone);
			
//	 ========== Below code is to send Mail to user after signUp/account creation & data saving into database ====================	
			
			MimeMessage mime = javaMailSender.createMimeMessage();
			MimeMessageHelper message = new MimeMessageHelper(mime,true); // true for multipart file like image or any other files
			message.setTo(user.getUserEmail());
			message.setSubject("Account Registration Response");
			String emailBody = "Hello "+user.getUserFirstName()+" "+user.getUserLastName()+", your Account in OMB Applictaion"
								+ " is created as a Role: "+user.getUserRole().getUserRoleName()
								+"<br><br><h4>Thanks & Regards!</h4>"+
							  "<h4>"+"Admin of OMB"+"</h4>"+
							  "<h4>"+"JSpiders @Basavanagudi"+"</h4><br>"+
							  "<img src=\"https://cdn1.vectorstock.com/i/1000x1000/27/60/medical-pharmacy-logo-design-template-vector-35392760.jpg\" width =\"250\" > ";
			message.setText(emailBody,true); // true -> means it is not text message instead it is html document
			message.setSentDate(new Date());
			
			javaMailSender.send(mime);
			
//	===========================================================================	
			
			return new ResponseEntity<ResponseStructure<UserResponseDTO>>(structure,HttpStatus.OK);
		}
		else
			throw new EmailAlreadyExistException(email+" for this Email data did't saved!!!");
			
	}

	@Override /** This is for user login after signUp page using @RequestParam */
	public ResponseEntity<ResponseStructure<String>> userLogin(String userEmail, String userPassword) {
//		userEmail.toLowerCase();
		User user = userRepo.findByUserEmail(userEmail.toLowerCase());
		
		ResponseStructure<String> structure = new ResponseStructure<String>();
		
		/** below we are trying to fetch data from user object, but if user not found means it is null &
		 * from null we cannot fetch any data, it's an Runtime error & program gets terminates here only  */
//		System.out.println(user.getUserId()+" "+user.getUserEmail()); 
		if (user!=null) {
			String pwd = user.getUserPassword();
			
//			System.out.println(pwd+"=="+userPassword);
//			System.out.println(pwd==userPassword+"######");  // false
			
			/** the above statement gives false because == compares address not String password, so we need to use equals() method */
			if (pwd.equals(userPassword)) {
				String roleName = user.getUserRole().getUserRoleName();
				
				structure.setStatusCode(HttpStatus.FOUND.value());
				structure.setMessage("Login Successfull!!!");
				structure.setData("Welcome Home: "+roleName);
				return new ResponseEntity<ResponseStructure<String>>(structure,HttpStatus.FOUND);
			}
			else {
//				structure = new ResponseStructure<String>();
//				structure.setStatusCode(HttpStatus.NOT_FOUND.value());
//				structure.setMessage("Password is Incorrect !!!");
//				structure.setData("Login Failed !!!!");
//				return new ResponseEntity<ResponseStructure<String>>(structure,HttpStatus.NOT_FOUND);
				
				throw new IncorrectPasswordException("User Ligin failed!!!");
			}
		}
		else {
			throw new UserNotFoundByEmailException(userEmail+" for this eamil user account doesnot exist, Please create Account!!!");
		}
		
	}

	@Override /** This is the valid method for User login using @RequestBody(not @RequestParam) & passing userEmail through cookie  */
	public ResponseEntity<ResponseStructure<String>> userLogin2(UserRequestDTO userRequestDTO,
			HttpServletResponse response) {
		String userEmail1 = userRequestDTO.getUserEmail().toLowerCase();
		User user = userRepo.findByUserEmail(userEmail1);
		if (user!=null) {
			String pwd = user.getUserPassword();
			if(pwd.equals(userRequestDTO.getUserPassword())) {
				String roleName = user.getUserRole().getUserRoleName();
				
				ResponseStructure<String> structure = new ResponseStructure<>();
				structure.setStatusCode(HttpStatus.FOUND.value());
				structure.setMessage("Login Successfull!!!");
				structure.setData("Welcome Home: "+roleName);
				
				/** getting userEmail of user from userLogin to store into cookie, so that we can access this email in other places. 
				 * (using this email we can validate his role & allow this user to perform some specific task like 
				 * add users, delete users, add product, update products) */ 
				String userEmail = user.getUserEmail();
				/** creating cookie to store userEmail */
				Cookie userIdCookie = new Cookie("userEmail", userEmail);
				/** Set the cookie's path, domain, and other properties as needed 
				 * & Set the path to "/" to make it accessible throughout the application */
				userIdCookie.setPath("/"); 
				/** Sets the maximum age of the cookie value in seconds.A negative value means that the cookie is not stored persistently 
				 * and will be deleted when the Web browser exits. A zero value causes the cookie to be deleted.  */
				userIdCookie.setMaxAge(24*60*60); // this is => 24hr  // 10 years -> 3650*24*60*60
				/** Add the cookie to the HTTP response */
				response.addCookie(userIdCookie);
				
				/** Redirect or return a response as needed Or Redirect to the home page after login */
				return new ResponseEntity<ResponseStructure<String>>(structure,HttpStatus.FOUND);
			} else {
				throw new IncorrectPasswordException(pwd+" InCorrect Password, Login failed!!!");
			}
		}
		throw new UserNotFoundByEmailException(userEmail1+" for this eamil user account doesnot exist, Please create Account!!!");
	}

	@Override /** This method is to use cookie & perform some specific task(like update user name, etc)  */
	public ResponseEntity<ResponseStructure<String>> updateuserName(HttpServletRequest request, String name) {
		Cookie[] cookies = request.getCookies();
		String userEmail = null;
		if(cookies!=null) {
			for(Cookie cookie : cookies) {
				if("userEmail".equals(cookie.getName())) {
					userEmail = cookie.getValue(); 
					User user = userRepo.findByUserEmail(userEmail);
					user.setUserFirstName(name);
					userRepo.save(user);
					
					ResponseStructure<String> structure = new ResponseStructure<>();
					structure.setStatusCode(HttpStatus.FOUND.value());
					structure.setMessage("user name updated");
					structure.setData(userEmail+" found & user name updated successfully!!!");
					return new ResponseEntity<ResponseStructure<String>>(structure,HttpStatus.FOUND.OK);
				}
			}
		}
		throw new UserNotFoundByEmailException(userEmail+" this email not found in cookie, user name did't updated!!");
		
	}
	
	@Override
	public ResponseEntity<ResponseStructure<String>> enterIdToDeleteAccount(int userId) {
		Optional<User> user = userRepo.findById(userId);
		
		if (!user.isEmpty()) {
			User user1=user.get();
			IsDeleted t=IsDeleted.TRUE;
			user1.setIsDeleted(t);
			userRepo.save(user1);
			
			/** HereOnly we have deleted account based on id(means changed Enum isDeleted value from FALSE to TRUE */
			
			/** But here we can use cookies to store & send the userId to next method of enterPasswordToDelete(), so that 
			 * there only we can access userId & delete account instead of deleting here only */
			
			ResponseStructure<String> structure = new ResponseStructure<>();
			structure.setStatusCode(HttpStatus.FOUND.value());
			structure.setMessage("Your request for Account deletion processed");
			structure.setData("Your Account deleted successfully!!!");
			return new ResponseEntity<ResponseStructure<String>>(structure,HttpStatus.FOUND);
		}
		
		throw new UserNotFoundByIdException("User with userId: "+userId+" is not present, Please enter valid userId!!!");
	}

	@Override
	public ResponseEntity<ResponseStructure<String>> enterPasswordToDelete(String password) {
		// In above method only we have deleted account
		return null;
	}

	@Override /** 2nd way: to delete account */ 
	public ResponseEntity<ResponseStructure<String>> deleteUserAccount2(int id, String password) {
		Optional<User> user = userRepo.findById(id);
		if (!user.isEmpty()) {
			User user1=user.get();
			if (user1.getUserPassword().equals(password)) {
				user1.setIsDeleted(IsDeleted.TRUE);
				userRepo.save(user1);
				ResponseStructure<String> structure = new ResponseStructure<>();
				structure.setStatusCode(HttpStatus.FOUND.value());
				structure.setMessage("Your Request to Delete Account is Processed");
				structure.setData("Your Account successfully Deleted!!!!");
				return new ResponseEntity<ResponseStructure<String>>(structure,HttpStatus.FOUND);
			} else
				throw new IncorrectPasswordException(password+" is incorrect!!");
		}
		throw new UserNotFoundByIdException("User with userId: "+id+" is not present, Please enter valid userId!!!");
	}

	private Map<String, String> emailStorage = new HashMap<>();
	@Override
	public ResponseEntity<String> sendMailToChangePassword(String userEmail) throws MessagingException {
		User user = userRepo.findByUserEmail(userEmail);
		if(user!=null) {
			/** generating one random token which acts as key to store userEmail in HashMap, later in other methods
			 * by using this token we can get userEmail & by this we can get User object */
			String token = UUID.randomUUID().toString();
			/** storing userEmail in HashMap using randomly generated token */
			emailStorage.put(token, userEmail);
			
			MimeMessage mime = javaMailSender.createMimeMessage();
			MimeMessageHelper message = new MimeMessageHelper(mime,true);
			message.setTo(userEmail);
			message.setSubject("Change password");
//			String link="http://localhost:8080/users/createNewPassword?password=Ljshj@sja12"; 
			/** Here we are passing retrieveUserEmail() url/path along with token as a link, so that whenever user clicks that link, 
			 * retrieveUserEmail() gets called & method gets executed & it gives user object */
			String link = "http://localhost:8080/users/retrieve?token="+token;
//			String link="http://localhost:52330/AAAA.html";
			String emailBody = "Hello "+user.getUserFirstName()+" "+user.getUserLastName()+", you can change the Account Password using below link: "
//								+"<a href=\"http://localhost:8080/users\">"+"Click here</a> to visit our website."
								+"<a href="+link+">"+link+"</a> to visit our website."
								+"<br><br><h4>Thanks & Regards!</h4>"+ 
							  "<h4>"+"Admin of OMB"+"</h4>"+
							  "<h4>"+"JSpiders @Basavanagudi"+"</h4><br>"+
							  "<img src=\"https://cdn1.vectorstock.com/i/1000x1000/27/60/medical-pharmacy-logo-design-template-vector-35392760.jpg\" width =\"250\" > ";
			message.setText(emailBody,true);
			message.setSentDate(new Date());
			
			javaMailSender.send(mime);
//			return new ResponseEntity<String>("Mail sent successfully!!!",HttpStatus.OK);
			
//			String emailContent = emailStorage.get(token);
//			System.out.println(emailContent+" xyshfs");
			return ResponseEntity.ok("Email sent with token: " + token);
		}
		
		throw new UserNotFoundByEmailException(userEmail+" is invalid, Please enter valid Email!!!"); 
	}
	
	@Override
	public ResponseEntity<ResponseStructure<String>> createNewPassword(String userEmail,String newPassword) {
		User user = userRepo.findByUserEmail(userEmail);
		if(user!=null) {
			user.setUserPassword(newPassword);
			userRepo.save(user);
			ResponseStructure<String> structure = new ResponseStructure<>();
			structure.setStatusCode(HttpStatus.OK.value());
			structure.setMessage("New Password created!!");
			structure.setData("Password changed successfully!!!");
			return new ResponseEntity<ResponseStructure<String>>(structure,HttpStatus.OK);
		}
//		Set<String> se=emailStorage.keySet();
//		for(String s:se) {
//			System.out.println(s);
//			System.out.println(emailStorage.get(s)+" @@@@@@@@@@@@");
//		}
		throw new UserNotFoundByEmailException(userEmail+ " is invalid email, Please enter valid email!!!");
	}

//	---------------------
	@Override /** here we can return user object also after finding user based on token & userEmail */
	public ResponseEntity<String> retrieveEmail(String token) {
		String userEmail = emailStorage.get(token);
		User user = userRepo.findByUserEmail(userEmail);
		System.out.println(user);
		if (userEmail!= null) {
            // Return the email content
            return ResponseEntity.ok(userEmail); 
        } else {
            return ResponseEntity.notFound().build();
        }
	}

//	---------- Product Entity operations --------------------
	@Override  /** Save Product */
	public ResponseEntity<ResponseStructure<ProductResponseDTO>> saveProduct(HttpServletRequest request, ProductRequestDTO productRequestDTO) {
		Cookie[] cookies = request.getCookies();
		String userEmail = null;
		
		if(cookies!=null) {
			for (Cookie cookie:cookies) {
				if("userEmail".equals(cookie.getName())) {
					userEmail = cookie.getValue();
//					int i=Integer.parseInt(userEmail);					
					User user= userRepo.findByUserEmail(userEmail);
					String userRoleName = user.getUserRole().getUserRoleName();
					if(userRoleName.equals("Admin")) {
						String pName=productRequestDTO.getProductName();
						Product product = productRepository.findByProductName(pName);
						if(product==null) {
							product = new Product();
							product.setProductName(pName);
							product.setProductQty(productRequestDTO.getProductQty());
							product.setProductPrice(productRequestDTO.getProductPrice());
							product.setUser(user);
							productRepository.save(product);
							ProductResponseDTO response = new ProductResponseDTO();
							response.setProductId(product.getProductId());
							response.setProductName(product.getProductName());
							response.setProductQty(product.getProductQty());
							response.setProductPrice(product.getProductPrice());
							
							ResponseStructure<ProductResponseDTO> structure=new ResponseStructure<ProductResponseDTO>();
							structure.setStatusCode(HttpStatus.CREATED.value());
							structure.setMessage(productRequestDTO.getProductName()+": Product saved successfully");
							structure.setData(response);
							
							return new ResponseEntity<ResponseStructure<ProductResponseDTO>>(structure,HttpStatus.CREATED);
						} else {
							throw new ProductAlreadyExistException(productRequestDTO.getProductName()+": Product is not Saved!!!");
						}
					} else {
						throw new YouAreNotAAdminException("Not allowed to save product!!!");
					}
				} else {
					throw new UserEmailNotFoundInCookieException(userEmail+" this email not found in cookie, product did't saved!!");
				}
			}
		}
		
		throw new UserEmailNotFoundInCookieException("Cookie doesn't contain any user data, product did't saved!!");

	}

	@Override  /** Update Product quantity */
	public ResponseEntity<ResponseStructure<String>> updateProductQuantity(HttpServletRequest request, String productName, int productQty) {
		Cookie[] cookies = request.getCookies();
		String userEmail = null;
		if(cookies!=null) {
			for (Cookie cookie:cookies) {
				if("userEmail".equals(cookie.getName())) {
					userEmail = cookie.getValue();
					User user= userRepo.findByUserEmail(userEmail);
					String userRoleName = user.getUserRole().getUserRoleName();
					if(userRoleName.equals("Admin")) {
						Product product = productRepository.findByProductName(productName);
						if(product!=null) {
							product.setProductQty(productQty);
							productRepository.save(product);
							ResponseStructure<String> structure=new ResponseStructure<>();
							structure.setStatusCode(HttpStatus.OK.value());
							structure.setMessage("Product updated");
							structure.setData("Product Quantity updated successfully");
							return new ResponseEntity<ResponseStructure<String>>(structure,HttpStatus.OK);
						} else {
							throw new ProductNotFoundByProductName(productName+": This Product doesn't exist!!!");
						}
					} else {
						throw new YouAreNotAAdminException("Not allowed to update product!!!");
					}
				}else {
					throw new UserEmailNotFoundInCookieException(userEmail+" this email not found in cookie, product did't updated!!");
				}
			}
		}
		throw new UserEmailNotFoundInCookieException("Cookie doesn't contain any user data, product did't updated!!");
	}

	@Override
	public ResponseEntity<ResponseStructure<String>> deleteProduct(HttpServletRequest request,
			String productName) {
		Cookie[] cookies = request.getCookies();
		String userEmail=null;
		if(cookies!=null) {
			for (Cookie cookie:cookies) {
				if("userEmail".equals(cookie.getName())) {
					userEmail=cookie.getValue();
					User user=userRepo.findByUserEmail(userEmail);
					String userRoleName=user.getUserRole().getUserRoleName();
					if(userRoleName.equals("Admin")) {
						Product product = productRepository.findByProductName(productName);
						if(product!=null) {
							productRepository.delete(product);
							ResponseStructure<String> structure = new ResponseStructure<>();
							structure.setStatusCode(HttpStatus.FOUND.value());
							structure.setMessage("Product deleted!!!");
							structure.setData(productName+": Product deleted successfully!!!");
							return new ResponseEntity<ResponseStructure<String>>(structure,HttpStatus.FOUND);
						} else {
							throw new ProductNotFoundByProductName(productName+": This Product did't deleted!!!");
						}
					}  else {
						throw new YouAreNotAAdminException("Not allowed to delete product!!!");
					}
				} else {
					throw new UserEmailNotFoundInCookieException(userEmail+" this email not found in cookie, product did't deleted!!");
				}
			}
		}
		throw new UserEmailNotFoundInCookieException("Cookie is empty, product didn't deleted!!!");
	}

	@Override
	public ResponseEntity<ResponseStructure<List<ProductResponseDTO>>> getAllProducts() {
		List<Product> products= productRepository.findAll();
		if(products.isEmpty()==false) {
			List<ProductResponseDTO> productList = new ArrayList<>();
			for(Product product: products) {
				ProductResponseDTO response = new ProductResponseDTO();
				response.setProductId(product.getProductId());
				response.setProductName(product.getProductName());
				response.setProductPrice(product.getProductPrice());
				response.setProductQty(product.getProductQty());
				productList.add(response);
			}
			ResponseStructure<List<ProductResponseDTO>> structure = new ResponseStructure<>();
			structure.setStatusCode(HttpStatus.FOUND.value());
			structure.setMessage("Products found successfully!!!");
			structure.setData(productList);
			return new ResponseEntity<ResponseStructure<List<ProductResponseDTO>>>(structure,HttpStatus.FOUND);
		}
		throw new ProductNotFoundException("Products List is empty, Please add products!!!");
	}

	
	//---------------------
	@Override
	public ResponseEntity<ResponseStructure<String>> forgotPasswordValidation(String userEmail)
			throws MessagingException {

		String email = userRepo.getUserEmailByEmail(userEmail);
//		System.out.println(email+" eeeeeeeeeeee");
		MimeMessage mime = javaMailSender.createMimeMessage();
		MimeMessageHelper message = new MimeMessageHelper(mime, true);
		ResponseStructure<String> structure = new ResponseStructure<>();
		if (email != null) {
			
			message.setTo(email);
			message.setSubject("ITS A VALID EMAIL");
			String messageBody = "<a href='http://127.0.0.1:5502/SBProject/CreateNewPassword.html'>Link</a>";
			message.setText(messageBody, true);
			message.setSentDate(new Date());
			javaMailSender.send(mime);
			structure.setData(email);
			structure.setMessage("GOT TO YOUR EMAIL TO RESET YOR PASSWORD");		
			structure.setStatusCode(HttpStatus.OK.value());

			return new ResponseEntity<ResponseStructure<String>>(structure, HttpStatus.OK);
		} else {
			throw new UserNotFoundByEmailException(userEmail+" is invalid, Please enter valid Email!!!"); 

		}
	}
	
	@Override
	public ResponseEntity<ResponseStructure<String>> updatePassword(String newPassword,String userEmail) {
//		System.out.println(userEmail+" eeeeeeee");
		User user = userRepo.findByUserEmail(userEmail);
		if(user!=null) {
			if (newPassword.length()>=6) {
				System.out.println(newPassword.length()+" p111ppp11");
				user.setUserPassword(newPassword);
				userRepo.save(user);
				ResponseStructure<String> structure = new ResponseStructure<>();
				structure.setStatusCode(HttpStatus.OK.value());
				structure.setMessage("New Password created!!");
				structure.setData("Password changed successfully!!!");
				return new ResponseEntity<ResponseStructure<String>>(structure,HttpStatus.OK);
			} 
			else {
				System.out.println(newPassword.length()+" p111ppp222");
				throw new IncorrectPasswordException("Please enter valid password!!!");
			}
			
		}
		System.out.println(newPassword+" p111ppp222");
		throw new UserEmailNotFoundInCookieException(userEmail+" this email is not present in cookie!!!");
	}
	
}


				
				