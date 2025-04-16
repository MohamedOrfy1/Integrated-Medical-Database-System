package com.example.JPAdemo.repositories;

import com.example.JPAdemo.models.AttributeId;
import com.example.JPAdemo.models.Test_attributes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestAttributes_repo extends JpaRepository<Test_attributes, AttributeId> {
}
