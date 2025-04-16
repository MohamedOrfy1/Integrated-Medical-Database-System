package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;
//This File is temporary and will be deleted once the
//database is deployed and Model files will be generated
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
}