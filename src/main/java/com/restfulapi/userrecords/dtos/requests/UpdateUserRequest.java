package com.restfulapi.userrecords.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UpdateUserRequest {
    private int id;
    private String firstname;
    private String lastname;
    private String gender;
    private String date_of_birth;

    public UpdateUserRequest() {
        this.firstname = "";
        this.lastname = "";
        this.gender = "";
        this.date_of_birth = "";
    }
}
