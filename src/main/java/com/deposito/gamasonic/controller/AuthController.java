package com.deposito.gamasonic.controller;
/*
import com.deposito.gamasonic.dto.LoginRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final HttpSession httpSession;  // ← Inyectar HttpSession

    public AuthController(AuthenticationManager authenticationManager,
                          HttpSession httpSession) {
        this.authenticationManager = authenticationManager;
        this.httpSession = httpSession;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            System.out.println("=== LOGIN ===");
            System.out.println("Usuario: " + request.getUsername());

            // Autenticar
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            // Establecer en SecurityContext
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Guardar en sesión HTTP
            httpSession.setAttribute(
                    HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                    SecurityContextHolder.getContext()
            );

            System.out.println("Login exitoso: " + authentication.getName());
            System.out.println("Session ID: " + httpSession.getId());
            System.out.println("Roles: " + authentication.getAuthorities());

            // Crear respuesta con información de sesión
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Login exitoso");
            response.put("username", authentication.getName());
            response.put("roles", authentication.getAuthorities());
            response.put("sessionId", httpSession.getId());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.out.println("Login fallido: " + e.getMessage());
            return ResponseEntity.status(401)
                    .body(Map.of("error", "Credenciales inválidas", "details", e.getMessage()));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        httpSession.invalidate();
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("Logout exitoso");
    }

    @GetMapping("/check")
    public ResponseEntity<?> checkAuth(HttpSession session) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Map<String, Object> response = new HashMap<>();
        response.put("authenticated", auth != null && auth.isAuthenticated());
        response.put("username", auth != null ? auth.getName() : "null");
        response.put("sessionId", session.getId());
        response.put("sessionNew", session.isNew());

        if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
            response.put("message", "Usuario autenticado");
            response.put("roles", auth.getAuthorities());
            return ResponseEntity.ok(response);
        }

        response.put("message", "No autenticado");
        return ResponseEntity.status(401).body(response);
    }
}*/