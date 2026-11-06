package com.portfolio.photographyportfolio.repository;

import com.portfolio.photographyportfolio.model.User;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.dao.DataIntegrityViolationException;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Runs UserRepository against a real PostgreSQL 16 (same version as docker-compose.yml),
 * started in Docker by Testcontainers. Docker must be running.
 * Each test runs in a transaction that is rolled back afterwards.
 */
@DataJpaTest(properties = "spring.jpa.hibernate.ddl-auto=create-drop")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@DisplayName("UserRepository Tests (PostgreSQL)")
class UserRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16");

    @Autowired
    private UserRepository userRepository;

    private User jane;

    @BeforeEach
    void setUp() {
        jane = userRepository.saveAndFlush(user("Jane", "Doe", "jane@example.com"));
        userRepository.saveAndFlush(user("John", "Smith", "john@example.com"));
    }

    @Test
    @DisplayName("Saving a user assigns a generated id")
    void save_assignsId() {
        assertThat(jane.getId()).isNotNull();
        assertThat(userRepository.findById(jane.getId())).isPresent();
    }

    @Test
    @DisplayName("findByEmail returns the matching user")
    void findByEmail_found() {
        Optional<User> found = userRepository.findByEmail("jane@example.com");

        assertThat(found).isPresent();
        assertThat(found.get().getFirstName()).isEqualTo("Jane");
    }

    @Test
    @DisplayName("findByEmail returns empty for an unknown email")
    void findByEmail_notFound() {
        assertThat(userRepository.findByEmail("nobody@example.com")).isEmpty();
    }

    @Test
    @DisplayName("existsByEmail reports whether an email is registered")
    void existsByEmail() {
        assertThat(userRepository.existsByEmail("jane@example.com")).isTrue();
        assertThat(userRepository.existsByEmail("nobody@example.com")).isFalse();
    }

    @Test
    @DisplayName("The database rejects a duplicate email")
    void duplicateEmail_rejected() {
        User duplicate = user("Other", "Person", "jane@example.com");

        assertThatThrownBy(() -> userRepository.saveAndFlush(duplicate))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("A user with a blank first name is rejected")
    void blankFirstName_rejected() {
        User invalid = user("", "Doe", "blank@example.com");

        assertThatThrownBy(() -> userRepository.saveAndFlush(invalid))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    @DisplayName("First name search is partial and case-insensitive")
    void findByFirstName_ignoresCase() {
        List<User> results = userRepository.findByFirstNameContainingIgnoreCase("JA");

        assertThat(results).extracting(User::getEmail).containsExactly("jane@example.com");
    }

    @Test
    @DisplayName("Last name search is partial and case-insensitive")
    void findByLastName_ignoresCase() {
        List<User> results = userRepository.findByLastNameContainingIgnoreCase("smi");

        assertThat(results).extracting(User::getEmail).containsExactly("john@example.com");
    }

    private static User user(String firstName, String lastName, String email) {
        return User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .password("hashed-password")
                .build();
    }
}
