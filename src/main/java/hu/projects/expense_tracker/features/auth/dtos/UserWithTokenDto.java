package hu.projects.expense_tracker.features.auth.dtos;

import hu.projects.expense_tracker.features.users.dtos.UserDto;

public record UserWithTokenDto(
        UserDto user,
        String token
) {}
