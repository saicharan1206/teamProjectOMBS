package com.jspiders.ombs.serviceimpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.jspiders.ombs.dto.ForgotEmailResponse;
import com.jspiders.ombs.dto.UserRequestDTO;
import com.jspiders.ombs.repository.UserRepository;
import com.jspiders.ombs.repository.UserRoleRepository;
import com.jspiders.ombs.util.ResponseStructure;
import com.jspiders.ombs.util.exception.EmailAlreadyExistException;
import com.jspiders.ombs.util.exception.EmailDoesNotExistException;
import com.jspiders.ombs.util.exception.InvalidPasswordException;
import com.jspiders.ombs.util.exception.InvalidUserException;
import com.jspiders.ombs.util.exception.UserNotFoundByIdException;
import com.jspiders.ombs.util.exception.UserNotFoundException;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.validation.Valid;

import com.jspiders.ombs.dto.UserResponseDTO;
import com.jspiders.ombs.entity.IsDeleted;
import com.jspiders.ombs.entity.User;
import com.jspiders.ombs.entity.UserRole;
import com.jspiders.ombs.service.UserService;

public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepository repo;
	
	@Autowired
	private UserRoleRepository roleRepo;

	@Autowired
	private UserResponseDTO responseDTO;

	@Autowired
	private JavaMailSender javaMailSender;
	
	@Override
	public ResponseEntity<ResponseStructure<UserResponseDTO>> saveUser(UserRequestDTO userRequest) 
	{
		List<User> userList = repo.findAll();
	    User user = new User();
        
	    if(userList != null)
	    {
	    	for(User ele : userList)
	    	{
	    		if(ele.getUserEmail().equalsIgnoreCase(userRequest.getUserEmail()))
	    		{
	    			throw new EmailAlreadyExistException("Data is not saved");
	    		}
	    	}
	    }
	    
	    List<UserRole> role = roleRepo.getAllUserRoles();
	    UserRole userRole = new UserRole();
	    
	    boolean flag = true;
	    if(!role.isEmpty())
	    {
	    	for(UserRole roles : role)
	    	{
	    		if(roles.getUserRole().equalsIgnoreCase(userRequest.getUserRole()))
	    		{
	    			user.setUserRole1(roles);
	    			sendMail(userRequest);
	    			flag=false;
	    			break;
	    		}
	    	}
	    	
	    	if(flag == true)
	    	{
	    		userRole.setUserRole(userRequest.getUserRole());
	    		roleRepo.save(userRole);
	    		sendMail(userRequest);
	    	}
	    }
	    else
	    {
	    	userRole.setUserRole(userRequest.getUserRole());
	    	roleRepo.save(userRole);
	    	user.setUserRole1(userRole);
    		sendMail(userRequest);
        }
	    
	    user.setUserEmail(userRequest.getUserEmail().toLowerCase());
	    user.setUserPassword(userRequest.getUserPassword());
	    user.setUserFirstName(userRequest.getUserFirstName());
	    user.setUserLastName(userRequest.getUserLastName());
	    user.setIsDeleted(userRequest.getIsDeleted().FALSE);
	    
	    repo.save(user);
	    
	    UserResponseDTO response = new UserResponseDTO();
	    response.setUserEmail(userRequest.getUserEmail());
	    response.setUserFirstName(userRequest.getUserFirstName());
	    response.setUserLastName(userRequest.getUserLastName());
	    response.setUserRole(userRequest.getUserRole());
	    
	  	ResponseStructure<UserResponseDTO> structure=new ResponseStructure<UserResponseDTO>();
	  	structure.setData(response);
	  	structure.setStatusCode(HttpStatus.OK.value());
	  	structure.setMessage("User Data Saved Successfully!!!!!");
			
			return new ResponseEntity<ResponseStructure<UserResponseDTO>>(structure,
					   HttpStatus.OK);
	
	}
	
		
	@Override
	public ResponseEntity<String> sendMail(com.jspiders.ombs.dto.MessageData messageData) {
		// TODO Auto-generated method stub
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(messageData.getTo());
		message.setSubject(messageData.getSubject());
		message.setText(messageData.getText()
				+"\n\nThanks & Regards"
				+"\n"+messageData.getSenderName()
				+"\n"+messageData.getSenderAddress());
		message.setSentDate(new Date());
		
		javaMailSender.send(message);
		
		return new ResponseEntity<String>("Mail has been sent successfully", HttpStatus.OK);	}

	@Override
	public ResponseEntity<String> sendMimeMessage(com.jspiders.ombs.dto.MessageData messageData)
			throws MessagingException {
		MimeMessage mime = javaMailSender.createMimeMessage();
	    MimeMessageHelper message = new MimeMessageHelper(mime, true); //it is checking any multipart files are not
	    
	    message.setTo(messageData.getTo());
	    message.setText(messageData.getText());
	    
	    String emailBody = messageData.getText()
	    		+"<br><br><h4>Thanks & Regards</h4><br>"
	    		+"<h4>"+messageData.getSenderName()+"<br>"
	    		+messageData.getSenderAddress()+"</h4>"
	            +"<img src =\"https://jspiders.com/_nuxt/img/logo_jspiders.3b552d0.png\" width =\"250\">";
	    
	    message.setText(emailBody, true); //this is for html tags
	    message.setSentDate(new Date());
	    
	    javaMailSender.send(mime);
	    
		return new ResponseEntity<String> ("Mime message has been sent successfully",
				     HttpStatus.OK);	}


	@Override
	public ResponseEntity<ResponseStructure<UserResponseDTO>> userLogin(UserRequestDTO userRequest) {
		// TODO Auto-generated method stub
		String userEmail = userRequest.getUserEmail().toLowerCase();
		String password = userRequest.getUserPassword();
		List<User> users = new ArrayList<User>();
		users = repo.getAllUsers();
		boolean flag=false;
		
		UserResponseDTO response = new UserResponseDTO();
		ResponseStructure<UserResponseDTO> structure = new ResponseStructure<>();
		for(int i=0;i<users.size();i++)
		{
			if(users.get(i).getUserEmail().equals(userEmail))
            {
	           flag=true;
	           if(users.get(i).getUserPassword().equals(password))
	           {
	        	   response.setUserRole(users.get(i).getUserRole1().getUserRole());
	        	   structure.setStatusCode(HttpStatus.OK.value());
	        	   
	        	   structure.setData(response);
	        	   System.out.println(users.get(i).getUserRole1().getUserRole());
	        	   structure.setMessage("The " +users.get(i).getUserRole1().getUserRole()+
	        			        " Login successfully");
	           }
	           else
	           {
	        	   throw new InvalidPasswordException("Invalid Credentials");
	           }
            }
		}
		if(flag==false)
		{
			throw new EmailDoesNotExistException("Invalid Credentials");
		}
		return new ResponseEntity<ResponseStructure<UserResponseDTO>>(structure,HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ResponseStructure<UserResponseDTO>> getUser(String email, String password) {
		User user = repo.findByUserEmail(email);

		if (user != null && user.getUserPassword().equals(password)) {
			UserResponseDTO response = new UserResponseDTO();
			response.setUserId(user.getUserId());
			response.setCreatedBy(user.getCreatedBy());
			response.setCreatedDateTime(user.getCreatedDateTime());
			response.setUserEmail(user.getUserEmail());
			response.setUserRole(user.getUserRole1().getUserRole());
			response.setUpdatedDateTime(user.getUpdatedDateTime());
			response.setUpdatedBy(user.getUpdatedBy());
			response.setUserEmail(user.getUserEmail());

			ResponseStructure<UserResponseDTO> responseStructure = new ResponseStructure<>();
			responseStructure.setData(response);
			responseStructure.setMessage("found sucesfull");
			responseStructure.setStatusCode(HttpStatus.FOUND.value());
			return new ResponseEntity(responseStructure, HttpStatus.FOUND);
		}
		throw new UserNotFoundException("user not found");
	}
	
	@Override
	public ResponseEntity<ResponseStructure<ForgotEmailResponse>> getUserByEmail(String email) throws MessagingException {
		User user = repo.findByUserEmail(email);

		if (user != null) {
//			SimpleMailMessage message = new SimpleMailMessage();
//			message.setTo(user.getUserEmail());
//			message.setSubject("update password ");
//			message.setText("Please click below link to set new password " + "\n\nThanks & Regards" + "\n"
//					+ user.getCreatedBy() + "\n");
//			message.setSentDate(new Date());
//			javaMailSender.send(message);

			MimeMessage mimeMessage = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true);
			helper.setSentDate(new Date());
			//FileSystemResource file=new FileSystemResource(new File("C:\\Users\\Admin\\Pictures\\Saved Pictures\\lion.jpg"));
			
		//	helper.addAttachment("lion.jpg", file);
			helper.setTo(user.getUserEmail());
			helper.setSubject("update password ");
			 String emailBody = "Please click below link to set new password "+"<br>"+" <a href=\"http://localhost:3000/pwd/"+user.getUserEmail()+"\">click here</a>"+"<h4>Thanks & Regards<br>"
					 			+user.getCreatedBy()+"<br>"+"</h4>"
					 			+"<img src=\"https://www.jspiders.com/_nuxt/img/logo_jspiders.3b552d0.png\" width=\"250\" />";
			helper.setText(emailBody,true);
			helper.setSentDate(new Date());
			
			javaMailSender.send(mimeMessage);
			
			ForgotEmailResponse emailResponse = new ForgotEmailResponse();
			emailResponse.setUserId(user.getUserId());
			emailResponse.setCreatedBy(user.getCreatedBy());
			emailResponse.setUserEmail(user.getUserEmail());
			emailResponse.setCreatedDateTime(user.getCreatedDateTime());

			ResponseStructure<ForgotEmailResponse> responseStructure = new ResponseStructure<>();
			responseStructure.setData(emailResponse);
			responseStructure.setMessage("found sucesfull");
			responseStructure.setStatusCode(HttpStatus.OK.value());
			return new ResponseEntity(responseStructure, HttpStatus.OK);
		}
		throw new UserNotFoundException("user not found");
	}

	@Override
	public ResponseEntity<ResponseStructure<UserResponseDTO>> deleteByEmail(String email) {
		User user = repo.findByUserEmail(email);
		if (user != null && user.getUserRole1().getUserRole().equals("admin")) {
			user.setIsDeleted(IsDeleted.TRUE);
			repo.save(user);

			UserResponseDTO responseDTO = new UserResponseDTO();
			responseDTO.setUserId(user.getUserId());
			responseDTO.setUserEmail(user.getUserEmail());
			responseDTO.setCreatedDateTime(user.getCreatedDateTime());
			responseDTO.setCreatedBy(user.getCreatedBy());
			responseDTO.setUserFirstName(user.getUserFirstName());
			responseDTO.setUserLastName(user.getUserLastName());

			ResponseStructure<UserResponseDTO> structure = new ResponseStructure<>();
			structure.setData(responseDTO);
			structure.setMessage("user data deleted successfully!!!");
			structure.setStatusCode(HttpStatus.CREATED.value());
			return new ResponseEntity<ResponseStructure<UserResponseDTO>>(structure, HttpStatus.CREATED);
		}
		throw new InvalidUserException("Email not found  or User as no authoriztion to delete the data");

	}

	@Override
	public ResponseEntity<ResponseStructure<UserResponseDTO>> updateByEmail(@Valid UserRequestDTO request) {
		User user = repo.findByUserEmail(request.getUserEmail().toLowerCase());
		UserRole userRole = roleRepo.findByUserRole(request.getUserRole());
		if (user == null) {
			throw new UserNotFoundByIdException("EmailId is already exist!!");
		}
		user.setUserEmail(request.getUserEmail().toLowerCase());
		user.setUserPassword(request.getUserPassword());
		user.setUserFirstName(request.getUserFirstName());
		user.setUserLastName(request.getUserLastName());
		user.setUserRole1(userRole);
		user.setIsDeleted(IsDeleted.FALSE);
		user = repo.save(user);

		UserResponseDTO responseDTO = new UserResponseDTO();
		responseDTO.setUserId(user.getUserId());
		responseDTO.setUserEmail(user.getUserEmail());
		responseDTO.setCreatedDateTime(user.getCreatedDateTime());
		responseDTO.setCreatedBy(user.getCreatedBy());
		responseDTO.setUserFirstName(user.getUserFirstName());
		responseDTO.setUserLastName(user.getUserLastName());
		responseDTO.setUserRole(user.getUserRole1().getUserRole());

		ResponseStructure<UserResponseDTO> structure = new ResponseStructure<>();
		structure.setData(responseDTO);
		structure.setMessage("User data updated successfully!!!");
		structure.setStatusCode(HttpStatus.CREATED.value());
		return new ResponseEntity<ResponseStructure<UserResponseDTO>>(structure, HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<ResponseStructure<List<UserResponseDTO>>> findAllUser() {
		List<User> findAll = repo.findAll();
		if (!findAll.isEmpty()) {
			Vector<UserResponseDTO> usersDtos = new Vector();
			for (User user : findAll) {
				if (user.getUserRole1().getUserRole().equalsIgnoreCase("admin")) {
					UserResponseDTO response = new UserResponseDTO();
					response.setUserId(user.getUserId());
					response.setCreatedBy(user.getCreatedBy());
					response.setCreatedDateTime(user.getCreatedDateTime());
					response.setUserEmail(user.getUserEmail());
					response.setUserRole(user.getUserRole1().getUserRole());
					response.setUpdatedDateTime(user.getUpdatedDateTime());
					response.setUpdatedBy(user.getUpdatedBy());
					response.setUserEmail(user.getUserEmail());
					response.setUserFirstName(user.getUserFirstName());
					response.setUserLastName(user.getUserLastName());

					usersDtos.add(response);

				}
			}
			ResponseStructure<List<UserResponseDTO>> structure = new ResponseStructure<>();
			structure.setData(usersDtos);
			structure.setMessage("User data deleted successfully!!!");
			structure.setStatusCode(HttpStatus.CREATED.value());
			return new ResponseEntity<ResponseStructure<List<UserResponseDTO>>>(structure, HttpStatus.CREATED);
		}
		throw new UserNotFoundException("User not found!!");
	}

	@Override
	public ResponseEntity<ResponseStructure<UserResponseDTO>> updatePassword(String email, String pwd,
			String confirmPwd) {
		User user = repo.findUserByEmail(email.toLowerCase());
		if(user!=null )
		{
			if(pwd.equals(confirmPwd))
			{
			user.setUserPassword(confirmPwd);
			repo.save(user);
			}
			else 
				{
					throw new UserNotFoundByIdException("Password missmatch!!");
				}
			
			UserResponseDTO responseDTO = new UserResponseDTO();
			responseDTO.setUserId(user.getUserId());
			responseDTO.setUserEmail(user.getUserEmail());
			responseDTO.setCreatedDateTime(user.getCreatedDateTime());
			responseDTO.setCreatedBy(user.getCreatedBy());
			responseDTO.setUserFirstName(user.getUserFirstName());
			responseDTO.setUserLastName(user.getUserLastName());
			responseDTO.setUserRole(user.getUserRole1().getUserRole());
			
			ResponseStructure<UserResponseDTO> structure = new ResponseStructure<>();
			structure.setData(responseDTO);
			structure.setMessage("User password updated successfully!!!");
			structure.setStatusCode(HttpStatus.ACCEPTED.value());
			return new ResponseEntity<ResponseStructure<UserResponseDTO>>(structure, HttpStatus.ACCEPTED);
		}
		throw new UserNotFoundException("User not found!!");
	}

	@Override
	public ResponseEntity<ResponseStructure<String>> forgotPasswordValidation(String userEmail)
			throws MessagingException {
		// TODO Auto-generated method stub
		String email = repo.getUserByEmail(userEmail);
		MimeMessage mime = javaMailSender.createMimeMessage();
		MimeMessageHelper message = new MimeMessageHelper(mime,true);
		System.out.println(email);
		ResponseStructure<String> structure = new ResponseStructure<>();
		if(email!=null)
		{
			message.setTo(email);
			message.setSubject("Its a valid email");
			String messageBody = "<a href=\"file:///D:/WEB%20TECH/Spring/ResetPassword.html\">Link</a>";
			message.setText(messageBody,true);
			
			message.setSentDate(new Date());
			javaMailSender.send(mime);
			
			structure.setData(email);
			structure.setMessage("Got to your email to reset your password");
			structure.setStatusCode(HttpStatus.OK.value());
			return new ResponseEntity<ResponseStructure<String>>(structure,HttpStatus.OK);
		}
		else
		{
			throw new EmailDoesNotExistException("Invalid credentials");
		}
		
	}


	@Override
	public ResponseEntity<ResponseStructure<UserResponseDTO>> updateUser(UserRequestDTO userRequestDTO, int userId) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public ResponseEntity<ResponseStructure<UserResponseDTO>> deleteUser(int userId) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public ResponseEntity<ResponseStructure<List<UserResponseDTO>>> findAllUsers() {
		// TODO Auto-generated method stub
		return null;
	}

	public void sendMail(UserRequestDTO userRequest)
	{
		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setTo(userRequest.getUserEmail());
		mail.setSubject("Account creared successfully");
		mail.setText("Hello "+userRequest.getUserFirstName()+
				  " Your account created successfully as "+ userRequest.getUserRole());
		mail.setSentDate(new Date());
		javaMailSender.send(mail);
	}
}
