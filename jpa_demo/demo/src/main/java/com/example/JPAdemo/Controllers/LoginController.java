package com.example.JPAdemo.Controllers;


import com.example.JPAdemo.SystemUser.SystemUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class LoginController {

    private final SystemUserService systemUserService;
    @Autowired
    public LoginController(SystemUserService  systemUserService) {
        this.systemUserService = systemUserService;
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest loginRequest)
    {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        return systemUserService.login(username, password);
       // return "success";
    }
    @GetMapping("/test")
    public String doctorEndpoint() {
        return "This is a doctor-only endpoint";
    }
    static class LoginRequest {
        private String username;
        private String password;

        // Getter and Setter
        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }
    }

}
