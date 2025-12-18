package hu.projects.expense_tracker.features.users.dtos;

public record UserDto(
        Long id,
        String username,
        String createdAt
) {}