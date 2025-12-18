package hu.projects.expense_tracker.features.auth.services;

import hu.projects.expense_tracker.features.auth.dtos.LoginDto;
import hu.projects.expense_tracker.features.auth.dtos.RegistrationDto;
import hu.projects.expense_tracker.features.users.dtos.UserDto;

public interface AuthService {
    UserDto register(RegistrationDto dto);
    String authenticate(LoginDto dto);
}
