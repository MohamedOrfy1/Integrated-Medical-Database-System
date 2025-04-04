package com.example.JPAdemo.SystemUser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface SystemUserRepo extends JpaRepository<MyUser, Long> {
    Optional<MyUser> findByUsername(String username);
}
