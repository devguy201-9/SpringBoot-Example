package com.examplespringboot.controller;

import com.examplespringboot.configuration.Translator;
import com.examplespringboot.dto.request.UserRequestDTO;
import com.examplespringboot.dto.response.PageResponse;
import com.examplespringboot.dto.response.ResponseData;
import com.examplespringboot.dto.response.ResponseError;
import com.examplespringboot.dto.response.UserDetailResponse;
import com.examplespringboot.exception.ResourceNotFoundExeption;
import com.examplespringboot.service.UserService;
import com.examplespringboot.util.UserStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Validated
@Slf4j
@Tag(name = "User Controller")
@RequiredArgsConstructor
public class UserController {

    /*@Autowired
    private UserService userService;*/

    private final UserService userService;

    @Operation(summary = "Add new user", description = "API create new user")
    @PostMapping("/")
    public ResponseData<Long> addUser(@Valid @RequestBody UserRequestDTO userDTO) {
        //return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Can not created user");
        //return new ResponseData<>(HttpStatus.CREATED.value(), "User added successfully", 1);
        /*try {
            userService.addUser(userDTO);
            return new ResponseData<>(HttpStatus.CREATED.value(), Translator.toLocate("user.add.success"), 1);
        } catch (ResourceNotFoundExeption e){
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Add fail");
        }*/

        try {
            long userId = userService.saveUser(userDTO);
            return new ResponseData<>(HttpStatus.CREATED.value(), Translator.toLocate("user.add.success"), userId);
        } catch (Exception e) {
            log.info("errorMessage= {}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Add user fail");
        }

    }


    @Operation(summary = "Update user", description = "API update user")
    @PutMapping("/{userId}")
    public ResponseData<?> updateUser(@PathVariable long userId, @Valid @RequestBody UserRequestDTO userDTO) {
        log.info("Request update user= {} {}", userDTO.getFirstName(), userDTO.getLastName());
//        return new ResponseData<>(HttpStatus.ACCEPTED.value(), Translator.toLocate("user.upd.success"));

        try {
            userService.updateUser(userId, userDTO);
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), Translator.toLocate("user.upd.success"));
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Update user fail");
        }

    }

    @Operation(summary = "Change status of user", description = "API change user status")
    @PatchMapping("/{userId}")
    public ResponseData<?> changeStatus(@Min(1) @PathVariable int userId, @RequestParam UserStatus status) {
        System.out.println("User is updated with userID=" + userId);
//        return new ResponseData<>(HttpStatus.ACCEPTED.value(), Translator.toLocate("user.change.success"));

        try {
            userService.changeStatus(userId, status);
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), Translator.toLocate("user.change.success"));
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Change status user fail");
        }

    }

    @Operation(summary = "Delete user", description = "API delete user")
    @DeleteMapping("/{userId}")
    public ResponseData<?> deleteUser(@PathVariable long userId) {
        System.out.println("User is deleted with userID=" + userId);
//        return new ResponseData<>(HttpStatus.NO_CONTENT.value(), Translator.toLocate("user.del.success"));

        try {
            userService.deleteUser(userId);
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), Translator.toLocate("user.del.success"));
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Delete user fail");
        }
    }

    @Operation(summary = "Get user detail", description = "API get user detail")
    @GetMapping("/{userId}")
    public ResponseData<UserDetailResponse> getUser(@PathVariable long userId) {
//        return new ResponseData<>(HttpStatus.OK.value(), "user", new UserRequestDTO("Thuan", "Tran", "phone", "email"));

        try {
            return new ResponseData<>(HttpStatus.OK.value(), "user", userService.getUserDetail(userId));
        } catch (ResourceNotFoundExeption e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }

    }

    @Operation(summary = "Get user list per page", description = "Return user by pageNo and pageSize")
    @GetMapping("/list")
    public ResponseData<PageResponse<?>> getAllUsers(@RequestParam(defaultValue = "0", required = false) int pageNo,
                                                     @Min(10) @RequestParam(defaultValue = "20", required = false) int pageSize,
                                                     @RequestParam(required = false) String sortBy) {
        //return new ResponseData<>(HttpStatus.OK.value(), "users", List.of(
        // new UserRequestDTO("Thuan", "Tran", "phone", "email"),
        // new UserRequestDTO("Thuan", "Tran", "phone", "email")));

        return new ResponseData<>(HttpStatus.OK.value(), "users", userService.listAllUserWithSortBy(pageNo, pageSize, sortBy));

    }

    @Operation(summary = "Get list of users with sort by multiple columns", description = "Send a request via this API to get user list by pageNo and pageSize")
    @GetMapping("/list-with-sort-by-multiple-columns")
    public ResponseData<PageResponse<?>> getAllUserWithSortByMultipleColumns(@RequestParam(defaultValue = "0", required = false) int pageNo,
                                                                             @Min(1) @RequestParam(defaultValue = "20", required = false) int pageSize,
                                                                             @RequestParam(required = false) String... sorts) {
        log.info("Request get all of users with sort by multiple columns");
        return new ResponseData<>(HttpStatus.OK.value(), "users", userService.listAllUserWithSortByMultipleColumns(pageNo, pageSize, sorts));

    }

    @Operation(summary = "Get list of users and search with paging and sorting by customize query", description = "Send a request via this API to get user list by pageNo, pageSize and sort by multiple column")
    @GetMapping("/list-user-and-search-with-paging-and-sorting")
    public ResponseData<?> getAllUsersAndSearchWithPagingAndSorting(@RequestParam(defaultValue = "0", required = false) int pageNo,
                                                                    @RequestParam(defaultValue = "20", required = false) int pageSize,
                                                                    @RequestParam(required = false) String search,
                                                                    @RequestParam(required = false) String sortBy) {
        log.info("Request get list of users and search with paging and sorting");
        return new ResponseData<>(HttpStatus.OK.value(), "users", userService.getAllUsersAndSearchWithPagingAndSorting(pageNo, pageSize, search, sortBy));

    }

    @Operation(summary = "Get list of users and search with paging and sorting by criteria", description = "Send a request via this API to get user list by pageNo and pageSize and sort by criteria")
    @GetMapping("/advance-search-by-criteria")
    public ResponseData<PageResponse<?>> advanceSearchByCriteria(@RequestParam(defaultValue = "0", required = false) int pageNo,
                                                                 @Min(1) @RequestParam(defaultValue = "20", required = false) int pageSize,
                                                                 @RequestParam(required = false) String sortBy,
                                                                 @RequestParam(required = false) String... search) {
        log.info("Request advance search with criteria and paging and sorting");
        return new ResponseData<>(HttpStatus.OK.value(), "users", userService.advanceSearchByCriteria(pageNo, pageSize, sortBy, search));

    }
}

