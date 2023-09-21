package com.jspiders.ombs.serviceimpl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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

import com.jspiders.ombs.dto.UserRequestDTO;
import com.jspiders.ombs.dto.UserResponseDTO;
import com.jspiders.ombs.repository.UserRepository;
import com.jspiders.ombs.repository.UserRoleRepository;
import com.jspiders.ombs.service.UserService;

import com.jspiders.ombs.util.ResponseStructure;
import com.jspiders.ombs.util.exception.EmailAlreadyExistException;
import com.jspiders.ombs.util.exception.EmaildoesNotExistException;
import com.jspiders.ombs.util.exception.InvalidPasswordException;
import com.jspiders.ombs.util.exception.UserNotFoundByIdException;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;

import com.jspiders.ombs.entity.IsDeleted;
import com.jspiders.ombs.entity.User;
import com.jspiders.ombs.entity.UserRole;

@Service
public class UserServiceImpl implements UserService {

	String email;

	@Autowired
	private UserRepository repoUser;

	@Autowired
	private UserRoleRepository repoUserRole;

	@Autowired
	private JavaMailSender javaMailSender;

	@Override
	public ResponseEntity<ResponseStructure<UserResponseDTO>> userSave(UserRequestDTO userRequest) {

		List<User> userList = repoUser.findAll();
		User user = new User();

		if (userList != null) {
			for (User ele : userList) {
				if (ele.getUserEmail().equalsIgnoreCase(userRequest.getUserEmail())) {
					throw new EmailAlreadyExistException("DATA IS NOT SAVED ");
				}
			}
		}

		List<UserRole> role = repoUserRole.getAllUserRoles();
		UserRole userRole = new UserRole();

		boolean flag = true;
		if (!role.isEmpty()) {

			for (UserRole roles : role) {
				if (roles.getUserRole().equalsIgnoreCase(userRequest.getUserRole())) {
					user.setUserRole1(roles);
					sendMail(userRequest);
					flag = false;
					break;
				}

			}

			if (flag == true) {
				userRole.setUserRole(userRequest.getUserRole());
				repoUserRole.save(userRole);
				user.setUserRole1(userRole);
				sendMail(userRequest);
			}

		} else {
			userRole.setUserRole(userRequest.getUserRole());
			repoUserRole.save(userRole);
			user.setUserRole1(userRole);
			sendMail(userRequest);
		}

		user.setUserEmail(userRequest.getUserEmail().toLowerCase());
		user.setUserPassword(userRequest.getUserPassword());
		user.setUserFirstName(userRequest.getUserFirstName());
		user.setUserLastName(userRequest.getUserLastName());
		user.setIsDeleted(userRequest.getIsDeleted().FALSE);

		// user.setUserRole(userRequest.getUserRole());
		repoUser.save(user);

		UserResponseDTO response = new UserResponseDTO();
		response.setUserEmail(userRequest.getUserEmail());
		response.setUserFirstName(userRequest.getUserFirstName());
		response.setUserRole(userRequest.getUserRole());
		response.setUserLastName(userRequest.getUserLastName());

		ResponseStructure<UserResponseDTO> responseStructure = new ResponseStructure<UserResponseDTO>();
		responseStructure.setData(response);
		responseStructure.setMessage("DATA SAVED SUCCESSFULL");
		responseStructure.setStatusCode(HttpStatus.OK.value());

		return new ResponseEntity<ResponseStructure<UserResponseDTO>>(responseStructure, HttpStatus.OK);

	}

/////****************** LOGIN DETAILS ******************************///////////	

	@Override
	public ResponseEntity<ResponseStructure<UserResponseDTO>> userLogin(UserRequestDTO userRequest) {

		String userEmail = userRequest.getUserEmail().toLowerCase();
		String password = userRequest.getUserPassword();
		List<User> users = new ArrayList<User>();
		users = repoUser.getAllUserDetails();
		boolean flag = false;

		UserResponseDTO response = new UserResponseDTO();
		ResponseStructure<UserResponseDTO> structure = new ResponseStructure<>();
		for (int i = 0; i < users.size(); i++) {
			if (users.get(i).getUserEmail().equals(userEmail)) {
				flag = true;
				if (users.get(i).getUserPassword().equals(password)) {
					// response.setMessage("USER AND PASSWORD EXIST");
					response.setUserRole(users.get(i).getUserRole1().getUserRole());
					structure.setStatusCode(HttpStatus.OK.value());

					structure.setData(response);
					System.out.println(users.get(i).getUserRole1().getUserRole());
					structure.setMessage("THE " + users.get(i).getUserRole1().getUserRole() + " LOGIN SUCCESSFULLYY");

				} else {
					// response.setMessage("INVALID PASSWORD");
					throw new InvalidPasswordException("INVALID CREDENTIALS");
				}
			}
		}
		if (flag == false) {

			throw new EmaildoesNotExistException("INVALID CREDENTIALS");

		}
		return new ResponseEntity<ResponseStructure<UserResponseDTO>>(structure, HttpStatus.OK);
	}

//*********************************************EMAIL METHOD ***********************************************

	public void sendMail(UserRequestDTO userRequest) {

		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setTo(userRequest.getUserEmail());
		mail.setSubject("ACCOUNT CREATED SUCCESSFULY");
		mail.setText("HELLO " + userRequest.getUserFirstName() + " YOUR ACCOUNT CREATED SUCCESSFULLY AS "
				+ userRequest.getUserRole());
		mail.setSentDate(new Date());
		javaMailSender.send(mail);

	}

