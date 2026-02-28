package com.deposito.gamasonic.controller;

import com.deposito.gamasonic.dto.ProductoCreateDTO;
import com.deposito.gamasonic.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/productos")
@RequiredArgsConstructor
public class ProductoViewController {

    private final ProductoService service;

    @GetMapping("/nuevo")
    public String pantallaAlta(Model model) {
        return "productos/alta-productos";  // ← Así debe ser: con "s" y con carpeta
    }

    @PostMapping("/guardar")
    public String guardarProducto(@ModelAttribute ProductoCreateDTO dto, RedirectAttributes redirect) {
        try {
            service.crear(dto);
            redirect.addFlashAttribute("exito", "Producto guardado con éxito.");
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Error al guardar: " + e.getMessage());
        }
        return "redirect:/productos/nuevo";
    }
}