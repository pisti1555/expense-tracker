package hu.projects.expense_tracker.features.auth.dtos;

public record RegistrationDto(
        String username,
        String password
) {}