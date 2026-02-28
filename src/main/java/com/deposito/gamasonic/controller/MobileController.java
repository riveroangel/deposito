package com.deposito.gamasonic.controller;

import com.deposito.gamasonic.dto.MovimientoDTO;
import com.deposito.gamasonic.dto.MovimientoEntradaDTO;
import com.deposito.gamasonic.dto.MovimientoSalidaDTO;
import com.deposito.gamasonic.dto.ProductoCreateDTO;
import com.deposito.gamasonic.entity.TipoMovimiento;
import com.deposito.gamasonic.repository.ProductoRepository;
import com.deposito.gamasonic.repository.UsuarioRepository;
import com.deposito.gamasonic.service.MovimientoService;
import com.deposito.gamasonic.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Controller
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class MobileController {

    private final ProductoRepository productoRepository;
    private final MovimientoService movimientoService;
    private final UsuarioRepository usuarioRepository;

    @GetMapping("/registrar")
    public String formularioRegistro(Model model) {
        // 1. Cargamos productos y operarios para los selectores
        model.addAttribute("productos", productoRepository.findAll());
        model.addAttribute("operarios", usuarioRepository.findAll());

        // 2. Traemos los últimos 5 movimientos para el historial
        List<MovimientoDTO> ultimosMovimientos = movimientoService.listarUltimosCinco();
        model.addAttribute("historial", ultimosMovimientos);

        return "app/registro-movimiento";  // ← CORREGIDO: agregar "app/"
    }
    @RestController
    @RequestMapping("/api/productos")
    @RequiredArgsConstructor
    public class ApiProductoController {

        private final ProductoService service;

        // Este es el endpoint que llamará la App Android
        @PostMapping("/guardar")
        public ResponseEntity<?> guardarDesdeApp(@RequestBody ProductoCreateDTO dto) {
            try {
                // Reutilizamos tu lógica de negocio existente
                service.crear(dto);
                return ResponseEntity.ok("Producto guardado exitosamente");
            } catch (Exception e) {
                return ResponseEntity.badRequest().body("Error: " + e.getMessage());
            }
        }
    }

    @PostMapping("/registrar")
    public String procesarRegistro(
            @RequestParam String codigoBarra,
            @RequestParam TipoMovimiento tipo,
            @RequestParam Integer cantidad,
            @RequestParam String nombreOperario,
            Model model) {

        try {
            if (tipo == TipoMovimiento.ENTRADA) {
                MovimientoEntradaDTO dto = new MovimientoEntradaDTO(codigoBarra, cantidad);
                movimientoService.registrarEntrada(dto, nombreOperario);
            } else {
                MovimientoSalidaDTO dto = new MovimientoSalidaDTO(codigoBarra, cantidad);
                movimientoService.registrarSalida(dto, nombreOperario);
            }
            return "redirect:/app/registrar?exito=true&operario=" + nombreOperario;

        } catch (ResponseStatusException e) {
            // Si hay error, debemos recargar TODO para que la página no rompa
            model.addAttribute("productos", productoRepository.findAll());
            model.addAttribute("operarios", usuarioRepository.findAll());
            model.addAttribute("historial", movimientoService.listarUltimosCinco());
            model.addAttribute("error", e.getReason());
            return "app/registro-movimiento";  // ← CORREGIDO: agregar "app/"
        }
    }
}