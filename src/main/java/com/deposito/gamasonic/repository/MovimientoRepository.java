package com.deposito.gamasonic.repository;

import com.deposito.gamasonic.entity.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MovimientoRepository extends JpaRepository <Movimiento, Long> {
    @Query("""
        SELECT m FROM Movimiento    m
        JOIN FETCH      m.producto
        JOIN FETCH      m.usuario
""")
    List<Movimiento> findAllWithRelations();

    // 🔥 NUEVOS MÉTODOS PARA DASHBOARD
    long countByFechaAfter(LocalDateTime fecha);

    long countByTipoAndFechaAfter(String tipo, LocalDateTime fecha);

    long countByTipoAndFechaBetween(String tipo, LocalDateTime inicio, LocalDateTime fin);

    // Consulta nativa para productos más movidos
    @Query(value = """
        SELECT p.id, p.nombre, COUNT(m.id) as total_movimientos 
        FROM movimiento m 
        JOIN producto p ON m.producto_id = p.id 
        WHERE m.fecha >= :fechaInicio 
        GROUP BY p.id, p.nombre 
        ORDER BY total_movimientos DESC 
        LIMIT :limite
        """, nativeQuery = true)
    List<Object[]> findProductosMasMovidos(
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("limite") int limite);

    // Valor total de entradas
    @Query("""
        SELECT COALESCE(SUM(m.cantidad * p.precioCompra), 0) 
        FROM Movimiento m 
        JOIN m.producto p 
        WHERE m.tipo = 'ENTRADA' 
        AND m.fecha BETWEEN :inicio AND :fin
        """)
    BigDecimal valorTotalEntradas(
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin);

    // Valor total de salidas
    @Query("""
        SELECT COALESCE(SUM(m.cantidad * p.precioVenta), 0) 
        FROM Movimiento m 
        JOIN m.producto p 
        WHERE m.tipo = 'SALIDA' 
        AND m.fecha BETWEEN :inicio AND :fin
        """)
    BigDecimal valorTotalSalidas(
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin);
    // En MovimientoRepository.java agregar:
    @Query("""
    SELECT m FROM Movimiento m 
    LEFT JOIN FETCH m.producto 
    LEFT JOIN FETCH m.usuario 
    WHERE (:productoId IS NULL OR m.producto.id = :productoId)
    AND (:tipo IS NULL OR m.tipo = :tipo)
    AND m.fecha BETWEEN :inicio AND :fin
    ORDER BY m.fecha DESC
    """)
    List<Movimiento> findForReport(
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin,
            @Param("productoId") Long productoId,
            @Param("tipo") String tipo);

    @Query(value = """
    SELECT p.nombre, SUM(m.cantidad) as total_vendido, 
           SUM(m.cantidad * p.precio_venta) as valor_total 
    FROM movimiento m 
    JOIN producto p ON m.producto_id = p.id 
    WHERE m.tipo = 'SALIDA' 
    AND m.fecha BETWEEN :inicio AND :fin 
    GROUP BY p.id, p.nombre 
    ORDER BY total_vendido DESC
    """, nativeQuery = true)
    List<Object[]> findVentasPorProducto(
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin);
}
