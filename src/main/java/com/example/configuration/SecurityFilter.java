package com.example.configuration;

import com.example.entity.AccountSession;
import com.example.repository.AccountSessionRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@Component
@AllArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {

    private final AccountSessionRepository accountSessionRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        // header -> sessionId

        if (header == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // here <--- there is something in Authorization header

        AccountSession accountSession = accountSessionRepository.findBySessionId(header);
        if (accountSession == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // here <--- there is smth in Authorization header + such session ID exists in the DATABASE

        // KEY??
        // 1. Object -> Principal

        // UserDetails
        // UserDetailsCredentials

        // Basic Authentication
        // Authorization -> Authorize base64(username:password)

        // name -> John
        // encoding     John -> adsjhadsk -> John
        // encryption   John -> hash |
        Authentication key = new UsernamePasswordAuthenticationToken(
                accountSession, // ->>> principal
                // accountId
                // sessionId
                null,
                new ArrayList<>() // roles
        );

        SecurityContextHolder
                .getContext()
                .setAuthentication(key);

        filterChain.doFilter(request, response);
    }
}
