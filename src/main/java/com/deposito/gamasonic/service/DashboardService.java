package com.deposito.gamasonic.service;

import com.deposito.gamasonic.repository.ProductoRepository;
import com.deposito.gamasonic.repository.MovimientoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    private final ProductoRepository productoRepository;
    private final MovimientoRepository movimientoRepository;

    public DashboardService(ProductoRepository productoRepository,
                            MovimientoRepository movimientoRepository) {
        this.productoRepository = productoRepository;
        this.movimientoRepository = movimientoRepository;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> obtenerEstadisticas() {
        Map<String, Object> stats = new HashMap<>();

        // Estadísticas de productos
        stats.put("totalProductos", productoRepository.count());

        // Contar productos activos/inactivos (si tienes estos métodos)
        try {
            stats.put("productosActivos", productoRepository.countByActivoTrue());
            stats.put("productosInactivos", productoRepository.countByActivoFalse());
        } catch (Exception e) {
            // Si no existen los métodos, usar alternativa
            long activos = productoRepository.findAll().stream()
                    .filter(p -> p.isActivo())
                    .count();
            stats.put("productosActivos", activos);
            stats.put("productosInactivos", productoRepository.count() - activos);
        }

        // Productos con stock bajo
        long productosBajoStock = productoRepository.findAll()
                .stream()
                .filter(p -> p.getStockMinimo() != null && p.getStock() < p.getStockMinimo())
                .count();
        stats.put("productosBajoStock", productosBajoStock);

        // Valor total del inventario
        BigDecimal valorInventario = productoRepository.findAll()
                .stream()
                .map(p -> {
                    if (p.getPrecioCompra() != null && p.getStock() > 0) {
                        return p.getPrecioCompra().multiply(BigDecimal.valueOf(p.getStock()));
                    }
                    return BigDecimal.ZERO;
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        stats.put("valorInventario", valorInventario);

        // Estadísticas de movimientos (últimos 30 días)
        LocalDateTime hace30Dias = LocalDateTime.now().minusDays(30);
        stats.put("totalMovimientos30Dias",
                movimientoRepository.countByFechaAfter(hace30Dias));

        // Contar entradas y salidas
        long entradas = movimientoRepository.findAll()
                .stream()
                .filter(m -> m.getFecha().isAfter(hace30Dias))
                .filter(m -> m.getTipo() != null && m.getTipo().name().equals("ENTRADA"))
                .count();
        stats.put("entradas30Dias", entradas);

        long salidas = movimientoRepository.findAll()
                .stream()
                .filter(m -> m.getFecha().isAfter(hace30Dias))
                .filter(m -> m.getTipo() != null && m.getTipo().name().equals("SALIDA"))
                .count();
        stats.put("salidas30Dias", salidas);

        // Productos más movidos (últimos 30 días)
        try {
            List<Object[]> productosMasMovidos = movimientoRepository
                    .findProductosMasMovidos(hace30Dias, 10);

            List<Map<String, Object>> productosList = productosMasMovidos.stream()
                    .map(row -> {
                        Map<String, Object> productoMap = new HashMap<>();
                        productoMap.put("productoId", row[0]);
                        productoMap.put("nombre", row[1]);
                        productoMap.put("totalMovimientos", row[2]);
                        return productoMap;
                    })
                    .collect(Collectors.toList());

            stats.put("productosMasMovidos", productosList);
        } catch (Exception e) {
            // Si falla la consulta nativa, devolver lista vacía
            stats.put("productosMasMovidos", List.of());
        }

        // Productos que necesitan reposición
        List<Map<String, Object>> productosReposicion = productoRepository.findAll()
                .stream()
                .filter(p -> p.getStockMinimo() != null && p.getStock() < p.getStockMinimo())
                .map(p -> {
                    Map<String, Object> prodMap = new HashMap<>();
                    prodMap.put("id", p.getId());
                    prodMap.put("nombre", p.getNombre());
                    prodMap.put("stockActual", p.getStock());
                    prodMap.put("stockMinimo", p.getStockMinimo());
                    prodMap.put("diferencia", p.getStockMinimo() - p.getStock());
                    return prodMap;
                })
                .collect(Collectors.toList());

        stats.put("productosNecesitanReposicion", productosReposicion);
        stats.put("totalNecesitanReposicion", productosReposicion.size());

        return stats;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> obtenerEstadisticasPorFecha(LocalDate inicio, LocalDate fin) {
        Map<String, Object> stats = new HashMap<>();
        LocalDateTime inicioDateTime = inicio.atStartOfDay();
        LocalDateTime finDateTime = fin.atTime(23, 59, 59);

        // Contar movimientos por tipo usando stream (si no tienes los métodos personalizados)
        long entradas = movimientoRepository.findAll()
                .stream()
                .filter(m -> m.getFecha().isAfter(inicioDateTime) &&
                        m.getFecha().isBefore(finDateTime))
                .filter(m -> m.getTipo() != null && m.getTipo().name().equals("ENTRADA"))
                .count();

        long salidas = movimientoRepository.findAll()
                .stream()
                .filter(m -> m.getFecha().isAfter(inicioDateTime) &&
                        m.getFecha().isBefore(finDateTime))
                .filter(m -> m.getTipo() != null && m.getTipo().name().equals("SALIDA"))
                .count();

        stats.put("entradas", entradas);
        stats.put("salidas", salidas);

        // Calcular valores totales
        BigDecimal valorEntradas = movimientoRepository.findAll()
                .stream()
                .filter(m -> m.getFecha().isAfter(inicioDateTime) &&
                        m.getFecha().isBefore(finDateTime))
                .filter(m -> m.getTipo() != null && m.getTipo().name().equals("ENTRADA"))
                .filter(m -> m.getProducto() != null && m.getProducto().getPrecioCompra() != null)
                .map(m -> m.getProducto().getPrecioCompra()
                        .multiply(BigDecimal.valueOf(m.getCantidad())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal valorSalidas = movimientoRepository.findAll()
                .stream()
                .filter(m -> m.getFecha().isAfter(inicioDateTime) &&
                        m.getFecha().isBefore(finDateTime))
                .filter(m -> m.getTipo() != null && m.getTipo().name().equals("SALIDA"))
                .filter(m -> m.getProducto() != null && m.getProducto().getPrecioVenta() != null)
                .map(m -> m.getProducto().getPrecioVenta()
                        .multiply(BigDecimal.valueOf(m.getCantidad())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        stats.put("valorTotalEntradas", valorEntradas);
        stats.put("valorTotalSalidas", valorSalidas);

        // Diferencia (ganancia/pérdida estimada)
        BigDecimal diferencia = valorSalidas.subtract(valorEntradas);
        stats.put("diferencia", diferencia);

        return stats;
    }
}