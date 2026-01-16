package com.deposito.gamasonic.controller;

import com.deposito.gamasonic.dto.*;
import com.deposito.gamasonic.service.StockDepositoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stock-deposito")
//@PreAuthorize("hasRole('ADMIN')")
public class StockDepositoController {

    private final StockDepositoService stockDepositoService;

    public StockDepositoController(StockDepositoService stockDepositoService) {
        this.stockDepositoService = stockDepositoService;
    }

    @PostMapping
    public ResponseEntity<StockDepositoResponseDTO> crearOActualizar(
            @Valid @RequestBody StockDepositoRequestDTO dto) {
        StockDepositoResponseDTO creado = stockDepositoService.crearOActualizar(dto);
        return ResponseEntity.ok(creado);
    }

    @PostMapping("/ajustar")
    public ResponseEntity<StockDepositoResponseDTO> ajustarStock(
            @Valid @RequestBody AjusteStockDTO dto) {
        // En un caso real, obtendrías el username del contexto de seguridad
        String username = "admin";
        StockDepositoResponseDTO ajustado = stockDepositoService.ajustarStock(dto, username);
        return ResponseEntity.ok(ajustado);
    }

    @PostMapping("/transferir")
    public ResponseEntity<StockDepositoResponseDTO> transferirStock(
            @RequestParam Long productoId,
            @RequestParam Long origenId,
            @RequestParam Long destinoId,
            @RequestParam Integer cantidad) {
        // En un caso real, obtendrías el username del contexto de seguridad
        String username = "admin";
        StockDepositoResponseDTO transferido = stockDepositoService.transferirStock(
                productoId, origenId, destinoId, cantidad, username);
        return ResponseEntity.ok(transferido);
    }

    @GetMapping("/{id}")
   // @PreAuthorize("isAuthenticated()")
    public StockDepositoResponseDTO buscarPorId(@PathVariable Long id) {
        return stockDepositoService.buscarPorId(id);
    }

    @GetMapping("/producto/{productoId}/deposito/{depositoId}")
  //  @PreAuthorize("isAuthenticated()")
    public StockDepositoResponseDTO buscarPorProductoYDeposito(
            @PathVariable Long productoId,
            @PathVariable Long depositoId) {
        return stockDepositoService.buscarPorProductoYDeposito(productoId, depositoId);
    }

    @GetMapping("/producto/{productoId}")
  //  @PreAuthorize("isAuthenticated()")
    public List<StockDepositoResponseDTO> buscarPorProducto(@PathVariable Long productoId) {
        return stockDepositoService.buscarPorProducto(productoId);
    }

    @GetMapping("/deposito/{depositoId}")
 //   @PreAuthorize("isAuthenticated()")
    public List<StockDepositoResponseDTO> buscarPorDeposito(@PathVariable Long depositoId) {
        return stockDepositoService.buscarPorDeposito(depositoId);
    }

    @GetMapping("/deposito/{depositoId}/bajo-stock")
 //   @PreAuthorize("isAuthenticated()")
    public List<StockDepositoResponseDTO> buscarBajoStockEnDeposito(@PathVariable Long depositoId) {
        return stockDepositoService.buscarBajoStockEnDeposito(depositoId);
    }

    @GetMapping("/producto/{productoId}/total")
  //  @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Integer> getStockTotalProducto(@PathVariable Long productoId) {
        Integer total = stockDepositoService.getStockTotalProducto(productoId);
        return ResponseEntity.ok(total);
    }

    @GetMapping("/disponibilidad/{productoId}")
  //  @PreAuthorize("isAuthenticated()")
    public List<StockDepositoResponseDTO> buscarDisponibilidadProducto(
            @PathVariable Long productoId,
            @RequestParam(defaultValue = "1") Integer cantidadMinima) {
        return stockDepositoService.buscarDisponibilidadProducto(productoId, cantidadMinima);
    }

    @GetMapping("/todos")
 //   @PreAuthorize("isAuthenticated()")
    public List<StockDepositoResponseDTO> buscarStockEnTodosDepositos() {
        return stockDepositoService.buscarStockEnTodosDepositos();
    }
}