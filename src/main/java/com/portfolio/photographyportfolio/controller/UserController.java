package com.portfolio.photographyportfolio.controller;


import com.portfolio.photographyportfolio.dto.UserRegistrationRequest;
import com.portfolio.photographyportfolio.dto.UserResponse;
import com.portfolio.photographyportfolio.model.User;
import com.portfolio.photographyportfolio.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/photographers")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody UserRegistrationRequest request) {
        User user = User.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email().trim().toLowerCase())
                .password(request.password())
                .build();

        User saved = userService.registerUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponse.fromUser(saved));
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        log.info("REST request to get all users");
        List<UserResponse> users = userService.findAllUsers()
                .stream()
                .map(UserResponse::fromUser)
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }
}
