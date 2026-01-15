package com.deposito.gamasonic.repository;


import com.deposito.gamasonic.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByUsername(String username);
    // El método existsByUsername se genera automáticamente
}

