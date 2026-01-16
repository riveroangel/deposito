package com.deposito.gamasonic.repository;

import com.deposito.gamasonic.entity.StockDeposito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StockDepositoRepository extends JpaRepository<StockDeposito, Long> {

    Optional<StockDeposito> findByProductoIdAndDepositoId(Long productoId, Long depositoId);

    List<StockDeposito> findByProductoId(Long productoId);

    List<StockDeposito> findByDepositoId(Long depositoId);

    List<StockDeposito> findByDepositoIdAndStockLessThan(Long depositoId, Integer stock);

    List<StockDeposito> findByProductoIdAndStockGreaterThan(Long productoId, Integer stock);

    @Query("SELECT sd FROM StockDeposito sd WHERE sd.deposito.id = :depositoId AND sd.stock < sd.stockMinimo")
    List<StockDeposito> findProductosBajoStockEnDeposito(@Param("depositoId") Long depositoId);

    @Query("SELECT COALESCE(SUM(sd.stock), 0) FROM StockDeposito sd WHERE sd.producto.id = :productoId")
    Integer getStockTotalProducto(@Param("productoId") Long productoId);

    @Query("SELECT sd FROM StockDeposito sd WHERE sd.deposito.activo = true AND sd.stock > 0")
    List<StockDeposito> findStockEnDepositosActivos();

    boolean existsByProductoIdAndDepositoId(Long productoId, Long depositoId);
}