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
import com.jspiders.ombs.util.exception.UserNotFoundByEmailException;
import com.jspiders.ombs.util.exception.UserNotFoundByIdException;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
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
	
	private String mailStorage =null;
	
	@Autowired
	private ProductRepository productRepository;

	@Override
	public ResponseEntity<ResponseStructure<UserResponseDTO>> saveUser(UserRequestDTO userRequestDTO) throws MessagingException {
		String email=userRequestDTO.getUserEmail().toLowerCase();
		
		User user1 = userRepo.findByUserEmail(email);
		
		User_Role userRole = new User_Role();
		
		if (user1==null) {
			
//			System.out.println(userRole+" 1111111");
			userRole = user_Role_Repository.findByUserRoleName(userRequestDTO.getUserRole());
//			System.out.println(userRole+" 222222");
			
			if (userRole==null) {
			userRole = new User_Role();
//			System.out.println(userRequestDTO.getUserRole()+" 33333333");
			userRole.setUserRoleName(userRequestDTO.getUserRole());
//			System.out.println(userRole+" 44444444");
			user_Role_Repository.save(userRole);
			}
			
			User user = new User();
			
			user.setUserEmail(email);
			user.setUserPassword(userRequestDTO.getUserPassword());
			user.setUserFirstName(userRequestDTO.getUserFirstName());
			user.setUserLastName(userRequestDTO.getUserLastName());
			
			user.setUserRole(userRole); 
			
			IsDeleted s=IsDeleted.FALSE;
			
			user.setIsDeleted(s);
			
			userRepo.save(user);
			
			UserResponseDTO respone = new UserResponseDTO();
			respone.setUserId(user.getUserId());
			respone.setUserEmail(user.getUserEmail());
			respone.setUserFirstName(user.getUserFirstName());
			respone.setUserLastName(user.getUserLastName());
			
			ResponseStructure<UserResponseDTO> structure = new ResponseStructure<UserResponseDTO>();
			structure.setStatusCode(HttpStatus.CREATED.value());
			structure.setMessage("User data inserted Successfully!!!");
			structure.setData(respone);
			
//	===========================================================================	
			
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
			
			return new ResponseEntity<ResponseStructure<UserResponseDTO>>(structure,HttpStatus.CREATED);
		}
		else
			throw new EmailAlreadyExistException(email+" Email already exists!!!");
			
	}

	@Override
	public ResponseEntity<ResponseStructure<String>> userLogin(String userEmail, String userPassword) {
//		userEmail.toLowerCase();
		User user = userRepo.findByUserEmail(userEmail.toLowerCase());
//		System.out.println(user+" 11111");
		
		ResponseStructure<String> structure = new ResponseStructure<String>();
		
		/** here we are trying to fetch data from user object, but if user not found means it is null &
		 * from null we cannot fetch any data, it's an Runtime error  */
//		System.out.println(user.getUserId()+" "+user.getUserEmail()); 
		if (user!=null) {
			String pwd = user.getUserPassword();
			
//			System.out.println("Password:----"+pwd);
//			System.out.println(pwd+"=="+userPassword);
//			System.out.println(pwd==userPassword+"######");
			
			/** the above statement gives false because == compares address not String password, so we need to use equals() method */
			if (pwd.equals(userPassword)) {
//				System.out.println("Password:++++ "+pwd);
				String roleName = user.getUserRole().getUserRoleName();
//				System.out.println("User role: "+roleName);
				
				structure.setStatusCode(HttpStatus.FOUND.value());
				structure.setMessage("Login Successfull!!!");
				structure.setData("Welcome Home: "+roleName);
				return new ResponseEntity<ResponseStructure<String>>(structure,HttpStatus.FOUND);
			}
			else {
				structure = new ResponseStructure<String>();
				structure.setStatusCode(HttpStatus.NOT_FOUND.value());
				structure.setMessage("Password is Incorrect !!!");
				structure.setData("Login Failed !!!!");
				return new ResponseEntity<ResponseStructure<String>>(structure,HttpStatus.NOT_FOUND);
				/** OR throw new InvalidPasswordException("Incorrect Password!!!") */
			}
		}
		else {
			throw new UserNotFoundByEmailException(userEmail+" This doesnot exist, Please create Account!!!");
		}
		
	}

	@Override
	public ResponseEntity<ResponseStructure<UserResponseDTO>> userLogin2(UserRequestDTO userRequestDTO) {
		String userEmail = userRequestDTO.getUserEmail().toLowerCase();
		User user = userRepo.findByUserEmail(userEmail);
		return null;
	}

	private Map<String, String> emailStorage = new HashMap<>();
	@Override
	public ResponseEntity<String> changePassword(String email) throws MessagingException {
		User user = userRepo.findByUserEmail(email);
		if(user!=null) {
			String token = UUID.randomUUID().toString();
			emailStorage.put(token, email);
			
			mailStorage = email;
			
			MimeMessage mime = javaMailSender.createMimeMessage();
			MimeMessageHelper message = new MimeMessageHelper(mime,true);
			message.setTo(email);
			message.setSubject("Change password");
//			String link="http://localhost:8080/users/createNewPassword?password=Ljshj@sja12"; 
			String link = "http://localhost:8080/users/retrieve?token";
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
			String emailContent = emailStorage.get(token);
			System.out.println(emailContent+" xyshfs");
			
			return ResponseEntity.ok("Email sent with token: " + token);
		}
		
		throw new UserNotFoundByEmailException(email+" is invalid, Please enter valid Email!!!"); 
	}

	@Override
	public ResponseEntity<ResponseStructure<String>> enterIdToDeleteAccount(int userId) {
		Optional<User> user = userRepo.findById(userId);
		
		if (!user.isEmpty()) {
			User user1=user.get();
			IsDeleted s=IsDeleted.TRUE;
			user1.setIsDeleted(s);
			userRepo.save(user1);
			
			/** Here only we have deleted account based on id(means changed Enum isDeleted value from FALSE to TRUE */
			
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
		return null;
	}

	@Override
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

	@Override
	public ResponseEntity<ResponseStructure<String>> createNewPassword(String password) {
		String mail = mailStorage;
		System.out.println(mail+" 11111");
		User user = userRepo.findByUserEmail(mail);
		user.setUserPassword(password);
		userRepo.save(user);
		ResponseStructure<String> structure = new ResponseStructure<>();
		structure.setStatusCode(HttpStatus.OK.value());
		structure.setMessage("New Password created!!");
		structure.setData("Password changed successfully!!!");
//		mailStorage = null;
		System.out.println(mailStorage+" 22222");
		return new ResponseEntity<ResponseStructure<String>>(structure,HttpStatus.OK);
	}

//	---------------------
	@Override
	public ResponseEntity<String> retrieveEmail(String token) {
		String emailContent = emailStorage.get(token);
		System.out.println(emailContent+" 11111");
		String mail = mailStorage;
		System.out.println(mail+" aaaaaaa");
		User user = userRepo.findByUserEmail(mail);
		System.out.println(user);
		if (mail != null) {
            // Return the email content
			System.out.println(emailContent+" 2222");
            return ResponseEntity.ok(mail); 
        } else {
            return ResponseEntity.notFound().build();
        }
	}

	@Override
	public ResponseEntity<ResponseStructure<ProductResponseDTO>> saveProduct(ProductRequestDTO productRequestDTO) {
		String pName=productRequestDTO.getProdName();
		Product product = productRepository.findByProdName(pName);
		return null;
	}
	
	
}

