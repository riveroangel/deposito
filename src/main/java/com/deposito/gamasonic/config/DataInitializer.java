package com.deposito.gamasonic.config;

import com.deposito.gamasonic.entity.Rol;
import com.deposito.gamasonic.entity.Usuario;
import com.deposito.gamasonic.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Crear usuario admin si no existe
        if (usuarioRepository.findByUsername("admin").isEmpty()) {
            Usuario admin = new Usuario();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRol(Rol.ADMIN);
            admin.setEnabled(true);
            usuarioRepository.save(admin);
            System.out.println("=== USUARIO ADMIN CREADO ===");
            System.out.println("Username: admin");
            System.out.println("Password: admin123");
            System.out.println("=============================");
        }

        // Crear usuario operador si no existe
        if (usuarioRepository.findByUsername("operador").isEmpty()) {
            Usuario operador = new Usuario();
            operador.setUsername("operador");
            operador.setPassword(passwordEncoder.encode("operador123"));
            operador.setRol(Rol.OPERADOR);
            operador.setEnabled(true);
            usuarioRepository.save(operador);
            System.out.println("=== USUARIO OPERADOR CREADO ===");
            System.out.println("Username: operador");
            System.out.println("Password: operador123");
            System.out.println("===============================");
        }

        System.out.println("Total usuarios en BD: " + usuarioRepository.count());
    }
}