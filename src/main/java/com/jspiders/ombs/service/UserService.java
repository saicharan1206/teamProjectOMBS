package com.jspiders.ombs.service;

import com.jspiders.ombs.dto.UserRequestDTO;
import com.jspiders.ombs.dto.UserResponseDTO;
import com.jspiders.ombs.util.ResponseStructure;

public interface UserService {

  public ResponseStructure<UserResponseDTO> saveUser(UserRequestDTO user);

}
