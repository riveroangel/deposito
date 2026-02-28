package com.deposito.gamasonic.controller;

import com.deposito.gamasonic.repository.DepositoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/info")
@RequiredArgsConstructor
public class InfoController {

    private final DepositoRepository depositoRepository;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getInfo() {
        Map<String, Object> info = new HashMap<>();

        info.put("application", "Gamasonic - Sistema de Gestión de Depósitos");
        info.put("status", "running");
        info.put("timestamp", java.time.LocalDateTime.now());
        info.put("totalDepositos", depositoRepository.count());
        info.put("depositosActivos", depositoRepository.findByActivoTrue().size());
        info.put("depositoPrincipal", depositoRepository.findByEsPrincipalTrue()
                .stream()
                .findFirst()
                .map(d -> Map.of(
                        "id", d.getId(),
                        "codigo", d.getCodigo(),
                        "nombre", d.getNombre()
                ))
                .orElse(null));

        return ResponseEntity.ok(info);
    }
}