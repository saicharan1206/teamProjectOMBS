package com.jspiders.ombs.serviceimpl;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.jspiders.ombs.dto.UserEmailResponseDTO;
import com.jspiders.ombs.dto.UserRequestDTO;
import com.jspiders.ombs.dto.UserResponseDTO;
import com.jspiders.ombs.entity.User;
import com.jspiders.ombs.entity.UserRole;
import com.jspiders.ombs.enums.IsDeleted;
import com.jspiders.ombs.repository.UserRepository;
import com.jspiders.ombs.repository.UserRoleRepository;
import com.jspiders.ombs.service.UserService;
import com.jspiders.ombs.util.ResponseStructure;
import com.jspiders.ombs.util.exception.InvalidUserException;
import com.jspiders.ombs.util.exception.UserAlreadyExists;
import com.jspiders.ombs.util.exception.UserNotFoundByEmailException;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
@Service
public class UserServiceImpl implements UserService
{

	@Autowired
	private UserRepository repo;
	@Autowired
	private UserRoleRepository roleRepo;
	@Autowired
	private UserResponseDTO responseDTO;
	@Autowired
	private UserEmailResponseDTO emailResponseDTO;
	@Autowired
	private JavaMailSender javaMailSender;
   

	
	@Override
	public ResponseStructure<UserResponseDTO> saveUser(UserRequestDTO userdata) throws MessagingException
	{
		  
		   String email = userdata.getUserEmail().toLowerCase();
		//   String password = userdata.getUserPassword();
		   User userExist = repo.findByUserEmail(email);
		  
		   UserRole userRole = roleRepo.findByUserRole(userdata.getRole());
		   UserRole role=null;
		   if(userRole==null)
		   {
			   role=new UserRole();
			   role.setUserRole(userdata.getRole());
			   roleRepo.save(role);
		   }
		   else
		   {
			   role=userRole;
		   }
		   
		   if(userExist!=null)
		   {
			   throw new UserAlreadyExists(email+"  User Already Exist ");
		   }
		   
		  
		    User user1=new User();
		    
		    user1.setUserFirstName(userdata.getUserFirstName());
		    user1.setUserLastName(userdata.getUserLastName());
			user1.setUserEmail(email);
			user1.setUserPassword(userdata.getUserPassword());
			user1.setRole(role);
			user1.setIsDeleted(IsDeleted.FALSE);
			
			
		    User user = repo.save(user1);
		
		   
			responseDTO.setUserId(user.getUserId());
			responseDTO.setUserFirstName(user.getUserFirstName());
			responseDTO.setUserLastName(user.getUserLastName());
			responseDTO.setUserEmail(user.getUserEmail());
			responseDTO.setCreatedBy(user.getCreatedBy());
			responseDTO.setCreatedDate(user.getCreatedDate());
			responseDTO.setUpdatedBy(user.getUpdatedBy());
			responseDTO.setUpdatedDate(user.getUpdatedDate());
			responseDTO.setRole(user.getRole().getUserRole());
			
			ResponseStructure<UserResponseDTO> response=new ResponseStructure<>();
			response.setData(responseDTO);
			response.setStatusCode(HttpStatus.CREATED.value());
			response.setMessage("User Data Saved Successfully!!!!!");
			
			MimeMessage mime=javaMailSender.createMimeMessage();
			MimeMessageHelper helper=new MimeMessageHelper(mime,true);
			helper.setTo(user.getUserEmail());
			helper.setSubject("Account Created Successfully");
			helper.setSentDate(new Date());
			String body="Account has been Created Successfully"
					+ "<br><br><h4>Thanks & Regards<br>"
					+ " \nUser Name :"+user.getUserFirstName()+" "+user.getUserLastName()+"<br>"
					+" \nUser Role :"+user.getRole().getUserRole()+"</h4>";
					
			helper.setText(body, true);
			
			
			javaMailSender.send(mime);
			
			return response;
	
	}


	@Override
	public ResponseStructure<UserResponseDTO> getByEmail(String email, String password) 
	{ 
		User user = repo.findByUserEmail(email);
		
		if(user!=null && password.equals(user.getUserPassword()))
		{
			responseDTO.setUserId(user.getUserId());
			responseDTO.setUserFirstName(user.getUserFirstName());
			responseDTO.setUserLastName(user.getUserLastName());
			responseDTO.setUserEmail(user.getUserEmail());
			responseDTO.setCreatedBy(user.getCreatedBy());
			responseDTO.setCreatedDate(user.getCreatedDate());
			responseDTO.setUpdatedBy(user.getUpdatedBy());
			responseDTO.setUpdatedDate(user.getUpdatedDate());
			responseDTO.setRole(user.getRole().getUserRole());
			
			ResponseStructure<UserResponseDTO> response=new ResponseStructure<>();
			response.setData(responseDTO);
			response.setStatusCode(HttpStatus.CREATED.value());
			response.setMessage("User Data Saved Successfully!!!!!");
			return response;
		}
	throw new UserNotFoundByEmailException("Please Enter Correct Email And Password");
	
	}


