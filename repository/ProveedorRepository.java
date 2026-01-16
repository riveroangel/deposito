package com.deposito.gamasonic.repository;

import com.deposito.gamasonic.entity.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {

    Optional<Proveedor> findByCodigo(String codigo);

    Optional<Proveedor> findByRuc(String ruc);

    boolean existsByRuc(String ruc);

    boolean existsByCodigo(String codigo);

    List<Proveedor> findByNombreContainingIgnoreCase(String nombre);

    List<Proveedor> findByActivoTrue();

    @Query("SELECT COUNT(p) FROM Pedido p WHERE p.proveedor.id = :proveedorId")
    Integer countPedidosByProveedorId(@Param("proveedorId") Long proveedorId);
}