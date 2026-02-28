package com.deposito.gamasonic.service;

import com.deposito.gamasonic.repository.ProductoRepository;
import com.deposito.gamasonic.repository.MovimientoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor // Esto genera el constructor automáticamente si usas Lombok
public class DashboardService {

    private final ProductoRepository productoRepository;
    private final MovimientoRepository movimientoRepository;

    @Transactional(readOnly = true)
    public Map<String, Object> obtenerEstadisticas() {

        Map<String, Object> stats = new HashMap<>();
        LocalDateTime hace30Dias = LocalDateTime.now().minusDays(30);

        // 1. Métricas de Productos (Usando ProductoRepository)
        stats.put("totalProductos", productoRepository.count());
        stats.put("productosActivos", productoRepository.countByActivoTrue());

        BigDecimal valorInv = productoRepository.calcularValorInventarioTotal();
        stats.put("valorInventario", (valorInv != null) ? valorInv : BigDecimal.ZERO);

        // 2. Métricas de Movimientos (Usando tus nuevos métodos de MovimientoRepository)
        stats.put("totalMovimientos30Dias", movimientoRepository.countByFechaAfter(hace30Dias));

        // AQUÍ VA LO QUE PREGUNTASTE:
        stats.put("entradas30Dias", movimientoRepository.countByTipoAndFechaAfter(
                com.deposito.gamasonic.entity.TipoMovimiento.ENTRADA,
                hace30Dias
        ));

        stats.put("salidas30Dias", movimientoRepository.countByTipoAndFechaAfter(
                com.deposito.gamasonic.entity.TipoMovimiento.SALIDA,
                hace30Dias
        ));
        // 3. Top 10 Productos más movidos
        try {
            // Cambiamos el 10 por PageRequest.of(0, 10)
            List<Object[]> masMovidosRaw = movimientoRepository.findProductosMasMovidos(
                    hace30Dias,
                    org.springframework.data.domain.PageRequest.of(0, 10)
            );

            stats.put("productosMasMovidos", masMovidosRaw.stream().map(row -> {
                Map<String, Object> map = new HashMap<>();
                map.put("productoId", row[0]);
                map.put("nombre", row[1]);
                map.put("cantidad", row[2]);
                return map;
            }).collect(Collectors.toList()));
        } catch (Exception e) {
            stats.put("productosMasMovidos", Collections.emptyList());
            // Es buena idea imprimir el error en consola para ver si algo falla en la query
            System.err.println("Error en productos mas movidos: " + e.getMessage());
        }

        return stats;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> obtenerEstadisticasPorFecha(LocalDate inicio, LocalDate fin) {
        Map<String, Object> stats = new HashMap<>();
        LocalDateTime start = inicio.atStartOfDay();
        LocalDateTime end = fin.atTime(23, 59, 59);

        // 🔥 Usando tus potentes consultas @Query de valores totales
        BigDecimal valorEntradas = movimientoRepository.valorTotalEntradas(start, end);
        BigDecimal valorSalidas = movimientoRepository.valorTotalSalidas(start, end);

        stats.put("valorTotalEntradas", valorEntradas);
        stats.put("valorTotalSalidas", valorSalidas);
        stats.put("balanceNeto", valorSalidas.subtract(valorEntradas));

        // 📈 Ventas por producto (Top Ventas del periodo)
        try {
            List<Object[]> ventasRaw = movimientoRepository.findVentasPorProducto(start, end);
            stats.put("detalleVentas", ventasRaw.stream().map(row -> {
                Map<String, Object> map = new HashMap<>();
                map.put("nombre", row[0]);
                map.put("cantidadVendida", row[1]);
                map.put("valorTotal", row[2]);
                return map;
            }).collect(Collectors.toList()));
        } catch (Exception e) {
            stats.put("detalleVentas", List.of());
        }

        return stats;
    }
}