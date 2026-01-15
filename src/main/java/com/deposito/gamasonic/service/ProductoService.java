package com.deposito.gamasonic.service;

import com.deposito.gamasonic.dto.ProductoCreateDTO;
import com.deposito.gamasonic.dto.ProductoDTO;
import com.deposito.gamasonic.entity.Producto;
import com.deposito.gamasonic.exception.DuplicadoException;
import com.deposito.gamasonic.exception.ProductoNoEncontradoException;
import com.deposito.gamasonic.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository productoRepo;

    public ProductoService(ProductoRepository productoRepo) {
        this.productoRepo = productoRepo;
    }

    @Transactional
    public ProductoDTO crear(ProductoCreateDTO dto) {
        // Validar código único
        if (productoRepo.existsByCodigoBarra(dto.getCodigoBarra())) {
            throw new DuplicadoException("Ya existe un producto con el código de barras: " + dto.getCodigoBarra());
        }

        // 🔥 CREAR PRODUCTO CON TODOS LOS CAMPOS NUEVOS
        Producto producto = new Producto();
        producto.setCodigoBarra(dto.getCodigoBarra());
        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setStock(dto.getStockInicial());
        producto.setCategoria(dto.getCategoria());
        producto.setPrecioCompra(dto.getPrecioCompra());
        producto.setPrecioVenta(dto.getPrecioVenta());
        producto.setMarca(dto.getMarca());
        producto.setStockMinimo(dto.getStockMinimo());
        producto.setUbicacion(dto.getUbicacion());
        // activo y fechas se setean automáticamente con @PrePersist

        Producto saved = productoRepo.save(producto);
        return toDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<ProductoDTO> listar() {
        return productoRepo.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProductoDTO buscarPorId(Long id) {
        Producto producto = productoRepo.findById(id)
                .orElseThrow(() -> new ProductoNoEncontradoException("Producto no encontrado con ID: " + id));
        return toDTO(producto);
    }

    @Transactional(readOnly = true)
    public ProductoDTO buscarPorCodigo(String codigoBarra) {
        Producto producto = productoRepo.findByCodigoBarra(codigoBarra)
                .orElseThrow(() -> new ProductoNoEncontradoException(
                        "Producto no encontrado con código: " + codigoBarra));
        return toDTO(producto);
    }

    @Transactional
    public ProductoDTO actualizar(Long id, ProductoCreateDTO dto) {
        Producto producto = productoRepo.findById(id)
                .orElseThrow(() -> new ProductoNoEncontradoException("Producto no encontrado con ID: " + id));

        // Validar que el nuevo código no esté duplicado (si cambió)
        if (!producto.getCodigoBarra().equals(dto.getCodigoBarra()) &&
                productoRepo.existsByCodigoBarra(dto.getCodigoBarra())) {
            throw new DuplicadoException("Ya existe un producto con código: " + dto.getCodigoBarra());
        }

        // 🔥 ACTUALIZAR TODOS LOS CAMPOS
        producto.setCodigoBarra(dto.getCodigoBarra());
        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setStock(dto.getStockInicial());
        producto.setCategoria(dto.getCategoria());
        producto.setPrecioCompra(dto.getPrecioCompra());
        producto.setPrecioVenta(dto.getPrecioVenta());
        producto.setMarca(dto.getMarca());
        producto.setStockMinimo(dto.getStockMinimo());
        producto.setUbicacion(dto.getUbicacion());
        // fechaActualizacion se actualiza automáticamente con @PreUpdate

        Producto updated = productoRepo.save(producto);
        return toDTO(updated);
    }

    @Transactional
    public void eliminar(Long id) {
        if (!productoRepo.existsById(id)) {
            throw new ProductoNoEncontradoException("Producto no encontrado con ID: " + id);
        }
        productoRepo.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<ProductoDTO> buscarBajoStock(int limiteStock) {
        return productoRepo.findByStockLessThan(limiteStock)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ProductoDTO> buscarPorNombre(String nombre) {
        return productoRepo.findByNombreContainingIgnoreCase(nombre)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    // 🔥 NUEVO: Buscar productos que necesitan reposición (stock < stockMinimo)
    @Transactional(readOnly = true)
    public List<ProductoDTO> buscarNecesitanReposicion() {
        return productoRepo.findAll()
                .stream()
                .filter(Producto::necesitaReposicion)
                .map(this::toDTO)
                .toList();
    }

    // 🔥 NUEVO: Buscar por categoría
    @Transactional(readOnly = true)
    public List<ProductoDTO> buscarPorCategoria(String categoria) {
        return productoRepo.findByCategoria(categoria)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    // 🔥 MÉTODO toDTO ACTUALIZADO
    private ProductoDTO toDTO(Producto producto) {
        return new ProductoDTO(
                producto.getId(),
                producto.getCodigoBarra(),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getStock(),
                producto.getCategoria(),
                producto.getPrecioCompra(),
                producto.getPrecioVenta(),
                producto.getMarca(),
                producto.getStockMinimo(),
                producto.getUbicacion(),
                producto.isActivo(),
                producto.getFechaCreacion(),
                producto.getFechaActualizacion(),
                producto.necesitaReposicion()
        );
    }
}