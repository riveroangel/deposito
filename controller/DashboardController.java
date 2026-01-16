package com.deposito.gamasonic.controller;

import com.deposito.gamasonic.service.DashboardService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/dashboard")
//@PreAuthorize("isAuthenticated()")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/estadisticas")
  //  @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR')")
    public Map<String, Object> obtenerEstadisticas() {
        return dashboardService.obtenerEstadisticas();
    }

    @GetMapping("/estadisticas/fecha")
  //  @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR')")
    public Map<String, Object> obtenerEstadisticasPorFecha(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        return dashboardService.obtenerEstadisticasPorFecha(inicio, fin);
    }
}
