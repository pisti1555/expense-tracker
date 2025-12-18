package hu.projects.expense_tracker.features.auth.dtos;

public record LoginDto(
        String username,
        String password
) {}