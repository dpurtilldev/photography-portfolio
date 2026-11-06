package com.portfolio.photographyportfolio.service;

import com.portfolio.photographyportfolio.model.User;
import com.portfolio.photographyportfolio.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService Unit Tests")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    // Real BCrypt encoder so we can check the stored password is a valid hash
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository, passwordEncoder);
    }

    @Test
    @DisplayName("registerUser hashes the password before saving")
    void registerUser_hashesPassword() {
        User newUser = user("jane@example.com", "plain-password");
        when(userRepository.existsByEmail("jane@example.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(inv -> {
            User u = inv.getArgument(0);
            u.setId(1L);
            return u;
        });

        User saved = userService.registerUser(newUser);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        String storedPassword = captor.getValue().getPassword();

        assertThat(storedPassword).isNotEqualTo("plain-password");
        assertThat(passwordEncoder.matches("plain-password", storedPassword)).isTrue();
        assertThat(saved.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("registerUser rejects an email that is already registered")
    void registerUser_duplicateEmail() {
        when(userRepository.existsByEmail("jane@example.com")).thenReturn(true);

        assertThatThrownBy(() -> userService.registerUser(user("jane@example.com", "plain-password")))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("already exists");

        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("findUserByEmail returns the user from the repository")
    void findUserByEmail() {
        User jane = user("jane@example.com", "hash");
        when(userRepository.findByEmail("jane@example.com")).thenReturn(Optional.of(jane));

        assertThat(userService.findUserByEmail("jane@example.com")).contains(jane);
    }

    @Test
    @DisplayName("findUserByEmail returns empty when no user matches")
    void findUserByEmail_notFound() {
        when(userRepository.findByEmail("nobody@example.com")).thenReturn(Optional.empty());

        assertThat(userService.findUserByEmail("nobody@example.com")).isEmpty();
    }

    @Test
    @DisplayName("findAllUsers returns every user")
    void findAllUsers() {
        List<User> users = List.of(user("a@example.com", "h"), user("b@example.com", "h"));
        when(userRepository.findAll()).thenReturn(users);

        assertThat(userService.findAllUsers()).hasSize(2).containsExactlyElementsOf(users);
    }

    private static User user(String email, String password) {
        return User.builder()
                .firstName("Jane")
                .lastName("Doe")
                .email(email)
                .password(password)
                .build();
    }
}
