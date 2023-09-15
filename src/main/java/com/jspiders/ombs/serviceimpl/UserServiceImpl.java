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
import org.springframework.stereotype.Service;

import com.jspiders.ombs.dto.UserLoginDTO;
import com.jspiders.ombs.dto.UserRequestDTO;
import com.jspiders.ombs.dto.UserResponse;
import com.jspiders.ombs.dto.UserResponseDTO;
import com.jspiders.ombs.entity.IsDeleted;
import com.jspiders.ombs.entity.User;
import com.jspiders.ombs.entity.UserRole;
import com.jspiders.ombs.repository.UserRepository;
import com.jspiders.ombs.repository.UserRoleRepository;
import com.jspiders.ombs.service.UserService;
import com.jspiders.ombs.util.ResponseStructure;
import com.jspiders.ombs.util.exception.EmailAlreadyExistsException;
import com.jspiders.ombs.util.exception.EmailDoesnotExistsException;
import com.jspiders.ombs.util.exception.PasswordMismatchException;
import com.jspiders.ombs.util.exception.UserNotFoundByIdException;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
    UserRepository repo;
	
	@Autowired
	UserRoleRepository roles;
	
	@Autowired
	private JavaMailSender javaMailSender;
	

	
	
	public void assigningRole(UserRequestDTO request) {
		String email = request.getEmail().toLowerCase();
		String roleType = request.getUserRole().toLowerCase();
		User user = new User();
		user.setUserFirstName(request.getUserFirstName());
		user.setUserLastName(request.getUserLastName());		
		user.setEmail(email);
		user.setPassword(request.getPassword());
		user.setIsDeleted(IsDeleted.FALSE);
		System.out.println(IsDeleted.FALSE);
    	UserRole userRole = new UserRole();
    	userRole.setUserRole(roleType);
    	user.setUserRole(userRole);
    	roles.save(userRole);
    	repo.save(user);
    	sendMail(request);
    	
    	//********************* Sending mail ***********************
    	
	}
	@Override
	public ResponseEntity<ResponseStructure<UserResponseDTO>> saveUser(UserRequestDTO request) {
		String email = request.getEmail().toLowerCase();
		String roleType = request.getUserRole().toLowerCase();
		
		List<User> userlist = repo.findAll();
		List<UserRole> rolelist = roles.getAllUserRole();
            if(!userlist.isEmpty()) {
            	for(int i=0; i<=userlist.size()-1; i++) {
    				if(userlist.get(i).getEmail().equalsIgnoreCase(email)) {
    					throw new EmailAlreadyExistsException( email, " User cannot be saved" );	
    				}
    				
    			}	
            }
                boolean flag = true;
            	if(!rolelist.isEmpty()) {
            		for(int i=0; i<=rolelist.size()-1;i++) {
            			if(rolelist.get(i).getUserRole().equalsIgnoreCase(roleType)) {
            			
            				User user = new User();
            				user.setUserFirstName(request.getUserFirstName());
        					user.setUserLastName(request.getUserLastName());		
        					user.setEmail(email);
        					user.setPassword(request.getPassword());
        					user.setIsDeleted(IsDeleted.FALSE);
        	            	user.setUserRole(rolelist.get(i));
        	            	repo.save(user);
            				flag=false;
            				sendMail(request);
            			}	
            		}
            		
            		if(flag==true) {
            			assigningRole( request)	;
            			
            		}
         
	            }
            	else  {
            		assigningRole( request)	;
            		
            		}
            ResponseStructure<UserResponseDTO> structure = new ResponseStructure<>();
	            UserResponseDTO response = new UserResponseDTO();
				response.setMessage("Email saved sucessfully");
				response.setUserRole(roleType);
				
				
				structure.setStatusCode(HttpStatus.CREATED.value());
				structure.setMessage("Email saved sucessfully");
				structure.setData(response);

		return new ResponseEntity<ResponseStructure<UserResponseDTO>>(structure,HttpStatus.CREATED );
	}
	@Override
	public ResponseEntity<ResponseStructure<UserResponseDTO>> updateUser(UserRequestDTO request, int userId) {
		Optional<User> user = repo.findById(userId);
		ResponseStructure<UserResponseDTO> structure = new ResponseStructure<>();
		if(!user.isEmpty())
		{
			User user1 = new User();
			user1.setID(userId);
			user1.setUserFirstName(request.getUserFirstName());
			user1.setUserLastName(request.getUserLastName());
			user1.setEmail(request.getEmail());
			user1.setPassword(request.getPassword());
			repo.save(user1);
			UserResponseDTO response = new UserResponseDTO();
			response.setMessage("Updated sucessfully");
			structure.setStatusCode(HttpStatus.OK.value());
			structure.setMessage("Data updated successfully");
			structure.setData(response);
	
		}
		else {
			throw new UserNotFoundByIdException(userId,"User data cannot be updated");
		}
		
		return new ResponseEntity<ResponseStructure<UserResponseDTO>>(structure, HttpStatus.OK) ;
	}
	
	@Override
	public ResponseEntity<ResponseStructure<UserResponseDTO>> userLogin(UserLoginDTO login) {
		String email = login.getEmail().toLowerCase();
		String password = login.getPassword();
		List<User> userList = repo.findAll();
		UserResponseDTO response = new UserResponseDTO();
		ResponseStructure<UserResponseDTO> structure = new ResponseStructure<>();
		boolean flag = false;
		String role=null;
		for(int i=0; i<=userList.size()-1;i++) {
			if(userList.get(i).getEmail().equalsIgnoreCase(email)) {
				if(userList.get(i).getPassword().equals(password)) {
					role = userList.get(i).getUserRole().getUserRole();
					flag = true;
					
				}
				break;
			}
		}
		if(flag==true) {
			//response.setUserRole(password);
			response.setUserRole(role);
			response.setMessage("valid credentials");
			structure.setStatusCode(HttpStatus.OK.value());
			structure.setMessage("Login sucessfull");
			structure.setData(response);
		}
		else if(flag==false){
			throw new PasswordMismatchException("Login failed");
		}
		else {
			throw new EmailDoesnotExistsException("Email is not registered");	
		}
		return new ResponseEntity<ResponseStructure<UserResponseDTO>>(structure, HttpStatus.OK) ;
	}

	public void sendMail(UserRequestDTO request) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(request.getEmail());
		message.setSubject("Account created sucessfully");
		message.setText("Hello "+request.getUserFirstName()+ " Your account is created sucessfully as "+
		request.getUserRole());
		message.setSentDate(new Date());
		
		javaMailSender.send(message);
	}
	@Override
	public ResponseEntity<ResponseStructure<String>> passwordReset(String email) {
		String userEmail = repo.getUserEmailByEmail(email);
		System.out.println(userEmail);
		
		
		ResponseStructure<String> structure = new ResponseStructure<String>();
		structure.setStatusCode(HttpStatus.OK.value());
		structure.setMessage("Link sent to mail");
		structure.setData("Mail sent");
		if(userEmail!=null) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(email);
		message.setSubject("Reset password");
		message.setText(" Click on below link to reset the password");

		message.setSentDate(new Date());
		
		javaMailSender.send(message);	
		return new ResponseEntity<ResponseStructure<String>>(structure,HttpStatus.OK);
		}
		else
			throw new EmailDoesnotExistsException("Email is not registered");
	}
	//@Override
	public ResponseEntity<ResponseStructure<UserResponseDTO>> deleteAccount(String email, String password) {
		User user = repo.getUserByEmail(email);
	   
		if(user!=null) {
			if(user.getPassword().equals(password) ) {
		}
			user.setIsDeleted(IsDeleted.TRUE);
			repo.save(user);
		}
		else {
			throw new EmailDoesnotExistsException("Email is not registered");
			
		}
		return null;
	}
	@Override
	public ResponseEntity<ResponseStructure<UserResponseDTO>> deleteUser(int ID) {
		Optional<User> optional = repo.findById(ID);
		User user;
		if(optional.isPresent()) {
			user = optional.get();
			user.setIsDeleted(IsDeleted.TRUE);
			repo.save(user);
		}
		else {
			throw new EmailDoesnotExistsException("User not found");
			
		}
		UserResponseDTO response = new UserResponseDTO();
		response.setMessage("Account deleted ");
		response.setUserRole(user.getUserRole().getUserRole());
		
		ResponseStructure<UserResponseDTO> structure = new ResponseStructure<UserResponseDTO>();
		structure.setStatusCode(HttpStatus.OK.value());
		structure.setMessage("Account deleted successfully");
		structure.setData(response);
		return new ResponseEntity<ResponseStructure<UserResponseDTO>>(structure,HttpStatus.OK);
	}
	
	//****************  FIND ALL USERS *****************************8
	@Override
	public ResponseEntity<ResponseStructure<List<UserResponse>>> findAllUser() {
		List<User> list = repo.findAll();
		List<UserResponse> userlist = new ArrayList<UserResponse>();
		if(!list.isEmpty()) {
			
			for(User users : list ) {
				UserResponse response = new UserResponse();
				response.setUserFirstName(users.getUserFirstName());
				response.setUserLastName(users.getUserLastName());
				response.setUserRole(users.getUserRole().getUserRole());
				userlist.add(response);
			}
			
		}
		ResponseStructure<List<UserResponse>> structure = new ResponseStructure<List<UserResponse>>();
		structure.setData(userlist);
		structure.setStatusCode(HttpStatus.FOUND.value());;
		structure.setMessage("Below list of users are found");
		return new ResponseEntity<ResponseStructure<List<UserResponse>>> (structure, HttpStatus.FOUND);
	}
	
	
	
	
	
	
	
	
	

}
