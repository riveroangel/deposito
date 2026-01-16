package com.deposito.gamasonic.controller;

import com.deposito.gamasonic.dto.ProductoCreateDTO;
import com.deposito.gamasonic.dto.ProductoDTO;
import com.deposito.gamasonic.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    private final ProductoService service;

    public ProductoController(ProductoService service) {
        this.service = service;
    }

    @GetMapping
    public List<ProductoDTO> listar() {
        return service.listar();
    }

    @PostMapping
   // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductoDTO> crear(@Valid @RequestBody ProductoCreateDTO dto) {
        ProductoDTO creado = service.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @GetMapping("/{id}")
    public ProductoDTO buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @GetMapping("/codigo/{codigoBarra}")
    public ProductoDTO buscarPorCodigo(@PathVariable String codigoBarra) {
        return service.buscarPorCodigo(codigoBarra);
    }

    @PutMapping("/{id}")
    //@PreAuthorize("hasRole('ADMIN')")
    public ProductoDTO actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProductoCreateDTO dto) {
        return service.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/bajo-stock")
    public List<ProductoDTO> bajoStock(@RequestParam(defaultValue = "10") int limite) {
        return service.buscarBajoStock(limite);
    }

    // 🔥 NUEVO: Productos que necesitan reposición
    @GetMapping("/necesitan-reposicion")
    public List<ProductoDTO> necesitanReposicion() {
        return service.buscarNecesitanReposicion();
    }

    @GetMapping("/buscar")
    public List<ProductoDTO> buscarPorNombre(@RequestParam String nombre) {
        return service.buscarPorNombre(nombre);
    }

    // 🔥 NUEVO: Buscar por categoría
    @GetMapping("/categoria/{categoria}")
    public List<ProductoDTO> buscarPorCategoria(@PathVariable String categoria) {
        return service.buscarPorCategoria(categoria);
    }

    // 🔥 NUEVO: Activar/desactivar producto
    @PatchMapping("/{id}/activar")
   // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> activarProducto(@PathVariable Long id, @RequestParam boolean activo) {
        // Nota: Necesitarías implementar este método en el Service
        // service.cambiarEstado(id, activo);
        return ResponseEntity.ok().build();
    }
}