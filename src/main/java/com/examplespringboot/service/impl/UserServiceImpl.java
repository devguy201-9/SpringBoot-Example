package com.examplespringboot.service.impl;

import com.examplespringboot.dto.request.UserRequestDTO;
import com.examplespringboot.exception.ResourceNotFoundExeption;
import com.examplespringboot.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Override
    public void addUser(UserRequestDTO userDTO) {
        System.out.println("Add user to db");

        if (!userDTO.getFirstName().equals("Thuan")){
            throw new ResourceNotFoundExeption("Name khac thuan");
        }
    }
}
