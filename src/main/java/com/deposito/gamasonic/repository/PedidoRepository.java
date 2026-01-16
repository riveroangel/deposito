package com.deposito.gamasonic.repository;

import com.deposito.gamasonic.entity.EstadoPedido;
import com.deposito.gamasonic.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    Optional<Pedido> findByNumeroPedido(String numeroPedido);

    List<Pedido> findByProveedorId(Long proveedorId);

    List<Pedido> findByEstado(EstadoPedido estado);

    List<Pedido> findByFechaPedidoBetween(LocalDate inicio, LocalDate fin);

    List<Pedido> findByFechaEsperadaEntregaBetween(LocalDate inicio, LocalDate fin);

    @Query("SELECT p FROM Pedido p WHERE p.estado IN :estados")
    List<Pedido> findByEstados(@Param("estados") List<EstadoPedido> estados);

    @Query("SELECT COUNT(p) FROM Pedido p WHERE p.proveedor.id = :proveedorId")
    Long countByProveedorId(@Param("proveedorId") Long proveedorId);

    @Query("SELECT COALESCE(SUM(p.total), 0) FROM Pedido p WHERE p.proveedor.id = :proveedorId AND p.estado = 'COMPLETADO'")
    BigDecimal sumTotalCompletadoByProveedorId(@Param("proveedorId") Long proveedorId);
}