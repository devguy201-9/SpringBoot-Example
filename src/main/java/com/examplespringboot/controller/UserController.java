package com.examplespringboot.controller;

import com.examplespringboot.dto.request.UserRequestDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @PostMapping("/")
    public String addUser(@Valid @RequestBody UserRequestDTO userDTO) {
        return "User Added";
    }


    @PutMapping("/{userId}")
    public String updateUser(@PathVariable int userId,@Valid @RequestBody UserRequestDTO userDTO) {
        System.out.println("User is updated with userID=" + userId);
        return "User updated";

    }

    @PatchMapping("/{userID}")
    public String changeStatus(@Min(1) @PathVariable int userId, @RequestParam boolean status) {
        System.out.println("User is updated with userID=" + userId);
        return "User status changed";
    }

    @DeleteMapping("/{userId}")
    public String deleteUser(@PathVariable int userId) {
        System.out.println("User is deleted with userID=" + userId);
        return "User is deleted";
    }

    @GetMapping("/{userId}")
    public UserRequestDTO getUser(@PathVariable int userId) {
        return new UserRequestDTO("Thuan", "Tran", "phone", "email");
    }

    @GetMapping("/list")
    public List<UserRequestDTO> getAllUsers(@RequestParam(defaultValue = "0") int pageNo, @RequestParam(defaultValue = "10") int pageSize) {
        return List.of(new UserRequestDTO("Thuan", "Tran", "phone", "email"), new UserRequestDTO("Thuan", "Tran", "phone", "email"));
    }
}

