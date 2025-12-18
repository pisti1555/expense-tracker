package hu.projects.expense_tracker.features.auth.controllers;

import hu.projects.expense_tracker.features.auth.dtos.LoginDto;
import hu.projects.expense_tracker.features.auth.dtos.RegistrationDto;
import hu.projects.expense_tracker.features.auth.dtos.TokenDto;
import hu.projects.expense_tracker.features.auth.dtos.UserWithTokenDto;
import hu.projects.expense_tracker.features.auth.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserWithTokenDto> register(@RequestBody RegistrationDto dto) {
        var registeredUser = authService.register(dto);
        return ResponseEntity.created(URI.create("/api/users/" + registeredUser.user().id())).body(registeredUser);
    }

    @PostMapping("/login")
    public TokenDto authenticate(@RequestBody LoginDto dto) {
        var token = authService.authenticate(dto);
        return new TokenDto(token);
    }
}
