package hu.projects.expense_tracker.features.auth.services;

import hu.projects.expense_tracker.common.exceptions.BadRequestException;
import hu.projects.expense_tracker.common.exceptions.UnauthorizedException;
import hu.projects.expense_tracker.common.exceptions.UnexpectedException;
import hu.projects.expense_tracker.features.auth.dtos.LoginDto;
import hu.projects.expense_tracker.features.auth.dtos.RegistrationDto;
import hu.projects.expense_tracker.features.auth.entities.AppAuthority;
import hu.projects.expense_tracker.features.auth.repositories.AuthorityRepository;
import hu.projects.expense_tracker.features.users.entities.User;
import hu.projects.expense_tracker.features.users.repositories.UserRepository;
import hu.projects.expense_tracker.services.auth_token_service.AuthTokenService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private AuthorityRepository authorityRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private AuthTokenService authTokenService;

    @InjectMocks
    private AuthServiceImpl authService;

    private User user;
    private static final AppAuthority USER_AUTHORITY = new AppAuthority();

    @BeforeAll
    static void beforeAll() {
        USER_AUTHORITY.setAuthority("ROLE_USER");
    }

    @BeforeEach
    void setUp() {
        user = new User("test-user", "password");
        user.setId(1L);
        user.setAuthorities(Set.of(USER_AUTHORITY));
        user.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void register_ShouldPass() {
        // Arrange
        var dto = new RegistrationDto(user.getUsername(), user.getPassword());

        when(authorityRepository.findByAuthority(USER_AUTHORITY.getAuthority())).thenReturn(Optional.of(USER_AUTHORITY));
        when(passwordEncoder.encode(dto.password())).thenReturn("encoded-password");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(authTokenService.generateToken(any(User.class))).thenReturn("jwt-token");

        // Act
        var result = authService.register(dto);

        // Assert
        assertEquals(user.getUsername(), result.user().username());
        assertEquals("jwt-token", result.token());

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void register_WhenUsernameAlreadyExists_ShouldThrowBadRequestException() {
        // Arrange
        var dto = new RegistrationDto(user.getUsername(), user.getPassword());

        when(authorityRepository.findByAuthority(USER_AUTHORITY.getAuthority())).thenReturn(Optional.of(USER_AUTHORITY));
        when(userRepository.existsByUsername(user.getUsername())).thenReturn(true);

        // Act & Assert
        assertThrows(BadRequestException.class, () -> authService.register(dto));

        verify(userRepository, never()).save(any(User.class));
        verifyNoInteractions(passwordEncoder);
        verifyNoInteractions(authTokenService);
    }

    @Test
    void register_WhenAuthorityIsMissing_ShouldThrowUnexpectedException() {
        // Arrange
        var dto = new RegistrationDto(user.getUsername(), user.getPassword());

        when(authorityRepository.findByAuthority(USER_AUTHORITY.getAuthority())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UnexpectedException.class, () -> authService.register(dto));

        verify(userRepository, never()).save(any());
        verify(authTokenService, never()).generateToken(any());
    }

    @Test
    void authenticate_ShouldPass() {
        // Arrange
        var dto = new LoginDto(user.getUsername(), user.getPassword());

        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(authTokenService.generateToken(user)).thenReturn("jwt-token");

        // Act
        var token = authService.authenticate(dto);

        // Assert
        assertEquals("jwt-token", token);
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void authenticate_WhenInvalidCredentials_ShouldThrowUnauthorizedException() {
        // Arrange
        var dto = new LoginDto(user.getUsername(), user.getPassword());

        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Test error message"));

        // Act & Assert
        assertThrows(UnauthorizedException.class, () -> authService.authenticate(dto));

        verifyNoInteractions(userRepository);
        verifyNoInteractions(authTokenService);
    }

    @Test
    void authenticate_WhenUserDoesNotExists_ShouldThrowUnexpectedException() {
        // Arrange
        var dto = new LoginDto(user.getUsername(), user.getPassword());

        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UnexpectedException.class, () -> authService.authenticate(dto));

        verifyNoInteractions(authTokenService);
    }
}