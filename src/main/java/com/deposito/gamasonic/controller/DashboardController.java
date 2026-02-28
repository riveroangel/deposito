package com.deposito.gamasonic.controller;

import com.deposito.gamasonic.service.DashboardService;
import com.deposito.gamasonic.service.MovimientoService;
import com.deposito.gamasonic.dto.UsuarioProductividadDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller; // Correcto para devolver HTML
import org.springframework.ui.Model; // Necesario para pasar datos al HTML
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;
    private final MovimientoService movimientoService;

    // 1. ESTO DEVUELVE LA PÁGINA HTML (Vista)
    @GetMapping("/ver")
    public String verPaginaDashboard(Model model) {
        // Pasamos el ranking directamente a la página
        model.addAttribute("ranking", movimientoService.obtenerRankingHoy());
        return "productos/dashboard-productividad"; // Nombre del archivo .html
    }

    // 2. ESTO SIGUE DEVOLVIENDO DATOS (JSON) para gráficos o auditoría
    @GetMapping("/estadisticas")
    @ResponseBody // <--- Obligatorio para que no busque un HTML
    public Map<String, Object> obtenerEstadisticas() {
        return dashboardService.obtenerEstadisticas();
    }

    @GetMapping("/productividad-datos")
    @ResponseBody // <--- Devuelve la lista como JSON
    public List<UsuarioProductividadDTO> obtenerRanking() {
        return movimientoService.obtenerRankingHoy();
    }
}