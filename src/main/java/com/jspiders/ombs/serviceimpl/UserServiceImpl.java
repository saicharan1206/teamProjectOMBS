package com.jspiders.ombs.serviceimpl;
import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.jspiders.ombs.dto.MessageData;
import com.jspiders.ombs.dto.UserRequestDTO;
import com.jspiders.ombs.dto.UserResponseDTO;
import com.jspiders.ombs.entity.User;
import com.jspiders.ombs.entity.UserRole;
import com.jspiders.ombs.repository.UserRepository;
import com.jspiders.ombs.repository.UserRoleRepository;
import com.jspiders.ombs.service.UserService;
import com.jspiders.ombs.util.ResponseStructure;
import com.jspiders.ombs.util.exception.EmailAlreadyFoundException;
import com.jspiders.ombs.util.exception.UserNotFoundByEmail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepository repo;

	@Autowired
	private UserRoleRepository reporole;
	@Autowired
	private JavaMailSender javaMailSender;

	@Override
	public ResponseEntity<ResponseStructure<UserResponseDTO>> saveUser(UserRequestDTO userRequestDTO)throws MessagingException {

		String emailaddress = userRequestDTO.getEmailaddress().toLowerCase();
		String roleName = userRequestDTO.getRole().toLowerCase();
		User object = repo.findByEmailAddress(emailaddress);

		UserRole userRol = new UserRole();

		if (object == null) {
			userRol = reporole.findByRoleName(roleName);

			if (userRol == null) {
				userRol = new UserRole();
				userRol.setRoleName(roleName);
				reporole.save(userRol);
			}
			User user = new User();
			user.setUserFirstName(userRequestDTO.getUserFirstName());
			user.setUserLastName(userRequestDTO.getUserLastName());
			user.setUserRole(userRol);
			user.setEmailAddress(userRequestDTO.getEmailaddress().toLowerCase());
			user.setPassword(userRequestDTO.getPassword());

			UserRole role = reporole.save(userRol);
			User user2 = repo.save(user);

			UserResponseDTO response = new UserResponseDTO();
			response.setUserId(user2.getUserId());
			response.setUserFirstName(user2.getUserFirstName());
			response.setUserRole(role.getRoleName());


			ResponseStructure<UserResponseDTO> structure = new ResponseStructure<UserResponseDTO>();
			structure.setStatusCode(HttpStatus.CREATED.value());
			structure.setMessage("User Data Inserted Successfully");
			structure.setData(response);
			
			/*****************Mail : Account Created Successfully ************/
			
			MimeMessage mime=javaMailSender.createMimeMessage();
			MimeMessageHelper messageHelper=new MimeMessageHelper(mime,true);
			
			messageHelper.setTo(userRequestDTO.getEmailaddress());
			messageHelper.setSubject("Acount Creation Response");
			
			String emailBody="Hi,"+user2.getUserFirstName()+",Your Account has been created successfully."
					+"<br><br><h4>Thanks & Regard</h4><br>"
					+"<h4>Jspider</h4><br>"
					+"<h4>http</h4>";
			messageHelper.setText(emailBody,true);
			messageHelper.setSentDate(new Date());
			
			javaMailSender.send(mime);
			

			return new ResponseEntity<ResponseStructure<UserResponseDTO>>(structure, HttpStatus.CREATED);
		}
		throw new EmailAlreadyFoundException("This Email is Not Applicable");
	}

	@Override
	public ResponseEntity<ResponseStructure<String>> loginUser(String emailaddress, String password) 
	{
		
		User user=repo.findByEmailAddress(emailaddress);
		ResponseStructure<String> structure=new ResponseStructure<String>();
		
		if(user!=null) 
		{	
			System.out.println(emailaddress+" "+ password);
			System.out.println(user.getEmailAddress()+" "+user.getPassword());
			
			String pass=user.getPassword();
			
			if(password.equals(pass)) 
			{	
				System.out.println("Password is Correct");
				
				structure.setStatusCode(HttpStatus.FOUND.value());
				structure.setMessage("Login Successfully");
				structure.setData("Welcome Home,"+user.getUserRole().getRoleName());
				
				return new ResponseEntity<ResponseStructure<String>>(structure,HttpStatus.FOUND);
			}
			System.out.println("Password is Incorrect");
			structure.setStatusCode(HttpStatus.NOT_FOUND.value());
			structure.setMessage("Sorry!! Re-enter");
			structure.setData("No User Found for this credentials");
		
			return new ResponseEntity<ResponseStructure<String>>(structure,HttpStatus.NOT_FOUND);	
		}
		throw new UserNotFoundByEmail ("Invalid Email!!");
	}
	@Override
	public ResponseEntity<String> sendMail(MessageData messageData) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(messageData.getTo());
		message.setSubject(messageData.getSubject());
		message.setText(messageData.getText()
				+"\n\nThanks & Regards"
				+"\n"+messageData.getSenderName()
				+"\n"+messageData.getSenderAddress()
				);
		
		message.setSentDate(new Date());

		javaMailSender.send(message);
		return new ResponseEntity<String>("Mail send Successfully!!!!!", HttpStatus.OK);
	}
	@Override
	public ResponseEntity<String> myMail(MessageData messageData) throws MessagingException {
		MimeMessage mime=javaMailSender.createMimeMessage();
		MimeMessageHelper message=new MimeMessageHelper(mime,false);
		
		message.setTo(messageData.getTo());
		message.setSubject(messageData.getSubject());
		
		String emailBody=messageData.getText()
				+"<br><br><h4>Thanks & Regard</h4><br>"
				+"<h4>"+messageData.getSenderName()+"</h4><br>"
				+"<h4>"+messageData.getSenderAddress()+"</h4>"
				;
		
		message.setText(emailBody,true);
		
		message.setSentDate(new Date());
		javaMailSender.send(mime);
		
		return new ResponseEntity<String>("My Mail send Successfully!!!!!",HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> forgotPassword(String emailaddress)throws MessagingException {
		User user=repo.findByEmailAddress(emailaddress);
		
		if(user!=null)
		{
			MimeMessage mime=javaMailSender.createMimeMessage();
			MimeMessageHelper message=new MimeMessageHelper(mime,true);
			
			message.setTo(user.getEmailAddress());
			message.setSubject("Link To Create Password");
			
			String emailBody="Hi"+user.getUserFirstName()+",You can change the password using the below link."
					+"<br><br><h4>Thanks & Regard</h4><br>"
					+"<h4>Jspider</h4><br>"
					+"<h4>http</h4>";
			message.setText(emailBody,true);
			message.setSentDate(new Date());
			
			javaMailSender.send(mime);
			return new ResponseEntity<String>("Mail Has been Sent,check Your mail",HttpStatus.OK);
		}
		throw new UserNotFoundByEmail("Please, Enter Valid Email");
	}
}