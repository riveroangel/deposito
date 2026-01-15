package com.deposito.gamasonic.service;

import com.deposito.gamasonic.dto.MovimientoReporteDTO;
import com.deposito.gamasonic.repository.MovimientoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReporteService {

    private final MovimientoRepository movimientoRepository;

    public ReporteService(MovimientoRepository movimientoRepository) {
        this.movimientoRepository = movimientoRepository;
    }

    @Transactional(readOnly = true)
    public List<MovimientoReporteDTO> generarReporteMovimientos(
            LocalDate fechaInicio,
            LocalDate fechaFin,
            Long productoId,
            String tipoMovimiento) {

        LocalDateTime inicio = fechaInicio.atStartOfDay();
        LocalDateTime fin = fechaFin.atTime(23, 59, 59);

        return movimientoRepository.findForReport(inicio, fin, productoId, tipoMovimiento)
                .stream()
                .map(m -> new MovimientoReporteDTO(
                        m.getId(),
                        m.getFecha(),
                        m.getTipo().name(),
                        m.getCantidad(),
                        m.getProducto().getCodigoBarra(),
                        m.getProducto().getNombre(),
                        m.getUsuario() != null ? m.getUsuario().getUsername() : "SISTEMA",
                        m.getProducto().getPrecioCompra(),
                        m.getProducto().getPrecioVenta()
                ))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<Object[]> generarReporteVentasPorProducto(
            LocalDate fechaInicio,
            LocalDate fechaFin) {

        LocalDateTime inicio = fechaInicio.atStartOfDay();
        LocalDateTime fin = fechaFin.atTime(23, 59, 59);

        return movimientoRepository.findVentasPorProducto(inicio, fin);
    }
}
