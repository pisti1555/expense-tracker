package hu.projects.expense_tracker.features.auth.services;

import hu.projects.expense_tracker.features.auth.dtos.LoginDto;
import hu.projects.expense_tracker.features.auth.dtos.RegistrationDto;
import hu.projects.expense_tracker.features.auth.dtos.UserWithTokenDto;

public interface AuthService {
    UserWithTokenDto register(RegistrationDto dto);
    String authenticate(LoginDto dto);
}
