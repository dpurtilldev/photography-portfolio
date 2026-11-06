package com.portfolio.photographyportfolio.dto;

/**
 * DTO for user responses.
 * This is what gets sent back to the client - NO PASSWORD FIELD!
 * Ensures sensitive data never leaves the server.
 */
public record UserResponse(
        Long id,
        String firstName,
        String lastName,
        String email
) {
    /**
     * Factory method to convert User entity to UserResponse DTO
     */
    public static UserResponse fromUser(com.portfolio.photographyportfolio.model.User user) {
        return new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail()
        );
    }
}
