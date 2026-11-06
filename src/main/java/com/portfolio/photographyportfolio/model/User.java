package com.portfolio.photographyportfolio.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank (message = "Please enter first name")
    @Column(nullable = false)
    private String firstName;

    @NotBlank (message = "Please enter last name")
    @Column(nullable = false)
    private String lastName;

    @NotBlank (message = "Please enter your email")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank (message = "Please enter a password")
    @Column(nullable = false)
    private String password;

}
