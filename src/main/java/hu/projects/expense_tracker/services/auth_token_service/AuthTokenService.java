package hu.projects.expense_tracker.services.auth_token_service;

import org.springframework.security.core.userdetails.UserDetails;

public interface AuthTokenService {
    String generateToken(UserDetails userDetails);
    boolean validateToken(String token, UserDetails userDetails);

    String getUsernameFromToken(String token);
}
