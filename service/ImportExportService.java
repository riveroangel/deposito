package com.deposito.gamasonic.service;/*

import com.deposito.gamasonic.dto.ProductoCreateDTO;
import com.deposito.gamasonic.entity.Producto;
import com.deposito.gamasonic.repository.ProductoRepository;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class ImportExportService {

    private final ProductoService productoService;
    private final ProductoRepository productoRepository;

    public ImportExportService(ProductoService productoService,
                               ProductoRepository productoRepository) {
        this.productoService = productoService;
        this.productoRepository = productoRepository;
    }

    // ============ IMPORTAR DESDE CSV ============
    public Map<String, Object> importarDesdeCSV(MultipartFile archivo) throws IOException {
        Map<String, Object> resultado = new HashMap<>();
        List<ProductoCreateDTO> productosImportados = new ArrayList<>();
        List<String> errores = new ArrayList<>();
        int linea = 1;

        try (CSVReader reader = new CSVReader(
                new InputStreamReader(archivo.getInputStream(), StandardCharsets.UTF_8))) {

            String[] encabezados = reader.readNext(); // Saltar encabezados
            if (encabezados == null) {
                errores.add("El archivo CSV está vacío");
                resultado.put("errores", errores);
                return resultado;
            }

            String[] lineaActual;
            while ((lineaActual = reader.readNext()) != null) {
                linea++;
                try {
                    ProductoCreateDTO dto = parsearLineaCSV(lineaActual, linea);
                    productosImportados.add(dto);
                } catch (Exception e) {
                    errores.add("Línea " + linea + ": " + e.getMessage());
                }
            }
        } catch (CsvValidationException e) {
            errores.add("Error de validación CSV: " + e.getMessage());
        }

        // Procesar productos válidos
        List<Producto> productosGuardados = new ArrayList<>();
        for (ProductoCreateDTO dto : productosImportados) {
            try {
                // Usar el método crear del ProductoService
                productoService.crear(dto);
                productosGuardados.add(productoRepository
                        .findByCodigoBarra(dto.getCodigoBarra()).orElse(null));
            } catch (Exception e) {
                errores.add("Error guardando producto " + dto.getCodigoBarra() + ": " + e.getMessage());
            }
        }

        resultado.put("totalLineas", linea - 1);
        resultado.put("importadosExitosos", productosGuardados.size());
        resultado.put("errores", errores);
        resultado.put("totalErrores", errores.size());

        return resultado;
    }

    private ProductoCreateDTO parsearLineaCSV(String[] campos, int linea) {
        if (campos.length < 5) {
            throw new IllegalArgumentException("Faltan campos requeridos. Se esperan al menos: código, nombre, stock, categoría, precioCompra, precioVenta");
        }

        ProductoCreateDTO dto = new ProductoCreateDTO();

        // Campos requeridos
        dto.setCodigoBarra(campos[0].trim());
        dto.setNombre(campos[1].trim());

        try {
            dto.setStockInicial(Integer.parseInt(campos[2].trim()));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Stock debe ser un número entero");
        }

        dto.setCategoria(campos[3].trim());

        try {
            dto.setPrecioCompra(new BigDecimal(campos[4].trim()));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Precio de compra debe ser un número decimal");
        }

        try {
            dto.setPrecioVenta(new BigDecimal(campos[5].trim()));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Precio de venta debe ser un número decimal");
        }

        // Campos opcionales
        if (campos.length > 6) dto.setDescripcion(campos[6].trim());
        if (campos.length > 7) dto.setMarca(campos[7].trim());
        if (campos.length > 8 && !campos[8].trim().isEmpty()) {
            try {
                dto.setStockMinimo(Integer.parseInt(campos[8].trim()));
            } catch (NumberFormatException e) {
                // Si no es número válido, se deja null
            }
        }
        if (campos.length > 9) dto.setUbicacion(campos[9].trim());

        return dto;
    }

    // ============ IMPORTAR DESDE EXCEL ============
    public Map<String, Object> importarDesdeExcel(MultipartFile archivo) throws IOException {
        Map<String, Object> resultado = new HashMap<>();
        List<ProductoCreateDTO> productosImportados = new ArrayList<>();
        List<String> errores = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(archivo.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            // Saltar encabezados (primera fila)
            if (rowIterator.hasNext()) rowIterator.next();

            int linea = 2; // Empieza desde la línea 2 (después de encabezados)

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                try {
                    ProductoCreateDTO dto = parsearFilaExcel(row, linea);
                    productosImportados.add(dto);
                } catch (Exception e) {
                    errores.add("Línea " + linea + ": " + e.getMessage());
                }
                linea++;
            }
        }

        // Procesar productos válidos
        List<Producto> productosGuardados = new ArrayList<>();
        for (ProductoCreateDTO dto : productosImportados) {
            try {
                productoService.crear(dto);
                productosGuardados.add(productoRepository
                        .findByCodigoBarra(dto.getCodigoBarra()).orElse(null));
            } catch (Exception e) {
                errores.add("Error guardando producto " + dto.getCodigoBarra() + ": " + e.getMessage());
            }
        }

        resultado.put("totalLineas", productosImportados.size() + errores.size());
        resultado.put("importadosExitosos", productosGuardados.size());
        resultado.put("errores", errores);
        resultado.put("totalErrores", errores.size());

        return resultado;
    }

    private ProductoCreateDTO parsearFilaExcel(Row row, int linea) {
        ProductoCreateDTO dto = new ProductoCreateDTO();

        // Celda 0: Código de barras
        Cell celdaCodigo = row.getCell(0);
        if (celdaCodigo == null || celdaCodigo.getStringCellValue().trim().isEmpty()) {
            throw new IllegalArgumentException("Código de barras es requerido");
        }
        dto.setCodigoBarra(celdaCodigo.getStringCellValue().trim());

        // Celda 1: Nombre
        Cell celdaNombre = row.getCell(1);
        if (celdaNombre == null || celdaNombre.getStringCellValue().trim().isEmpty()) {
            throw new IllegalArgumentException("Nombre es requerido");
        }
        dto.setNombre(celdaNombre.getStringCellValue().trim());

        // Celda 2: Stock inicial
        Cell celdaStock = row.getCell(2);
        if (celdaStock == null) {
            throw new IllegalArgumentException("Stock inicial es requerido");
        }
        try {
            dto.setStockInicial((int) celdaStock.getNumericCellValue());
        } catch (Exception e) {
            throw new IllegalArgumentException("Stock debe ser un número");
        }

        // Celda 3: Categoría
        Cell celdaCategoria = row.getCell(3);
        if (celdaCategoria == null || celdaCategoria.getStringCellValue().trim().isEmpty()) {
            throw new IllegalArgumentException("Categoría es requerida");
        }
        dto.setCategoria(celdaCategoria.getStringCellValue().trim());

        // Celda 4: Precio compra
        Cell celdaPrecioCompra = row.getCell(4);
        if (celdaPrecioCompra == null) {
            throw new IllegalArgumentException("Precio de compra es requerido");
        }
        try {
            dto.setPrecioCompra(BigDecimal.valueOf(celdaPrecioCompra.getNumericCellValue()));
        } catch (Exception e) {
            throw new IllegalArgumentException("Precio de compra debe ser un número");
        }

        // Celda 5: Precio venta
        Cell celdaPrecioVenta = row.getCell(5);
        if (celdaPrecioVenta == null) {
            throw new IllegalArgumentException("Precio de venta es requerido");
        }
        try {
            dto.setPrecioVenta(BigDecimal.valueOf(celdaPrecioVenta.getNumericCellValue()));
        } catch (Exception e) {
            throw new IllegalArgumentException("Precio de venta debe ser un número");
        }

        // Campos opcionales
        Cell celdaDescripcion = row.getCell(6);
        if (celdaDescripcion != null) {
            dto.setDescripcion(celdaDescripcion.getStringCellValue().trim());
        }

        Cell celdaMarca = row.getCell(7);
        if (celdaMarca != null) {
            dto.setMarca(celdaMarca.getStringCellValue().trim());
        }

        Cell celdaStockMinimo = row.getCell(8);
        if (celdaStockMinimo != null) {
            try {
                dto.setStockMinimo((int) celdaStockMinimo.getNumericCellValue());
            } catch (Exception e) {
                // Si no es número válido, se deja null
            }
        }

        Cell celdaUbicacion = row.getCell(9);
        if (celdaUbicacion != null) {
            dto.setUbicacion(celdaUbicacion.getStringCellValue().trim());
        }

        return dto;
    }

    // ============ EXPORTAR A CSV ============
    public byte[] exportarProductosCSV() throws IOException {
        List<Producto> productos = productoRepository.findAll();

        StringWriter writer = new StringWriter();
        CSVWriter csvWriter = new CSVWriter(writer);

        // Encabezados
        String[] encabezados = {
                "CODIGO_BARRAS",
                "NOMBRE",
                "DESCRIPCION",
                "STOCK",
                "CATEGORIA",
                "PRECIO_COMPRA",
                "PRECIO_VENTA",
                "MARCA",
                "STOCK_MINIMO",
                "UBICACION",
                "ACTIVO",
                "FECHA_CREACION",
                "FECHA_ACTUALIZACION"
        };
        csvWriter.writeNext(encabezados);

        // Datos
        for (Producto producto : productos) {
            String[] fila = {
                    producto.getCodigoBarra(),
                    producto.getNombre(),
                    producto.getDescripcion() != null ? producto.getDescripcion() : "",
                    String.valueOf(producto.getStock()),
                    producto.getCategoria() != null ? producto.getCategoria() : "",
                    producto.getPrecioCompra() != null ? producto.getPrecioCompra().toString() : "",
                    producto.getPrecioVenta() != null ? producto.getPrecioVenta().toString() : "",
                    producto.getMarca() != null ? producto.getMarca() : "",
                    producto.getStockMinimo() != null ? producto.getStockMinimo().toString() : "",
                    producto.getUbicacion() != null ? producto.getUbicacion() : "",
                    producto.isActivo() ? "SI" : "NO",
                    producto.getFechaCreacion() != null ? producto.getFechaCreacion().toString() : "",
                    producto.getFechaActualizacion() != null ? producto.getFechaActualizacion().toString() : ""
            };
            csvWriter.writeNext(fila);
        }

        csvWriter.close();
        return writer.toString().getBytes(StandardCharsets.UTF_8);
    }

    // ============ EXPORTAR A EXCEL ============
    public byte[] exportarProductosExcel() throws IOException {
        List<Producto> productos = productoRepository.findAll();

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Productos");

            // Estilo para encabezados
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            // Crear encabezados
            Row headerRow = sheet.createRow(0);
            String[] encabezados = {
                    "Código de Barras", "Nombre", "Descripción", "Stock", "Categoría",
                    "Precio Compra", "Precio Venta", "Marca", "Stock Mínimo",
                    "Ubicación", "Activo", "Fecha Creación", "Fecha Actualización"
            };

            for (int i = 0; i < encabezados.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(encabezados[i]);
                cell.setCellStyle(headerStyle);
            }

            // Llenar datos
            int rowNum = 1;
            for (Producto producto : productos) {
                Row row = sheet.createRow(rowNum++);

                row.createCell(0).setCellValue(producto.getCodigoBarra());
                row.createCell(1).setCellValue(producto.getNombre());
                row.createCell(2).setCellValue(producto.getDescripcion() != null ? producto.getDescripcion() : "");
                row.createCell(3).setCellValue(producto.getStock());
                row.createCell(4).setCellValue(producto.getCategoria() != null ? producto.getCategoria() : "");

                if (producto.getPrecioCompra() != null) {
                    row.createCell(5).setCellValue(producto.getPrecioCompra().doubleValue());
                }

                if (producto.getPrecioVenta() != null) {
                    row.createCell(6).setCellValue(producto.getPrecioVenta().doubleValue());
                }

                row.createCell(7).setCellValue(producto.getMarca() != null ? producto.getMarca() : "");

                if (producto.getStockMinimo() != null) {
                    row.createCell(8).setCellValue(producto.getStockMinimo());
                }

                row.createCell(9).setCellValue(producto.getUbicacion() != null ? producto.getUbicacion() : "");
                row.createCell(10).setCellValue(producto.isActivo() ? "SI" : "NO");

                if (producto.getFechaCreacion() != null) {
                    row.createCell(11).setCellValue(producto.getFechaCreacion().toString());
                }

                if (producto.getFechaActualizacion() != null) {
                    row.createCell(12).setCellValue(producto.getFechaActualizacion().toString());
                }
            }

            // Autoajustar columnas
            for (int i = 0; i < encabezados.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Convertir a bytes
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }

    // ============ DESCARGAR PLANTILLA CSV ============
    public byte[] descargarPlantillaCSV() {
        String plantilla = """
            CODIGO_BARRAS,NOMBRE,STOCK,CATEGORIA,PRECIO_COMPRA,PRECIO_VENTA,DESCRIPCION,MARCA,STOCK_MINIMO,UBICACION
            7501234567890,Laptop Gamer Pro,50,Electrónica,1500.00,2500.00,Laptop gaming con RTX 4090,ASUS,5,Estante A-12
            7501234567891,Mouse Gaming,100,Accesorios,25.00,50.00,Mouse RGB 16000DPI,Logitech,10,Estante B-3
            7501234567892,Teclado Mecánico,30,Accesorios,100.00,200.00,Teclado mecánico RGB,Razer,5,Estante B-4
            """;

        return plantilla.getBytes(StandardCharsets.UTF_8);
    }

    // ============ DESCARGAR PLANTILLA EXCEL ============
    public byte[] descargarPlantillaExcel() throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Plantilla Productos");

            // Estilo para encabezados
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            // Encabezados
            Row headerRow = sheet.createRow(0);
            String[] encabezados = {
                    "Código de Barras*", "Nombre*", "Stock Inicial*", "Categoría*",
                    "Precio Compra*", "Precio Venta*", "Descripción", "Marca",
                    "Stock Mínimo", "Ubicación"
            };

            for (int i = 0; i < encabezados.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(encabezados[i]);
                cell.setCellStyle(headerStyle);
            }

            // Datos de ejemplo
            Row ejemplo1 = sheet.createRow(2);
            ejemplo1.createCell(0).setCellValue("7501234567890");
            ejemplo1.createCell(1).setCellValue("Laptop Gamer Pro");
            ejemplo1.createCell(2).setCellValue(50);
            ejemplo1.createCell(3).setCellValue("Electrónica");
            ejemplo1.createCell(4).setCellValue(1500.00);
            ejemplo1.createCell(5).setCellValue(2500.00);
            ejemplo1.createCell(6).setCellValue("Laptop gaming con RTX 4090");
            ejemplo1.createCell(7).setCellValue("ASUS");
            ejemplo1.createCell(8).setCellValue(5);
            ejemplo1.createCell(9).setCellValue("Estante A-12");

            Row ejemplo2 = sheet.createRow(3);
            ejemplo2.createCell(0).setCellValue("7501234567891");
            ejemplo2.createCell(1).setCellValue("Mouse Gaming");
            ejemplo2.createCell(2).setCellValue(100);
            ejemplo2.createCell(3).setCellValue("Accesorios");
            ejemplo2.createCell(4).setCellValue(25.00);
            ejemplo2.createCell(5).setCellValue(50.00);
            ejemplo2.createCell(6).setCellValue("Mouse RGB 16000DPI");
            ejemplo2.createCell(7).setCellValue("Logitech");
            ejemplo2.createCell(8).setCellValue(10);
            ejemplo2.createCell(9).setCellValue("Estante B-3");

            // Instrucciones
            Row instrucciones = sheet.createRow(5);
            Cell instruccionCell = instrucciones.createCell(0);
            instruccionCell.setCellValue("INSTRUCCIONES:");

            Row inst1 = sheet.createRow(6);
            inst1.createCell(0).setCellValue("1. Los campos marcados con * son obligatorios");

            Row inst2 = sheet.createRow(7);
            inst2.createCell(0).setCellValue("2. El código de barras debe ser único");

            Row inst3 = sheet.createRow(8);
            inst3.createCell(0).setCellValue("3. Stock y precios deben ser números");

            Row inst4 = sheet.createRow(9);
            inst4.createCell(0).setCellValue("4. No modifique la primera fila (encabezados)");

            // Autoajustar columnas
            for (int i = 0; i < encabezados.length; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }
}*/
