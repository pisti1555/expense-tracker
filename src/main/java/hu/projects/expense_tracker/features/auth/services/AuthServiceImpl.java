package hu.projects.expense_tracker.features.auth.services;

import hu.projects.expense_tracker.common.exceptions.UnauthorizedException;
import hu.projects.expense_tracker.common.exceptions.UnexpectedException;
import hu.projects.expense_tracker.features.auth.dtos.UserWithTokenDto;
import hu.projects.expense_tracker.features.auth.repositories.AuthorityRepository;
import hu.projects.expense_tracker.features.auth.dtos.LoginDto;
import hu.projects.expense_tracker.features.auth.dtos.RegistrationDto;
import hu.projects.expense_tracker.features.users.entities.User;
import hu.projects.expense_tracker.features.users.repositories.UserRepository;
import hu.projects.expense_tracker.services.auth_token_service.AuthTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final AuthTokenService authTokenService;

    @Autowired
    public AuthServiceImpl(
            UserRepository userRepository, AuthorityRepository authorityRepository,
            PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager,
            AuthTokenService authTokenService
    ) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.authTokenService = authTokenService;
    }

    @Override
    public UserWithTokenDto register(RegistrationDto dto) {
        var userAuthority = authorityRepository.findByAuthority("ROLE_USER")
                .orElseThrow(() -> new UnexpectedException("User authority not found."));

        var user = new User(dto.username(), passwordEncoder.encode(dto.password()));
        user.setAuthorities(Collections.singleton(userAuthority));

        var savedUser = userRepository.save(user);
        var token = authTokenService.generateToken(savedUser);

        return new UserWithTokenDto(User.toDto(savedUser), token);
    }

    @Override
    public String authenticate(LoginDto dto) {
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.username(), dto.password())
        );

        if (!authentication.isAuthenticated()) {
            throw new UnauthorizedException("Invalid credentials.");
        }

        var user = userRepository.findByUsername(dto.username()).orElseThrow(() -> new UnexpectedException("User not found."));
        return authTokenService.generateToken(user);
    }
}