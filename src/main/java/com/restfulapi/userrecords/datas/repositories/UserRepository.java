package com.restfulapi.userrecords.datas.repositories;

import com.restfulapi.userrecords.datas.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Page<User> findByFirstnameContainingIgnoreCase(String firstname, Pageable pageable);
    List<User> findByFirstnameContainingIgnoreCase(String firstname);
    Page<User> findByLastnameContainingIgnoreCase(String lastname,Pageable pageable);
}
