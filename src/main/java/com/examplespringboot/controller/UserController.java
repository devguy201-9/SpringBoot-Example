package com.examplespringboot.controller;

import com.examplespringboot.configuration.Translator;
import com.examplespringboot.dto.request.UserRequestDTO;
import com.examplespringboot.dto.response.ResponseData;
import com.examplespringboot.dto.response.ResponseError;
import com.examplespringboot.exception.ResourceNotFoundExeption;
import com.examplespringboot.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@Validated
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/")
    public ResponseData<Integer> addUser(@Valid @RequestBody UserRequestDTO userDTO) {
        //return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Can not created user");
        //return new ResponseData<>(HttpStatus.CREATED.value(), "User added successfully", 1);
        try {
            userService.addUser(userDTO);
            return new ResponseData<>(HttpStatus.CREATED.value(), Translator.toLocate("user.add.success"), 1);
        } catch (ResourceNotFoundExeption e){
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Add fail");
        }
    }


    @PutMapping("/{userId}")
    public ResponseData<?> updateUser(@PathVariable int userId, @Valid @RequestBody UserRequestDTO userDTO) {
        System.out.println("User is updated with userID=" + userId);
        return new ResponseData<>(HttpStatus.ACCEPTED.value(), Translator.toLocate("user.upd.success"));

    }

    @PatchMapping("/{userID}")
    public ResponseData<?> changeStatus(@Min(1) @PathVariable int userId, @RequestParam boolean status, @PathVariable String userID) {
        System.out.println("User is updated with userID=" + userId);
        return new ResponseData<>(HttpStatus.ACCEPTED.value(), Translator.toLocate("user.change.success"));
    }

    @DeleteMapping("/{userId}")
    public ResponseData<?> deleteUser(@PathVariable int userId) {
        System.out.println("User is deleted with userID=" + userId);
        return new ResponseData<>(HttpStatus.NO_CONTENT.value(), Translator.toLocate("user.del.success"));
    }

    @GetMapping("/{userId}")
    public ResponseData<UserRequestDTO> getUser(@PathVariable int userId) {
        return new ResponseData<>(HttpStatus.OK.value(), "user", new UserRequestDTO("Thuan", "Tran", "phone", "email"));
    }

    @GetMapping("/list")
    public ResponseData<List<UserRequestDTO>> getAllUsers(@RequestParam(defaultValue = "0") int pageNo, @RequestParam(defaultValue = "10") int pageSize) {
        return new ResponseData<>(HttpStatus.OK.value(), "users", List.of(new UserRequestDTO("Thuan", "Tran", "phone", "email"), new UserRequestDTO("Thuan", "Tran", "phone", "email")));
    }
}

