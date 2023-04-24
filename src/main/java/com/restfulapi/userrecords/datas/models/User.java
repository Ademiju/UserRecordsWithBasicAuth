package com.restfulapi.userrecords.datas.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.cglib.core.GeneratorStrategy;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstname;
    private String lastname;
    private Gender gender;
    private LocalDate dateOfBirth;
    private int age;
    private String token;
    private LocalDateTime dateCreated;
    private LocalDateTime dateUpdated;

}
