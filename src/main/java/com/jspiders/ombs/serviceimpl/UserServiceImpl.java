package com.jspiders.ombs.serviceimpl;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.jspiders.ombs.dto.UserRequestDTO;
import com.jspiders.ombs.dto.UserResponseDTO;
import com.jspiders.ombs.repository.UserRepository;
import com.jspiders.ombs.repository.UserRoleRepository;
import com.jspiders.ombs.service.UserService;

import com.jspiders.ombs.util.ResponseStructure;
import com.jspiders.ombs.util.exception.EmailAlreadyExistException;
import com.jspiders.ombs.util.exception.EmaildoesNotExistException;
import com.jspiders.ombs.util.exception.InvalidPasswordException;



import com.jspiders.ombs.entity.User;
import com.jspiders.ombs.entity.UserRole;

@Service
public class UserServiceImpl implements UserService {
	
	
	@Autowired
	private UserRepository repo;	
	
	@Autowired
	private UserRoleRepository repo1;
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	@Override
	public  ResponseEntity<ResponseStructure<UserResponseDTO>> userSave(UserRequestDTO userRequest)  {
	
		List<User> userList=repo.findAll();
		User user = new User();
		
		if(userList!=null)
		{
			for(User ele:userList)
			{
				if(ele.getUserEmail().equalsIgnoreCase(userRequest.getUserEmail()))
				{
					throw new EmailAlreadyExistException("DATA IS NOT SAVED ");
				}
			}
		}
		
		List<UserRole> role = repo.getAllUserRoles();
		UserRole userRole = new UserRole();
		
		
		boolean flag=true;
		if(!role.isEmpty())
		{	
			
			for(UserRole roles:role)
			{
				if(roles.getUserRole().equalsIgnoreCase(userRequest.getUserRole()))
				{
					user.setUserRole1(roles);
					sendMail(userRequest);
					flag=false;
					break;
				}			
				
			}
			
			if(flag==true)
			{
				userRole.setUserRole(userRequest.getUserRole());
				repo1.save(userRole);
				user.setUserRole1(userRole);
				sendMail(userRequest);
			}
	
		}
		else
		{
			userRole.setUserRole(userRequest.getUserRole());
			repo1.save(userRole);
			user.setUserRole1(userRole);
			sendMail(userRequest);
		}
			
			
		
		
		user.setUserEmail(userRequest.getUserEmail().toLowerCase());
		user.setUserPassword(userRequest.getUserPassword());
		user.setUserFirstName(userRequest.getUserFirstName());
		user.setUserLastName(userRequest.getUserLastName());
		
		//user.setUserRole(userRequest.getUserRole());
		repo.save(user);

		UserResponseDTO response = new UserResponseDTO();
		response.setUserEmail(userRequest.getUserEmail());
		response.setUserFirstName(userRequest.getUserFirstName());
		response.setUserRole(userRequest.getUserRole());
		response.setUserLastName(userRequest.getUserLastName());
		
		
		ResponseStructure<UserResponseDTO> responseStructure = new ResponseStructure<UserResponseDTO>();
		responseStructure.setData(response);
		responseStructure.setMessage("DATA SAVED SUCCESSFULL");
		responseStructure.setStatusCode(HttpStatus.OK.value());
		
		return new ResponseEntity<ResponseStructure<UserResponseDTO>>(responseStructure,HttpStatus.OK);
		
	}

	@Override
	public ResponseEntity<ResponseStructure<UserResponseDTO>> userLogin(UserRequestDTO userRequest) {
		
		String userEmail=userRequest.getUserEmail().toLowerCase();
		String password = userRequest.getUserPassword();
		List<User> users = new ArrayList<User>();
		users = repo1.getAllUserDetails();
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
					response.setMessage("USER AND PASSWORD EXIST");
					response.setUserRole(users.get(i).getUserRole1().getUserRole());
					structure.setStatusCode(HttpStatus.OK.value());
					
					structure.setData(response);
					System.out.println(users.get(i).getUserRole1().getUserRole());
					structure.setMessage("THE "+users.get(i).getUserRole1().getUserRole()+" LOGIN SUCCESSFULLYY");
					
				}
				else
				{
					response.setMessage("INVALID PASSWORD");
					throw new InvalidPasswordException("INVALID CREDENTIALS");
				}	
			}
		}	
		if(flag==false) {
		
		throw new EmaildoesNotExistException("INVALID CREDENTIALS");
		
		}
	return new ResponseEntity<ResponseStructure<UserResponseDTO>>(structure,HttpStatus.OK);
	}

//*********************************************EMAIL METHOD ***********************************************


public void sendMail(UserRequestDTO userRequest)
{
	
	SimpleMailMessage mail = new SimpleMailMessage();
	mail.setTo(userRequest.getUserEmail());
	mail.setSubject("ACCOUNT CREATED SUCCESSFULY");
	mail.setText("HELLO "+userRequest.getUserFirstName()+" YOUR ACCOUNT CREATED SUCCESSFULLY AS "+ userRequest.getUserRole());
	mail.setSentDate(new Date());
	javaMailSender.send(mail);
	
}


@Override
public ResponseEntity<ResponseStructure<String>> forgotPasswordValidation(String userEmail) {
	
	String email=repo.getUserByEmail(userEmail);
	System.out.println(email);
	ResponseStructure<String> structure = new ResponseStructure<>();
	if(email!=null)
	{
		
		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setTo(userEmail);
		mail.setSubject("ITS A VALID EMAIL");
		mail.setText("PLEASE FIND THE BELOW LINK TO RESET YOUR PASSWORD");
		mail.setSentDate(new Date());
		javaMailSender.send(mail);
		structure.setData(email);
		structure.setMessage("GOT TO YOUR EMAIL TO RESET YOR PASSWORD");
		structure.setStatusCode(HttpStatus.OK.value());

		return new ResponseEntity<ResponseStructure<String>>(structure,HttpStatus.OK);
	}
	else
	{
		throw new EmaildoesNotExistException("INVALID CREDENTIALS");		
		
	}
	
}

}





























