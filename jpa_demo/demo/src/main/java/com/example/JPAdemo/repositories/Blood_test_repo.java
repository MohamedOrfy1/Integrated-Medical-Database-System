package com.example.JPAdemo.repositories;

import com.example.JPAdemo.models.Blood_Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Blood_test_repo extends JpaRepository<Blood_Test, Long> {
}
