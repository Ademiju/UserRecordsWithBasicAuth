package com.restfulapi.userrecords.dtos.requests;

import com.restfulapi.userrecords.datas.models.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest {
    private String firstname;
    private String lastname;
    private String gender;
    private String dateOfBirth;

}
