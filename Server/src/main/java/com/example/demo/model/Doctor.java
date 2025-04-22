package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(name = "doctor")
public class Doctor {
    @Id
    @Column(name = "doctor_id", nullable = false, length = 14)
    private String doctorId;

    @Column(name = "first_name", nullable = false, length = 20)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 20)
    private String lastName;

    @Column(name = "username", nullable = false, length = 20)
    private String username;

    @Column(name = "doc_type", nullable = false, length = 15)
    private String docType;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "dept_id", nullable = false)
    private Department dept;

    @Column(name = "password", nullable = false, length = 78)
    private String password;

    @ColumnDefault("nextval('doctor_id_seq')")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "name")
    private String name;

}