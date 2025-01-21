package com.examplespringboot.service.impl;

import com.examplespringboot.dto.request.AddressDTO;
import com.examplespringboot.dto.request.UserRequestDTO;
import com.examplespringboot.dto.response.PageResponse;
import com.examplespringboot.dto.response.UserDetailResponse;
import com.examplespringboot.exception.ResourceNotFoundExeption;
import com.examplespringboot.model.Address;
import com.examplespringboot.model.User;
import com.examplespringboot.repository.SearchRepository;
import com.examplespringboot.repository.UserRepository;
import com.examplespringboot.service.UserService;
import com.examplespringboot.util.UserStatus;
import com.examplespringboot.util.UserType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final SearchRepository searchRepository;

    @Override
    public void addUser(UserRequestDTO userDTO) {
        System.out.println("Add user to db");

        if (!userDTO.getFirstName().equals("Thuan")) {
            throw new ResourceNotFoundExeption("Name khac thuan");
        }
    }

    @Override
    public long saveUser(UserRequestDTO request) {
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .dateOfBirth(request.getDateOfBirth())
                .gender(request.getGender())
                .phone(request.getPhone())
                .email(request.getEmail())
                .username(request.getUsername())
                .password(request.getPassword())
                .status(request.getStatus())
                .type(UserType.valueOf(request.getType().toUpperCase()))
                //.addresses(convertToAddress(request.getAddresses()))
                .build();

        request.getAddresses().forEach(a ->
                user.saveAddress(Address.builder()
                        .apartmentNumber(a.getApartmentNumber())
                        .floor(a.getFloor())
                        .building(a.getBuilding())
                        .streetNumber(a.getStreetNumber())
                        .street(a.getStreet())
                        .city(a.getCity())
                        .country(a.getCountry())
                        .addressType(a.getAddressType())
                        .build()));
        userRepository.save(user);

        log.info("User has saved");

        return user.getId();
    }

    @Override
    public void updateUser(long userId, UserRequestDTO request) {
        User user = getUserById(userId);
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setDateOfBirth(request.getDateOfBirth());
        user.setGender(request.getGender());
        user.setPhone(request.getPhone());
        if (!request.getEmail().equals(user.getEmail())) {
            // check email from database if not exist then allow update email otherwise throw exception
            user.setEmail(request.getEmail());
        }
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setStatus(request.getStatus());
        user.setType(UserType.valueOf(request.getType().toUpperCase()));
        user.setAddresses(convertToAddress(request.getAddresses()));
        userRepository.save(user);

        log.info("User has updated successfully, userId={}", userId);
    }

    @Override
    public void changeStatus(long userId, UserStatus status) {
        User user = getUserById(userId);
        user.setStatus(status);
        userRepository.save(user);

        log.info("User has updated status successfully, userId={}", userId);
    }

    @Override
    public void deleteUser(long userId) {
        userRepository.deleteById(userId);
        log.info("User has deleted successfully, userId={}", userId);
    }

    @Override
    public UserDetailResponse getUserDetail(long userId) {
        User user = getUserById(userId);

        return UserDetailResponse.builder()
                .id(userId)
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .dateOfBirth(user.getDateOfBirth())
                .gender(user.getGender())
                .phone(user.getPhone())
                .email(user.getEmail())
                .username(user.getUsername())
                .status(user.getStatus())
                .type(user.getType().name())
                .build();

    }

    @Override
    public PageResponse<?> listAllUserWithSortBy(int pageNo, int pageSize, String sortBy) {
        List<Sort.Order> sorts = new ArrayList<>();

        if (StringUtils.hasLength(sortBy)) {
            // column:asc|desc
            Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)");
            Matcher matcher = pattern.matcher(sortBy);
            if (matcher.find()) {
                if (matcher.group(3).equalsIgnoreCase("asc")) {
                    sorts.add(new Sort.Order(Sort.Direction.ASC, matcher.group(1)));
                } else {
                    sorts.add(new Sort.Order(Sort.Direction.DESC, matcher.group(1)));
                }
            }
        }

        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sorts));

        Page<User> users = userRepository.findAll(pageable);

        List<UserDetailResponse> list = users.stream().map(user -> UserDetailResponse.builder()
                        .id(user.getId())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .dateOfBirth(user.getDateOfBirth())
                        .gender(user.getGender())
                        .phone(user.getPhone())
                        .email(user.getEmail())
                        .username(user.getUsername())
                        .status(user.getStatus())
                        .type(user.getType().name())
                        .build())
                .toList();

        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPage(users.getTotalPages())
                .items(list)
                .build();

//        return list;
    }

    @Override
    public PageResponse<?> listAllUserWithSortByMultipleColumns(int pageNo, int pageSize, String... sorts) {
        List<Sort.Order> orders = new ArrayList<>();
        Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)");
        Matcher matcher = null;
        if (sorts != null) {
            for (String sortBy : sorts) {
                // column:asc|desc
                log.info("sortBy: {}", sortBy);
                matcher = pattern.matcher(sortBy);
                if (matcher.find()) {
                    if (matcher.group(3).equalsIgnoreCase("asc")) {
                        orders.add(new Sort.Order(Sort.Direction.ASC, matcher.group(1)));
                    } else {
                        orders.add(new Sort.Order(Sort.Direction.DESC, matcher.group(1)));
                    }
                }
            }

        }

        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(orders));

        Page<User> users = userRepository.findAll(pageable);

        List<UserDetailResponse> list = users.stream().map(user -> UserDetailResponse.builder()
                        .id(user.getId())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .dateOfBirth(user.getDateOfBirth())
                        .gender(user.getGender())
                        .phone(user.getPhone())
                        .email(user.getEmail())
                        .username(user.getUsername())
                        .status(user.getStatus())
                        .type(user.getType().name())
                        .build())
                .toList();

        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPage(users.getTotalPages())
                .items(list)
                .build();

//        return list;
    }

    @Override
    public PageResponse<?> getAllUsersAndSearchWithPagingAndSorting(int pageNo, int pageSize, String search, String sort) {
        return searchRepository.searchUser(pageNo, pageSize, search, sort);
    }


    @Override
    public PageResponse<?> advanceSearchByCriteria(int pageNo, int pageSize, String sortBy, String... search) {
        return null;
    }

    private Set<Address> convertToAddress(Set<AddressDTO> addresses) {
        Set<Address> result = new HashSet<>();
        addresses.forEach(a ->
                result.add(Address.builder()
                        .apartmentNumber(a.getApartmentNumber())
                        .floor(a.getFloor())
                        .building(a.getBuilding())
                        .streetNumber(a.getStreetNumber())
                        .street(a.getStreet())
                        .city(a.getCity())
                        .country(a.getCountry())
                        .addressType(a.getAddressType())
                        .build())
        );
        return result;
    }

    private User getUserById(long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundExeption("User not found"));
    }

}