	@Override
	public ResponseStructure<UserEmailResponseDTO> sendEmail(String email) throws MessagingException
	{
		User user = repo.findByUserEmail(email);
		if(user!=null)
		{
			MimeMessage mime=javaMailSender.createMimeMessage();
			MimeMessageHelper helper=new MimeMessageHelper(mime,true);
			helper.setTo(user.getUserEmail());
			helper.setSubject("Create a New Password");
			helper.setSentDate(new Date());
			String body="Create New Password Link"
					+ "<br><br><h4>Thanks & Regards<br>"
					+ " \nUser Name :"+user.getUserFirstName()+" "+user.getUserLastName()+"<br>"
					+" \nUser Role :"+user.getRole().getUserRole()+"</h4>";
					
			helper.setText(body, true);
			
			javaMailSender.send(mime);
			
			emailResponseDTO.setUserId(user.getUserId());
			emailResponseDTO.setUserEmail(user.getUserEmail());
			ResponseStructure<UserEmailResponseDTO> response=new ResponseStructure<>();
			response.setData(emailResponseDTO);
			response.setStatusCode(HttpStatus.CREATED.value());
			response.setMessage("User Password Created  Successfully!!!!!");
			return response;

		}
		throw new UserNotFoundByEmailException("User Not Found");
  }


	@Override
	public ResponseStructure<String> deletebymail(String email)
	{
		User user = repo.findByUserEmail(email);
		if(user!=null && user.getRole().getUserRole().equals("Admin"))
		{
			user.setIsDeleted(IsDeleted.TRUE);
			repo.save(user);
			ResponseStructure<String> response=new ResponseStructure<>();
			response.setData(email+" Deleted Successfully");
			response.setMessage("User Deleted Successfully");
			response.setStatusCode(HttpStatus.OK.value());
			return response;
		
		}
		throw new UserNotFoundByEmailException("User is Not Authorized to Delete Account");
		
	}


	@Override
	public ResponseStructure<UserResponseDTO> update(UserRequestDTO userdata)
	{
		String email = userdata.getUserEmail().toLowerCase();
		User user = repo.findByUserEmail(email);
		UserRole role = roleRepo.findByUserRole(userdata.getRole());   
		if(user!=null)
		{
			 
			    
			    user.setUserFirstName(userdata.getUserFirstName());
			    user.setUserLastName(userdata.getUserLastName());
				user.setUserEmail(email);
				user.setUserPassword(userdata.getUserPassword());
				user.setRole(role);
				user.setIsDeleted(IsDeleted.FALSE);
				
			    User user1 = repo.save(user);
			    
			    responseDTO.setUserId(user1.getUserId());
				responseDTO.setUserFirstName(user1.getUserFirstName());
				responseDTO.setUserLastName(user1.getUserLastName());
				responseDTO.setUserEmail(user1.getUserEmail());
				responseDTO.setCreatedBy(user1.getCreatedBy());
				responseDTO.setCreatedDate(user1.getCreatedDate());
				responseDTO.setUpdatedBy(user1.getUpdatedBy());
				responseDTO.setUpdatedDate(user1.getUpdatedDate());
				responseDTO.setRole(user1.getRole().getUserRole());
				
				ResponseStructure<UserResponseDTO> response=new ResponseStructure<>();
				response.setData(responseDTO);
				response.setStatusCode(HttpStatus.CREATED.value());
				response.setMessage("User Data Saved Successfully!!!!!");
				
				return response;
			    
		}
		throw new UserNotFoundByEmailException("User Not Found ");
	}


	@Override
	public ResponseStructure<List<UserResponseDTO>> fetchAllAdmin() 
	{
		List<User> allUsers = repo.findAll();
		List<UserResponseDTO> allAdmins=new ArrayList<>();
		if(!allUsers.isEmpty())
		{
			for (User user : allUsers) 
	        {
				if(user.getRole().getUserRole().equals("Admin"))
				{
					UserResponseDTO responsedto = new UserResponseDTO();
					responsedto.setUserId(user.getUserId());
					responsedto.setUserFirstName(user.getUserFirstName());
					responsedto.setUserLastName(user.getUserLastName());
					responsedto.setUserEmail(user.getUserEmail());
					responsedto.setCreatedBy(user.getCreatedBy());
					responsedto.setCreatedDate(user.getCreatedDate());
					responsedto.setUpdatedBy(user.getUpdatedBy());
					responsedto.setUpdatedDate(user.getUpdatedDate());
					responsedto.setRole(user.getRole().getUserRole());
					allAdmins.add(responsedto);
				}
				
	        }
			
		
		ResponseStructure<List<UserResponseDTO>> response=new ResponseStructure<>();
		response.setData(allAdmins);
		response.setStatusCode(HttpStatus.FOUND.value());
		response.setMessage("Admin Data Retrived Successfully!!!!!");
		
		return response;
		
	  }
	    	throw new InvalidUserException("User Details Not Found");
	}

}
