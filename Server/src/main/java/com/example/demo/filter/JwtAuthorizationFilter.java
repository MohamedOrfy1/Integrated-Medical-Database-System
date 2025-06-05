package com.example.demo.filter;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.example.demo.service.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;


@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JWTService jwtservice;

    public JwtAuthorizationFilter(JWTService jwtservice) {
        this.jwtservice = jwtservice;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);
        if (jwtservice.isTokenValid(token)) {
            String id = jwtservice.getIDFromToken(token);
            String role = jwtservice.getRoleFromToken(token);

            Authentication auth = new UsernamePasswordAuthenticationToken(
                    id,
                    null,
                    Collections.singletonList(new SimpleGrantedAuthority(role))
            );

            SecurityContextHolder.getContext().setAuthentication(auth);
            Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();


        }

        chain.doFilter(request, response);
    }
}
