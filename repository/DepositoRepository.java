package com.deposito.gamasonic.repository;

import com.deposito.gamasonic.entity.Deposito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepositoRepository extends JpaRepository<Deposito, Long> {

    Optional<Deposito> findByCodigo(String codigo);

    List<Deposito> findByActivoTrue();

    List<Deposito> findByEsPrincipalTrue();

    boolean existsByCodigo(String codigo);

    List<Deposito> findByCiudad(String ciudad);

    List<Deposito> findByNombreContainingIgnoreCase(String nombre);
}