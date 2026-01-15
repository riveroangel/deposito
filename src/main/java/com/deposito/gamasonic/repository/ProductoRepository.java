package com.deposito.gamasonic.repository;

import com.deposito.gamasonic.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    Optional<Producto> findByCodigoBarra(String codigoBarra);

    boolean existsByCodigoBarra(String codigoBarra);

    List<Producto> findByStockLessThan(int stock);

    List<Producto> findByNombreContainingIgnoreCase(String nombre);

    // 🔥 NUEVO: Buscar por categoría
    List<Producto> findByCategoria(String categoria);

    // 🔥 NUEVO: Buscar activos
    List<Producto> findByActivoTrue();

    // 🔥 NUEVO: Buscar por marca
    List<Producto> findByMarca(String marca);
    //
    long countByActivoTrue();
    long countByActivoFalse();
}