package com.example.JPAdemo.Controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @GetMapping("/test")
    public String test() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("User roles: " + auth.getAuthorities());  // Should show [ROLE_DOCTOR]
        return "Employee endpoint";
    }
}
