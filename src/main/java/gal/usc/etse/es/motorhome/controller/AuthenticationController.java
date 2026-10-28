package gal.usc.etse.es.motorhome.controller;

import gal.usc.etse.es.motorhome.model.dto.LoginRequest;
import gal.usc.etse.es.motorhome.model.dto.User;
import gal.usc.etse.es.motorhome.service.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public User login(@Valid @RequestBody LoginRequest request) {
        return authenticationService.login(request);
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public java.util.Map<String, String> invalidCredentials() {
        return java.util.Map.of("error", "Credenciales incorrectas");
    }
}
