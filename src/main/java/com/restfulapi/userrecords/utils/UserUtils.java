package com.restfulapi.userrecords.utils;

import com.restfulapi.userrecords.dtos.requests.CreateUserRequest;
import com.restfulapi.userrecords.exceptions.DateErrorException;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class UserUtils {

    private static final String ALPHANUMERIC = "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final SecureRandom secureRandom = new SecureRandom();

    public static String generateUserFullName(CreateUserRequest userRequest) {
        return userRequest.getFirstname()+" "+ userRequest.getLastname();
    }
    public static String generateToken(int length){
       StringBuilder token = new StringBuilder();
        for (int i = 0; i < length; i++) {
        int randomPosition = secureRandom.nextInt(ALPHANUMERIC.length());
        token.append(ALPHANUMERIC.charAt(randomPosition));
        }
        return String.valueOf(token);
    }

    public static int generateAge(String dob){
        try {
            LocalDate dateOfBirth = LocalDate.parse(dob);
            LocalDate presentDate = LocalDate.now();
            return Period.between(dateOfBirth, presentDate).getYears();
        }catch (Exception ex){
             throw new DateErrorException("Date Should be in the format yyyy-MM-dd");
        }

    }
    public static LocalDate getFormattedDateOfBirth(String dateOfBirth) {
        try {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(dateOfBirth, formatter);
        }catch (Exception ex){
            ex.printStackTrace();
            throw  new DateErrorException("Date Should be in the format yyyy-MM-dd");
        }
    }

    public static boolean isValidGender(String gender) {
        return gender.equals("M") || gender.equals("F");
    }
}
