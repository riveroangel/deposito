package com.deposito.gamasonic.repository;

import com.deposito.gamasonic.dto.UsuarioProductividadDTO;
import com.deposito.gamasonic.entity.Movimiento;
import com.deposito.gamasonic.entity.TipoMovimiento;
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

    @Query("""
    SELECT new com.deposito.gamasonic.dto.UsuarioProductividadDTO(u.username, COUNT(m))
    FROM Movimiento m
    JOIN m.usuario u
    WHERE m.fecha >= :inicioDia
    GROUP BY u.username
    ORDER BY COUNT(m) DESC
""")
    List<UsuarioProductividadDTO> obtenerProductividadDelDia(@Param("inicioDia") java.time.LocalDateTime inicioDia);

    // 1. Para el Historial del Celular (Automático por nombre)
    // Spring hace el JOIN solo si es necesario, pero si querés optimizar:
    @Query("SELECT m FROM Movimiento m JOIN FETCH m.producto LEFT JOIN FETCH m.usuario ORDER BY m.fecha DESC LIMIT 5")
    List<Movimiento> findTop5ByOrderByFechaDesc();

    // 🔥 NUEVOS MÉTODOS PARA DASHBOARD
    long countByFechaAfter(LocalDateTime fecha);
    long countByTipoAndFechaAfter(TipoMovimiento tipo, LocalDateTime fecha);
    // Si querés saber cuántas entradas hubo en los últimos 30 día
    long countByTipoAndFechaBetween(TipoMovimiento tipo, LocalDateTime inicio, LocalDateTime fin);

    // Consulta nativa para productos más movidos
    @Query("""
        SELECT p.id, p.nombre, COUNT(m.id) 
        FROM Movimiento m 
        JOIN m.producto p 
        WHERE m.fecha >= :fechaInicio 
        GROUP BY p.id, p.nombre 
        ORDER BY COUNT(m.id) DESC
        """)
    List<Object[]> findProductosMasMovidos(
            @Param("fechaInicio") LocalDateTime fechaInicio,
            org.springframework.data.domain.Pageable pageable);
    // Nota: Para el LIMIT en JPQL se usa Pageable

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

    // 2. Ventas por producto (Cambiado a JPQL)
    @Query("""
        SELECT p.nombre, SUM(m.cantidad), SUM(m.cantidad * p.precioVenta) 
        FROM Movimiento m 
        JOIN m.producto p 
        WHERE m.tipo = 'SALIDA' 
        AND m.fecha BETWEEN :inicio AND :fin 
        GROUP BY p.id, p.nombre 
        ORDER BY SUM(m.cantidad) DESC
        """)
    List<Object[]> findVentasPorProducto(
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin);}