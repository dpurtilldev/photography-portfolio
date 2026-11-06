package com.portfolio.photographyportfolio.service;

import com.portfolio.photographyportfolio.model.User;
import com.portfolio.photographyportfolio.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public User registerUser(User user) {
        log.info("Attempting to register user: {}", user.getEmail());

        //check to see if the user is already in the DB
        if(userRepository.existsByEmail(user.getEmail())) {
            log.error("User {} has already been registered", user.getEmail());
            throw new RuntimeException("User with email already exists");
        }

        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);

        User savedUser = userRepository.save(user);
        log.info("User {} has been saved with ID", savedUser.getId());

        return savedUser;
    }



}
