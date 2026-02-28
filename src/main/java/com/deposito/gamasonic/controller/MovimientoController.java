package com.deposito.gamasonic.controller;

import com.deposito.gamasonic.dto.MovimientoDTO;
import com.deposito.gamasonic.dto.MovimientoSalidaDTO;
import com.deposito.gamasonic.dto.MovimientoEntradaDTO;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import com.deposito.gamasonic.service.MovimientoService;

import java.util.List;

@RestController
@RequestMapping("api/v1/movimientos")
public class MovimientoController {

    private final MovimientoService service;

    public MovimientoController(MovimientoService service) {
        this.service = service;
    }
    @Valid
    @PostMapping("/entrada")
   // @PreAuthorize("hasAnyRole('ADMIN','OPERADOR')")
    public MovimientoDTO entrada(
            @Valid @RequestBody MovimientoEntradaDTO dto,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return service.registrarEntrada(dto, userDetails.getUsername());
    }



    @PostMapping("/salida")
    //@PreAuthorize("hasAnyRole('ADMIN','OPERADOR')")
    public MovimientoDTO salida(
            @Valid @RequestBody MovimientoSalidaDTO dto,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return service.registrarSalida(dto, userDetails.getUsername());
    }


    @GetMapping
   // @PreAuthorize("hasRole('ADMIN')")
    public List<MovimientoDTO> listar() {
        return service.listar();
    }


}
