package com.deposito.gamasonic.controller;

import com.deposito.gamasonic.dto.ProductoCreateDTO;
import com.deposito.gamasonic.dto.ProductoDTO;
import com.deposito.gamasonic.entity.CategoriaProducto;
import com.deposito.gamasonic.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:8000")
@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService service;

    public ProductoController(ProductoService service) {
        this.service = service;
    }

    // ==============================
    // GET
    // ==============================
    @GetMapping
    public List<ProductoDTO> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public ProductoDTO buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @GetMapping("/codigo/{codigoBarra}")
    public ProductoDTO buscarPorCodigo(@PathVariable String codigoBarra) {
        return service.buscarPorCodigo(codigoBarra);
    }

    @GetMapping("/buscar")
    public List<ProductoDTO> buscarPorNombre(@RequestParam String nombre) {
        return service.buscarPorNombre(nombre);
    }

    @GetMapping("/categoria/{categoria}")
    public List<ProductoDTO> buscarPorCategoria(@PathVariable CategoriaProducto categoria) {
        return service.buscarPorCategoria(categoria);
    }

    @GetMapping("/bajo-stock")
    public List<ProductoDTO> bajoStock(@RequestParam(defaultValue = "10") int limite) {
        return service.buscarBajoStock(limite);
    }

    @GetMapping("/necesitan-reposicion")
    public List<ProductoDTO> necesitanReposicion() {
        return service.buscarNecesitanReposicion();
    }

    // ==============================
    // POST
    // ==============================
    @PostMapping
    public ResponseEntity<ProductoDTO> crear(@Valid @RequestBody ProductoCreateDTO dto) {
        ProductoDTO creado = service.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    // ==============================
    // PUT
    // ==============================
    @PutMapping("/{id}")
    public ProductoDTO actualizarPorId(
            @PathVariable Long id,
            @Valid @RequestBody ProductoCreateDTO dto) {
        return service.actualizar(id, dto);
    }

    @PutMapping("/codigo/{codigoBarra}")
    public ProductoDTO actualizarPorCodigo(
            @PathVariable String codigoBarra,
            @Valid @RequestBody ProductoCreateDTO dto) {
        return service.actualizarPorCodigo(codigoBarra, dto);
    }

    // ==============================
    // DELETE
    // ==============================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPorId(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/codigo/{codigoBarra}")
    public ResponseEntity<Void> eliminarPorCodigo(@PathVariable String codigoBarra) {
        service.eliminarPorCodigo(codigoBarra);
        return ResponseEntity.noContent().build();
    }

    // ==============================
    // PATCH / Activar / Desactivar
    // ==============================
    @PatchMapping("/{id}/activar")
    public ResponseEntity<Void> activarProducto(
            @PathVariable Long id,
            @RequestParam boolean activo) {
        service.cambiarEstado(id, activo);
        return ResponseEntity.ok().build();
    }

    // 🔥 AGREGA ESTE MÉTODO PARA DIAGNOSTICAR
    @PostMapping("/diagnostico")
    public ResponseEntity<Map<String, Object>> diagnostico(@RequestBody Map<String, Object> body) {
        try {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "ok");
            response.put("message", "Endpoint funcionando");
            response.put("body_recibido", body);
            response.put("timestamp", LocalDateTime.now());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("status", "error");
            error.put("message", e.getMessage());
            error.put("exception", e.getClass().getName());
            return ResponseEntity.status(500).body(error);
        }
    }
}

