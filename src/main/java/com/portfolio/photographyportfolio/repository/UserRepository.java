package com.portfolio.photographyportfolio.repository;

import com.portfolio.photographyportfolio.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    List<User> findByFirstNameContainingIgnoreCase(String firstName);
    List<User> findByLastNameContainingIgnoreCase(String lastName);
}
