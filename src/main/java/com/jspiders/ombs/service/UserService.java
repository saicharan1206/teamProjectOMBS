package com.jspiders.ombs.service;

import com.jspiders.ombs.dto.UserEmailResponseDTO;
import com.jspiders.ombs.dto.UserRequestDTO;
import com.jspiders.ombs.dto.UserResponseDTO;
import com.jspiders.ombs.util.ResponseStructure;

import jakarta.mail.MessagingException;

public interface UserService
{

  public ResponseStructure<UserResponseDTO> saveUser(UserRequestDTO user) throws MessagingException;

  public ResponseStructure<UserResponseDTO> getByEmail(String email, String password);

  public ResponseStructure<UserEmailResponseDTO> sendEmail(String email) throws MessagingException;

}
