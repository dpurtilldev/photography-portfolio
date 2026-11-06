package com.portfolio.photographyportfolio.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO for user registration requests.
 * This is what the client sends when creating a new account.
 * Separates the API request structure from the database entity.
 */
public record UserRegistrationRequest(
        
        @NotBlank(message = "First name is required")
        String firstName,
        
        @NotBlank(message = "Last name is required")
        String lastName,
        
        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        String email,
        
        @NotBlank(message = "Password is required")
        @Size(min = 8, message = "Password must be at least 8 characters")
        String password
) {
}
