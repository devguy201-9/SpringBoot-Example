package com.examplespringboot.controller;

import com.examplespringboot.dto.request.UserRequestDTO;
import com.examplespringboot.dto.response.ResponseSuccess;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @PostMapping("/")
    public ResponseSuccess addUser(@Valid @RequestBody UserRequestDTO userDTO) {
        return new ResponseSuccess(HttpStatus.CREATED, "User added successfully", 1);
    }


    @PutMapping("/{userId}")
    public ResponseSuccess updateUser(@PathVariable int userId, @Valid @RequestBody UserRequestDTO userDTO) {
        System.out.println("User is updated with userID=" + userId);
        return new ResponseSuccess(HttpStatus.ACCEPTED, "User updated successfully");

    }

    @PatchMapping("/{userID}")
    public ResponseSuccess changeStatus(@Min(1) @PathVariable int userId, @RequestParam boolean status) {
        System.out.println("User is updated with userID=" + userId);
        return new ResponseSuccess(HttpStatus.ACCEPTED, "User changed status successfully");
    }

    @DeleteMapping("/{userId}")
    public ResponseSuccess deleteUser(@PathVariable int userId) {
        System.out.println("User is deleted with userID=" + userId);
        return new ResponseSuccess(HttpStatus.NO_CONTENT, "User deleted successfully");
    }

    @GetMapping("/{userId}")
    public ResponseSuccess getUser(@PathVariable int userId) {
        return new ResponseSuccess(HttpStatus.OK, "user", new UserRequestDTO("Thuan", "Tran", "phone", "email"));
    }

    @GetMapping("/list")
    public ResponseSuccess getAllUsers(@RequestParam(defaultValue = "0") int pageNo, @RequestParam(defaultValue = "10") int pageSize) {
        return new ResponseSuccess(HttpStatus.OK, "users", List.of(new UserRequestDTO("Thuan", "Tran", "phone", "email"), new UserRequestDTO("Thuan", "Tran", "phone", "email")));
    }
}

