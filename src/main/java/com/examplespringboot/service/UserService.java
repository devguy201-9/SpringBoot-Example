package com.examplespringboot.service;

import com.examplespringboot.dto.request.UserRequestDTO;
import com.examplespringboot.dto.response.PageResponse;
import com.examplespringboot.dto.response.UserDetailResponse;
import com.examplespringboot.util.UserStatus;


public interface UserService {
    // For example previous lesson
    void addUser(UserRequestDTO userDTO);

    long saveUser(UserRequestDTO request);

    void updateUser(long userId, UserRequestDTO request);

    void changeStatus(long userId, UserStatus status);

    void deleteUser(long userId);

    UserDetailResponse getUserDetail(long userId);

    PageResponse<?> listAllUserWithSortBy(int pageNo, int pageSize, String sortBy);

    PageResponse<?> listAllUserWithSortByMultipleColumns(int pageNo, int pageSize, String... sorts);

    PageResponse<?> getAllUsersAndSearchWithPagingAndSorting(int pageNo, int pageSize, String search, String sort);

    PageResponse<?> advanceSearchByCriteria(int pageNo, int pageSize, String sortBy, String... search);
}
