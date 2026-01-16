package com.deposito.gamasonic.controller;

import com.deposito.gamasonic.dto.ProveedorRequestDTO;
import com.deposito.gamasonic.dto.ProveedorResponseDTO;
import com.deposito.gamasonic.service.ProveedorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/proveedores")
//@PreAuthorize("hasRole('ADMIN')")
public class ProveedorController {

    private final ProveedorService proveedorService;

    public ProveedorController(ProveedorService proveedorService) {
        this.proveedorService = proveedorService;
    }

    @PostMapping
    public ResponseEntity<ProveedorResponseDTO> crear(@Valid @RequestBody ProveedorRequestDTO dto) {
        ProveedorResponseDTO creado = proveedorService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @GetMapping
    //@PreAuthorize("isAuthenticated()")
    public List<ProveedorResponseDTO> listar() {
        return proveedorService.listarTodos();
    }

    @GetMapping("/activos")
   // @PreAuthorize("isAuthenticated()")
    public List<ProveedorResponseDTO> listarActivos() {
        return proveedorService.listarActivos();
    }

    @GetMapping("/{id}")
  //  @PreAuthorize("isAuthenticated()")
    public ProveedorResponseDTO buscarPorId(@PathVariable Long id) {
        return proveedorService.buscarPorId(id);
    }

    @GetMapping("/codigo/{codigo}")
   // @PreAuthorize("isAuthenticated()")
    public ProveedorResponseDTO buscarPorCodigo(@PathVariable String codigo) {
        return proveedorService.buscarPorCodigo(codigo);
    }

    @PutMapping("/{id}")
    public ProveedorResponseDTO actualizar(@PathVariable Long id,
                                           @Valid @RequestBody ProveedorRequestDTO dto) {
        return proveedorService.actualizar(id, dto);
    }

    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<Void> desactivar(@PathVariable Long id) {
        proveedorService.desactivar(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/activar")
    public ResponseEntity<Void> activar(@PathVariable Long id) {
        proveedorService.activar(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/buscar")
   // @PreAuthorize("isAuthenticated()")
    public List<ProveedorResponseDTO> buscarPorNombre(@RequestParam String nombre) {
        return proveedorService.buscarPorNombre(nombre);
    }
}