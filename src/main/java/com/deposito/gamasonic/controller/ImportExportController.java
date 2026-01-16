package com.deposito.gamasonic.controller;
/*
import com.deposito.gamasonic.service.ImportExportService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/import-export")
//@PreAuthorize("hasRole('ADMIN')")
public class ImportExportController {

    private final ImportExportService importExportService;

    public ImportExportController(ImportExportService importExportService) {
        this.importExportService = importExportService;
    }

    // ============ IMPORTAR ============

    @PostMapping("/importar/csv")
    public ResponseEntity<Map<String, Object>> importarCSV(
            @RequestParam("archivo") MultipartFile archivo) throws IOException {

        if (archivo.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "El archivo está vacío"));
        }

        if (!archivo.getOriginalFilename().toLowerCase().endsWith(".csv")) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "El archivo debe ser CSV"));
        }

        Map<String, Object> resultado = importExportService.importarDesdeCSV(archivo);
        return ResponseEntity.ok(resultado);
    }

    @PostMapping("/importar/excel")
    public ResponseEntity<Map<String, Object>> importarExcel(
            @RequestParam("archivo") MultipartFile archivo) throws IOException {

        if (archivo.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "El archivo está vacío"));
        }

        String filename = archivo.getOriginalFilename().toLowerCase();
        if (!filename.endsWith(".xlsx") && !filename.endsWith(".xls")) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "El archivo debe ser Excel (.xlsx o .xls)"));
        }

        Map<String, Object> resultado = importExportService.importarDesdeExcel(archivo);
        return ResponseEntity.ok(resultado);
    }

    // ============ EXPORTAR ============

    @GetMapping("/exportar/csv")
    public ResponseEntity<byte[]> exportarCSV() throws IOException {
        byte[] csvBytes = importExportService.exportarProductosCSV();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=productos_" + java.time.LocalDate.now() + ".csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(csvBytes);
    }

    @GetMapping("/exportar/excel")
    public ResponseEntity<byte[]> exportarExcel() throws IOException {
        byte[] excelBytes = importExportService.exportarProductosExcel();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=productos_" + java.time.LocalDate.now() + ".xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(excelBytes);
    }

    // ============ PLANTILLAS ============

    @GetMapping("/plantilla/csv")
    public ResponseEntity<byte[]> descargarPlantillaCSV() {
        byte[] plantilla = importExportService.descargarPlantillaCSV();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=plantilla_productos.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(plantilla);
    }

    @GetMapping("/plantilla/excel")
    public ResponseEntity<byte[]> descargarPlantillaExcel() throws IOException {
        byte[] plantilla = importExportService.descargarPlantillaExcel();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=plantilla_productos.xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(plantilla);
    }

    // ============ STATUS ============

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> obtenerStatus() {
        return ResponseEntity.ok(Map.of(
                "status", "Import/Export module ready",
                "endpoints", Map.of(
                        "importCSV", "POST /api/import-export/importar/csv",
                        "importExcel", "POST /api/import-export/importar/excel",
                        "exportCSV", "GET /api/import-export/exportar/csv",
                        "exportExcel", "GET /api/import-export/exportar/excel",
                        "templateCSV", "GET /api/import-export/plantilla/csv",
                        "templateExcel", "GET /api/import-export/plantilla/excel"
                )
        ));
    }
}*/