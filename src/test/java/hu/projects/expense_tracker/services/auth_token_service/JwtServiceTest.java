package hu.projects.expense_tracker.services.auth_token_service;

import hu.projects.expense_tracker.configuration.properties.JwtConfigurationProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {
    @Mock
    private JwtConfigurationProperties jwtProperties;

    @InjectMocks
    private JwtService jwtService;

    private static final String SECRET_KEY = "this0is0a0very0strong0test0secret0key0which0is0long0enough";
    private static final UserDetails userDetails = User
            .withUsername("test-user")
            .password("password")
            .authorities("ROLE_USER")
            .build();

    @BeforeEach
    void setUp() {
        when(jwtProperties.getSecretKey()).thenReturn(SECRET_KEY);
        when(jwtProperties.getIssuer()).thenReturn("expense-tracker");
        when(jwtProperties.getExpirationTime()).thenReturn(1000000L);
    }

    @Test
    void generateToken_ShouldGenerateValidToken() {
        // Act
        var token = jwtService.generateToken(userDetails);

        // Assert
        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    @Test
    void validateToken_ShouldReturnTrue() {
        var token = jwtService.generateToken(userDetails);

        // Act
        var isValid = jwtService.validateToken(token, userDetails);

        // Assert
        assertTrue(isValid);
    }

    @Test
    void validateToken_whenWrongUsername_shouldReturnFalse() {
        var token = jwtService.generateToken(userDetails);
        UserDetails otherUserDetails = User
                .withUsername("other-user")
                .password("password")
                .authorities("ROLE_USER")
                .build();

        // Act
        var isValid = jwtService.validateToken(token, otherUserDetails);

        // Assert
        assertFalse(isValid);
    }

    @Test
    void validateToken_whenWrongIssuer_shouldReturnFalse() {
        var token = jwtService.generateToken(userDetails);
        when(jwtProperties.getIssuer()).thenReturn("other-issuer");

        // Act
        var isValid = jwtService.validateToken(token, userDetails);

        // Assert
        assertFalse(isValid);
    }

    @Test
    void validateToken_whenTokenIsExpired_shouldReturnFalse() {
        var token = jwtService.generateToken(userDetails);
        when(jwtProperties.getExpirationTime()).thenReturn(-1000L);

        // Act
        var isValid = jwtService.validateToken(token, userDetails);

        // Assert
        assertFalse(isValid);
    }

    @Test
    void validateToken_whenTokenDoesNotContainAnyAuthority_shouldReturnFalse() {
        var userDetailsWithoutAuthorities = new User("test-user", "password", List.of());
        var token = jwtService.generateToken(userDetailsWithoutAuthorities);

        // Act
        var isValid = jwtService.validateToken(token, userDetails);

        // Assert
        assertFalse(isValid);
    }

    @Test
    void getUsernameFromToken_shouldReturnUsername() {
        var token = jwtService.generateToken(userDetails);

        // Act
        var username = jwtService.getUsernameFromToken(token);

        // Assert
        assertEquals("test-user", username);
    }
}