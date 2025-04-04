package com.example.JPAdemo.SystemUser;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class SystemUserService  implements UserDetailsService {
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    private final SystemUserRepo systemUserRepo;

    private final static String USER_NOT_FOUND = "user with username %s not found";

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return systemUserRepo.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                String.format(USER_NOT_FOUND, username)));
    }

    public String login(String username, String pwd) {
        UserDetails userReturned = loadUserByUsername(username);
        if (passwordEncoder.matches(pwd, userReturned.getPassword())) {
            return "successful login";
        }
        return "incorrect password";
    }
}