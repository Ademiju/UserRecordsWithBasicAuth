package com.restfulapi.userrecords.services.impl;

import com.restfulapi.userrecords.datas.models.Gender;
import com.restfulapi.userrecords.datas.models.User;
import com.restfulapi.userrecords.datas.repositories.UserRepository;
import com.restfulapi.userrecords.dtos.requests.CreateUserRequest;
import com.restfulapi.userrecords.dtos.requests.UpdateUserRequest;
import com.restfulapi.userrecords.exceptions.InvalidGenderException;
import com.restfulapi.userrecords.exceptions.UserNotFoundException;
import com.restfulapi.userrecords.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

        LocalDate date = getFormattedDateOfBirth(createUserRequest.getDate_of_birth());
        user.setDate_of_birth(date);
        user.setDate_created(LocalDateTime.now().withNano(0));
        user.setDate_updated(LocalDateTime.now().withNano(0));
        return userRepository.save(user);
    }

    private boolean isValidGender(String gender) {
        return gender.equals("M") || gender.equals("F");
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
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(()-> new UserNotFoundException("User Not Found"));
    }

    @Override
    public User updateUser(UpdateUserRequest request) {
        User foundUser = userRepository.findById((long) request.getId()).orElseThrow(()-> new UserNotFoundException("User Not Found"));
        if(!(request.getFirstname().trim().equals("")|| request.getFirstname() == null)) {
            foundUser.setFirstname(request.getFirstname());
        }
        if(!(request.getLastname().trim().equals("")|| request.getLastname() == null)) {
            foundUser.setLastname(request.getLastname());
        }
        if(!(request.getGender().trim().equals("")|| request.getGender() == null)) {
            if(isValidGender(request.getGender())) foundUser.setGender(Gender.valueOf(request.getGender()));
        }
        if(!(request.getDate_of_birth().trim().equals("")|| request.getDate_of_birth() == null)) {
            LocalDate date = getFormattedDateOfBirth(request.getDate_of_birth());
            foundUser.setDate_of_birth(date);
        }
        foundUser.setDate_updated(LocalDateTime.now().withNano(0));

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

    private LocalDate getFormattedDateOfBirth(String dateOfBirth) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(dateOfBirth, formatter);
    }

}
