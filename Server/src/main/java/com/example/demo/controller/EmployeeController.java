package com.example.demo.controller;

import com.example.demo.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.demo.model.Employee;

import java.util.List;
@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*")
@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    public Employee createEmployee(@RequestBody Employee employee) {
        return employeeService.createEmployee(employee);
    }

    @GetMapping
    public List<Employee> getAllEmployees() {
        return null;
    }

    @GetMapping("/{id}")
    public Employee getEmployeeById(@PathVariable Long id) {
        return null;
    }

    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable Long id) {

    }
}