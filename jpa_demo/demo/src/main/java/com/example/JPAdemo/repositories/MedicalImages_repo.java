package com.example.JPAdemo.repositories;

import com.example.JPAdemo.models.Medical_Images;
import com.example.JPAdemo.models.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicalImages_repo extends JpaRepository<Medical_Images, Long> {

}
