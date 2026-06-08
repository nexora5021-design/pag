package com.co.nexora.pag.controller;

import com.co.nexora.pag.dto.ChangePasswordRequest;
import com.co.nexora.pag.dto.LoginRequest;
import com.co.nexora.pag.dto.LoginResponse;
import com.co.nexora.pag.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        if (response != null) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(401).body("Usuario o contraseña incorrectos");
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validate(@RequestHeader("Authorization") String header) {
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            if (authService.isTokenValid(token)) {
                return ResponseEntity.ok(authService.getTokenInfo(token));
            }
        }
        return ResponseEntity.status(401).body("Token invalido o expirado");
    }

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request) {
        boolean updated = authService.changePassword(request);
        if (updated) {
            return ResponseEntity.ok("Contraseña actualizada correctamente");
        }
        return ResponseEntity.status(401).body("Usuario o contraseña actual incorrectos");
    }

    @PostMapping("/encrypt")
    public ResponseEntity<String> encrypt(@RequestBody String password) {
        return ResponseEntity.ok(authService.encryptPassword(password));
    }
}
