package com.deposito.gamasonic.controller;

import com.deposito.gamasonic.dto.*;
import com.deposito.gamasonic.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
//@PreAuthorize("hasRole('ADMIN')")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping
    public ResponseEntity<PedidoResponseDTO> crear(@Valid @RequestBody PedidoRequestDTO dto) {
        PedidoResponseDTO creado = pedidoService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @GetMapping
  //  @PreAuthorize("isAuthenticated()")
    public List<PedidoResponseDTO> listar() {
        return pedidoService.listarTodos();
    }

    @GetMapping("/{id}")
  //  @PreAuthorize("isAuthenticated()")
    public PedidoResponseDTO buscarPorId(@PathVariable Long id) {
        return pedidoService.buscarPorId(id);
    }

    @GetMapping("/proveedor/{proveedorId}")
  //  @PreAuthorize("isAuthenticated()")
    public List<PedidoResponseDTO> buscarPorProveedor(@PathVariable Long proveedorId) {
        return pedidoService.buscarPorProveedor(proveedorId);
    }

    @GetMapping("/estado/{estado}")
  //  @PreAuthorize("isAuthenticated()")
    public List<PedidoResponseDTO> buscarPorEstado(@PathVariable String estado) {
        return pedidoService.buscarPorEstado(estado);
    }

    @GetMapping("/pendientes")
  //  @PreAuthorize("isAuthenticated()")
    public List<PedidoResponseDTO> buscarPendientes() {
        return pedidoService.buscarPendientes();
    }

    @PostMapping("/{id}/enviar")
    public ResponseEntity<PedidoResponseDTO> enviarPedido(@PathVariable Long id) {
        PedidoResponseDTO enviado = pedidoService.enviarPedido(id);
        return ResponseEntity.ok(enviado);
    }

    @PostMapping("/{id}/recibir")
    public ResponseEntity<PedidoResponseDTO> recibirMercaderia(
            @PathVariable Long id,
            @Valid @RequestBody List<RecepcionDetalleDTO> recepcion) {
        PedidoResponseDTO recibido = pedidoService.recibirMercaderia(id, recepcion);
        return ResponseEntity.ok(recibido);
    }

    @PostMapping("/{id}/cancelar")
    public ResponseEntity<PedidoResponseDTO> cancelarPedido(@PathVariable Long id) {
        PedidoResponseDTO cancelado = pedidoService.cancelarPedido(id);
        return ResponseEntity.ok(cancelado);
    }

    // Endpoint para obtener estadísticas de pedidos
    @GetMapping("/estadisticas")
  //  @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> obtenerEstadisticas() {
        // Aquí podrías agregar lógica para estadísticas de pedidos
        return ResponseEntity.ok(
                "Estadísticas de pedidos - Funcionalidad por implementar"
        );
    }
}