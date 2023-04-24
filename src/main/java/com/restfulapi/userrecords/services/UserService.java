package com.restfulapi.userrecords.services;

import com.restfulapi.userrecords.datas.models.User;
import com.restfulapi.userrecords.dtos.requests.CreateUserRequest;
import com.restfulapi.userrecords.dtos.requests.UpdateUserRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Map;


public interface UserService {

    User createUser(CreateUserRequest createUserRequest);
    Map<String, Object> getAllUser(String filterFirstname, String FilterLastname,String filterGender, String filterDateOfBirth, int page, int page_size, String sortOrder, String sortField);
    User getUserByToken(String token);
    User updateUser(String token, UpdateUserRequest request);
    String deleteUserById(Long userId);
    List<User> findAllUser();
}
