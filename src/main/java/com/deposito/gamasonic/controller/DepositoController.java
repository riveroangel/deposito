package com.deposito.gamasonic.controller;

import com.deposito.gamasonic.dto.DepositoRequestDTO;
import com.deposito.gamasonic.dto.DepositoResponseDTO;
import com.deposito.gamasonic.service.DepositoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/depositos")
@RequiredArgsConstructor
public class DepositoController {

    private final DepositoService depositoService;

    @PostMapping
    public ResponseEntity<DepositoResponseDTO> crear(@Valid @RequestBody DepositoRequestDTO dto) {
        DepositoResponseDTO creado = depositoService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @GetMapping
    public ResponseEntity<List<DepositoResponseDTO>> listar() {
        return ResponseEntity.ok(depositoService.listarTodos());
    }

    @GetMapping("/activos")
    public ResponseEntity<List<DepositoResponseDTO>> listarActivos() {
        return ResponseEntity.ok(depositoService.listarActivos());
    }

    @GetMapping("/principal")
    public ResponseEntity<DepositoResponseDTO> obtenerPrincipal() {
        return ResponseEntity.ok(depositoService.obtenerDepositoPrincipal());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepositoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(depositoService.buscarPorId(id));
    }

    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<DepositoResponseDTO> buscarPorCodigo(@PathVariable String codigo) {
        return ResponseEntity.ok(depositoService.buscarPorCodigo(codigo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DepositoResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody DepositoRequestDTO dto) {
        return ResponseEntity.ok(depositoService.actualizar(id, dto));
    }

    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<Void> desactivar(@PathVariable Long id) {
        depositoService.desactivar(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/activar")
    public ResponseEntity<Void> activar(@PathVariable Long id) {
        depositoService.activar(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/marcar-principal")
    public ResponseEntity<DepositoResponseDTO> marcarComoPrincipal(@PathVariable Long id) {
        return ResponseEntity.ok(depositoService.marcarComoPrincipal(id));
    }

    @GetMapping("/buscar/nombre")
    public ResponseEntity<List<DepositoResponseDTO>> buscarPorNombre(@RequestParam String nombre) {
        return ResponseEntity.ok(depositoService.buscarPorNombre(nombre));
    }

    @GetMapping("/buscar/ciudad")
    public ResponseEntity<List<DepositoResponseDTO>> buscarPorCiudad(@RequestParam String ciudad) {
        return ResponseEntity.ok(depositoService.buscarPorCiudad(ciudad));
    }

    @GetMapping("/estadisticas")
    public ResponseEntity<Map<String, Object>> obtenerEstadisticas() {
        long total = depositoService.listarTodos().size();
        long activos = depositoService.listarActivos().size();

        Map<String, Object> estadisticas = Map.of(
                "totalDepositos", total,
                "depositosActivos", activos,
                "depositosInactivos", total - activos
        );

        return ResponseEntity.ok(estadisticas);
    }
}