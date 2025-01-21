package com.examplespringboot.dto.response;

import com.examplespringboot.dto.validator.Gender;
import com.examplespringboot.util.UserStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;


@Getter
@Setter
@Builder
public class UserDetailResponse implements Serializable {
    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    private Date dateOfBirth;

    private Gender gender;

    private String username;

    private String type;

    private UserStatus status;

    public UserDetailResponse(Long id, String firstName, String lastName, String phone, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
    }

    public UserDetailResponse(Long id, String firstName, String lastName, String email, String phone, Date dateOfBirth, Gender gender, String username, String type, UserStatus status) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.username = username;
        this.type = type;
        this.status = status;
    }
}
