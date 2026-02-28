package com.deposito.gamasonic.controller;

import com.deposito.gamasonic.dto.MovimientoReporteDTO;
import com.deposito.gamasonic.service.ReporteService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/reportes")
//@PreAuthorize("hasRole('ADMIN')")
public class ReporteController {

    private final ReporteService reporteService;

    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @GetMapping("/movimientos")
    public List<MovimientoReporteDTO> generarReporteMovimientos(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
            @RequestParam(required = false) Long productoId,
            @RequestParam(required = false) String tipo) {

        return reporteService.generarReporteMovimientos(
                fechaInicio, fechaFin, productoId, tipo);
    }

    @GetMapping("/movimientos/exportar")
    public ResponseEntity<byte[]> exportarReporteMovimientos(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {

        List<MovimientoReporteDTO> reporte = reporteService.generarReporteMovimientos(
                fechaInicio, fechaFin, null, null);

        // Convertir a CSV (simplificado)
        StringBuilder csv = new StringBuilder();
        csv.append("ID,Fecha,Tipo,Cantidad,Codigo Barra,Producto,Usuario,Precio Compra,Precio Venta,Valor Total\n");

        for (MovimientoReporteDTO dto : reporte) {
            csv.append(String.format("%d,%s,%s,%d,%s,%s,%s,%.2f,%.2f,%.2f\n",
                    dto.getId(),
                    dto.getFecha(),
                    dto.getTipo(),
                    dto.getCantidad(),
                    dto.getCodigoBarra(),
                    dto.getNombreProducto(),
                    dto.getUsuario(),
                    dto.getPrecioCompra(),
                    dto.getPrecioVenta(),
                    dto.getValorTotal()));
        }

        byte[] csvBytes = csv.toString().getBytes();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=reporte_movimientos_" + LocalDate.now() + ".csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(csvBytes);
    }
}
