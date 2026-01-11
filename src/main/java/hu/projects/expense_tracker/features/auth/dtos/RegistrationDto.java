package hu.projects.expense_tracker.features.auth.dtos;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record RegistrationDto(
        @NotBlank(message = "Username must be provided.")
        @Length(min = 6, max = 50, message = "Username must be between 6-50 characters of length.")
        String username,

        @NotBlank(message = "Password must be provided.")
        @Length(min = 8, max = 300, message = "Password must be at least 8 characters long.")
        String password
) {}