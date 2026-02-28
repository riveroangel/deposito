package com.deposito.gamasonic.repository;

import com.deposito.gamasonic.entity.CategoriaProducto;
import com.deposito.gamasonic.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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
    List<Producto> findByCategoria(CategoriaProducto categoria);  // ← ENUM

    // 🔥 NUEVO: Buscar activos
    List<Producto> findByActivoTrue();

    // 🔥 NUEVO: Buscar por marca
    List<Producto> findByMarca(String marca);
    //
    long countByActivoTrue();
    long countByActivoFalse();

    // Calcula el valor total del inventario (Precio Compra * Stock)
    @Query("SELECT SUM(p.precioCompra * p.stock) FROM Producto p WHERE p.activo = true")
    java.math.BigDecimal calcularValorInventarioTotal();

    // Cuenta productos que están por debajo de su stock mínimo
    @Query("SELECT COUNT(p) FROM Producto p WHERE p.stock < p.stockMinimo AND p.activo = true")
    long countProductosBajoStock();
}