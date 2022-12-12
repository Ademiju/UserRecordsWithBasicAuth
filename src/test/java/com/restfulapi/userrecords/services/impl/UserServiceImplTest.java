package com.restfulapi.userrecords.services.impl;

import com.restfulapi.userrecords.datas.models.Gender;
import com.restfulapi.userrecords.datas.models.User;
import com.restfulapi.userrecords.dtos.requests.CreateUserRequest;
import com.restfulapi.userrecords.dtos.requests.UpdateUserRequest;
import com.restfulapi.userrecords.exceptions.InvalidGenderException;
import com.restfulapi.userrecords.services.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class UserServiceImplTest {
    @Autowired
    UserService userService;

    @BeforeEach
    void setUp() {

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void createUserTest() {
        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setFirstname("chibunna");
        userRequest.setLastname("oduonye");
        userRequest.setGender("M");
        userRequest.setDate_of_birth("2015-04-08");
        User createdUser = userService.createUser(userRequest);
        assertEquals((userRequest.getFirstname()),createdUser.getFirstname());
        assertEquals((userRequest.getLastname()),createdUser.getLastname());
        assertEquals((userRequest.getGender()),createdUser.getGender().toString());
        assertEquals((userRequest.getDate_of_birth()),createdUser.getDate_of_birth().toString());
        assertEquals(LocalDateTime.now().withNano(0), createdUser.getDate_created());
        assertEquals(LocalDateTime.now().withNano(0), createdUser.getDate_updated());
    }
    @Test
    void createUserWithInvalidGenderThrowsExceptionTest(){
        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setFirstname("chibunna");
        userRequest.setLastname("oduonye");
        userRequest.setGender("S");
        userRequest.setDate_of_birth("2015-04-08");
        assertThatThrownBy(()-> userService.createUser(userRequest)).isInstanceOf(InvalidGenderException.class).hasMessage("Enter M for male or F for Female");

    }

    @Test
    void getAllUserTest() {

    }

    @Test
    void getUserByIdTest() {
    }

    @Test
    void updateUserTest(){
        User foundUser = userService.getUserById(2L);
        assertEquals("chibunna",foundUser.getFirstname());
        assertEquals("oduonye",foundUser.getLastname());
        assertEquals("F", foundUser.getGender().toString());
        assertEquals("2015-04-08",foundUser.getDate_of_birth().toString());
        UpdateUserRequest request = new UpdateUserRequest(2,"Godwin","Jega","M","2015-04-08");

        User updatedUser = userService.updateUser(request);
        assertEquals(request.getGender(),updatedUser.getGender().toString());
        assertEquals(request.getFirstname(),updatedUser.getFirstname());
        assertEquals(request.getLastname(),updatedUser.getLastname());
        assertEquals(request.getGender(),updatedUser.getGender().toString());
    }

    @Test
    void deleteUserById() {
    }
}