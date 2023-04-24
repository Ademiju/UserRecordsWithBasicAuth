package com.restfulapi.userrecords.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UpdateUserRequest {
    private String firstname;
    private String lastname;
    private String gender;
    private String dateOfBirth;

    public UpdateUserRequest() {
        this.firstname = "";
        this.lastname = "";
        this.gender = "";
        this.dateOfBirth = "";
    }
}
