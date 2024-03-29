package com.restfulapi.userrecords.services.impl;

import com.restfulapi.userrecords.datas.models.Gender;
import com.restfulapi.userrecords.datas.models.User;
import com.restfulapi.userrecords.datas.repositories.UserRepository;
import com.restfulapi.userrecords.dtos.requests.CreateUserRequest;
import com.restfulapi.userrecords.dtos.requests.UpdateUserRequest;
import com.restfulapi.userrecords.exceptions.InvalidGenderException;
import com.restfulapi.userrecords.exceptions.UserNotFoundException;
import com.restfulapi.userrecords.services.UserService;
import static com.restfulapi.userrecords.utils.UserUtils.*;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.springframework.data.domain.Sort.by;

@Service
public class UserServiceImpl implements UserService {
   private UserRepository userRepository;

    ModelMapper modelMapper = new ModelMapper();

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;

    }

    @Override
    public User createUser(CreateUserRequest createUserRequest) {
        User user = new User();
        modelMapper.map(createUserRequest, user);
        if(!isValidGender(createUserRequest.getGender().toUpperCase())) throw new InvalidGenderException("Enter M for male or F for Female");
        user.setGender(Gender.valueOf(createUserRequest.getGender().toUpperCase()));

        LocalDate date = getFormattedDateOfBirth(createUserRequest.getDateOfBirth());
        user.setDateOfBirth(date);
        user.setAge(generateAge(createUserRequest.getDateOfBirth()));
        user.setToken(generateToken(30));
        user.setDateCreated(LocalDateTime.now().withNano(0));
        user.setDateUpdated(LocalDateTime.now().withNano(0));
        return userRepository.save(user);
    }

    @Override
    public Map<String, Object>  getAllUser(String filterFirstname, String filterLastname, String filterGender, String filterDateOfBirth, int page, int page_size, String sortOrder, String sortField) {
        List<User> users = new ArrayList<>();
        List<Order> orders = new ArrayList<>();
        if(sortField != null && sortOrder.equalsIgnoreCase("asc")) {orders.add(new Order(Sort.Direction.ASC, sortField));
        }
        if(sortField != null && sortOrder.equalsIgnoreCase("desc")) {orders.add(new Order(Sort.Direction.DESC,sortField));}
        if(sortOrder.equalsIgnoreCase("desc")){
            orders.add(new Order(Sort.Direction.DESC, "id"));
        }

        Pageable pagingSort = PageRequest.of(page, page_size,Sort.by(orders));
        Page<User> userPage;

        if (filterFirstname != null) {
            userPage = userRepository.findByFirstnameContainingIgnoreCase(filterFirstname, pagingSort);
        }
        else if(filterLastname != null){
            userPage = userRepository.findByLastnameContainingIgnoreCase(filterLastname, pagingSort);
        }
        else {
            userPage = userRepository.findAll(pagingSort);
        }
         users = userPage.getContent();

        Map<String, Object> response = new HashMap<>();
        response.put("users", users);
        response.put("page_size",userPage.getNumberOfElements());
        response.put("totalUsers", userPage.getTotalElements());
        response.put("totalPages", userPage.getTotalPages());
        response.put("currentPage", userPage.getNumber()+1);
        assert sortField != null;
        response.put("sort_order_mode", userPage.getSort().getOrderFor(sortField).getDirection());
        response.put("sort_field", sortField);
        return response;
    }

    @Override
    public User getUserByToken(String token) {
        return userRepository.findByToken(token).orElseThrow(()-> new UserNotFoundException("User Not Found"));
    }

    @Override
    public User updateUser(String token, UpdateUserRequest request) {
        User foundUser = userRepository.findByToken(token).orElseThrow(()-> new UserNotFoundException("User Not Found"));
        if(!(request.getFirstname().trim().equals("")|| request.getFirstname() == null)) {
            foundUser.setFirstname(request.getFirstname());
        }
        if(!(request.getLastname().trim().equals("")|| request.getLastname() == null)) {
            foundUser.setLastname(request.getLastname());
        }
        if(!(request.getGender().trim().equals("")|| request.getGender() == null)) {
            if(isValidGender(request.getGender())) foundUser.setGender(Gender.valueOf(request.getGender()));
        }
        if(!(request.getDateOfBirth().trim().equals("")|| request.getDateOfBirth() == null)) {
            LocalDate date = getFormattedDateOfBirth(request.getDateOfBirth());
            foundUser.setDateOfBirth(date);
            int newAge = generateAge(request.getDateOfBirth());
            foundUser.setAge(newAge);
        }
        foundUser.setDateUpdated(LocalDateTime.now().withNano(0));

        return userRepository.save(foundUser);
    }

    @Override
    public String deleteUserById(Long userId) {
        userRepository.findById(userId).orElseThrow(()-> new UserNotFoundException("UNAVAILABLE RECORD"));
        userRepository.deleteById(userId);
        return "Successfully Deleted";
    }
   @Override
   public List<User> findAllUser(){
   return userRepository.findAll();
   }



}