	@Override
	public ResponseEntity<ResponseStructure<String>> forgotPasswordValidation(String userEmail)
			throws MessagingException {

		email = repoUser.getUserByEmail(userEmail);

		MimeMessage mime = javaMailSender.createMimeMessage();
		MimeMessageHelper message = new MimeMessageHelper(mime, true);
		ResponseStructure<String> structure = new ResponseStructure<>();
		if (email != null) {
			FileOutputStream fout = null;
			try {
				fout = new FileOutputStream("C:\\Users\\admin\\Desktop\\Myfile\\sample.txt");
				byte[] b = email.getBytes();
				fout.write(b);
				fout.flush();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					fout.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			message.setTo(email);
			message.setSubject("ITS A VALID EMAIL");

			String messageBody = "<a href='http://127.0.0.1:5500/Spring/ResetPassword.html'>Link</a>";
			message.setText(messageBody, true);
			message.setSentDate(new Date());
			javaMailSender.send(mime);
			structure.setData(email);
			structure.setMessage("GOT TO YOUR EMAIL TO RESET YOR PASSWORD");
			structure.setStatusCode(HttpStatus.OK.value());

			return new ResponseEntity<ResponseStructure<String>>(structure, HttpStatus.OK);
		} else {
			throw new EmaildoesNotExistException("INVALID CREDENTIALS");

		}
	}

//************** to update ****************************//
	@Override
	public ResponseEntity<ResponseStructure<UserResponseDTO>> updateUser(UserRequestDTO userRequestDTO, int userId) {

		User user = new User();
		ResponseStructure<UserResponseDTO> structure = new ResponseStructure<>();
		Optional<User> optional = repoUser.findById(userId);
		if (optional.isPresent()) {
			user = optional.get();
			user.setUserId(userId);
			user.setUserEmail(userRequestDTO.getUserEmail());
			user.setUserFirstName(userRequestDTO.getUserFirstName());
			user.setUserLastName(userRequestDTO.getUserLastName());
			user.setUserPassword(userRequestDTO.getUserPassword());

			repoUser.save(user);

			UserResponseDTO response = new UserResponseDTO();
			response.setUserEmail(user.getUserEmail());

			response.setUserFirstName(userRequestDTO.getUserFirstName());
			response.setUserLastName(userRequestDTO.getUserLastName());
			response.setUserRole(user.getUserRole1().getUserRole());
			structure.setData(response);
			structure.setStatusCode(HttpStatus.OK.value());
			structure.setMessage("UPDATED SUCCESFULLYY");

			return new ResponseEntity<ResponseStructure<UserResponseDTO>>(structure, HttpStatus.OK);

		} else {
			throw new UserNotFoundByIdException("INVALID DATA");
		}

	}

	@Override
	public ResponseEntity<ResponseStructure<UserResponseDTO>> deleteUser(int userId) {

		Optional<User> optional = repoUser.findById(userId);
		if (optional.isPresent()) {
			User user = new User();
			user = optional.get();
			user.setIsDeleted(IsDeleted.TRUE);
			UserResponseDTO response = new UserResponseDTO();
			response.setUserEmail(user.getUserEmail());

			response.setUserFirstName(user.getUserFirstName());
			response.setUserLastName(user.getUserLastName());
			response.setUserRole(user.getUserRole1().getUserRole());
			repoUser.save(user);
			ResponseStructure<UserResponseDTO> structure = new ResponseStructure<>();
			structure.setData(response);
			structure.setStatusCode(HttpStatus.OK.value());
			structure.setMessage("DELETED SUCCESFULLYY");

			return new ResponseEntity<ResponseStructure<UserResponseDTO>>(structure, HttpStatus.OK);
		}

		else {
			throw new UserNotFoundByIdException("USER NOT FOUND");
		}

	}

	@Override
	public ResponseEntity<ResponseStructure<List<UserResponseDTO>>> findAllUserDetails() {

		List<User> users = new ArrayList<>();

		List<UserResponseDTO> users2 = new ArrayList<>();

		users = repoUser.findAll();

		for (User userdetails : users) {
			UserResponseDTO response = new UserResponseDTO();

			response.setUserEmail(userdetails.getUserEmail());
			response.setUserFirstName(userdetails.getUserFirstName());
			response.setUserLastName(userdetails.getUserLastName());
			response.setUserRole(userdetails.getUserRole1().getUserRole());

			users2.add(response);
		}

		ResponseStructure<List<UserResponseDTO>> structure = new ResponseStructure<>();
		structure.setData(users2);
		structure.setMessage("ALL DATA ARE FETCHED");
		structure.setStatusCode(HttpStatus.OK.value());
		return new ResponseEntity<ResponseStructure<List<UserResponseDTO>>>(structure, HttpStatus.OK);

	}

	@Override
	public ResponseEntity<ResponseStructure<UserResponseDTO>> updatePassword(String userPassword) {
		
		User user = repoUser.getUserFromEmailAndPassword(email);
		System.out.println();
		System.out.println(user);
		UserRequestDTO userRequest = new UserRequestDTO();
		userRequest.setUserEmail(user.getUserEmail());
		userRequest.setUserPassword(userPassword);
		userRequest.setUserFirstName(user.getUserFirstName());
		userRequest.setUserLastName(user.getUserLastName());
		userRequest.setUserRole(user.getUserRole1().getUserRole());
		return updateUser(userRequest, user.getUserId());
	}

}
