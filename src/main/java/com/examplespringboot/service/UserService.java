package com.examplespringboot.service;

import com.examplespringboot.dto.request.UserRequestDTO;


public interface UserService {
    void addUser(UserRequestDTO userDTO);
}
