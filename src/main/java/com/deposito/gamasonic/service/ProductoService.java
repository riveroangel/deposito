package com.deposito.gamasonic.service;

import com.deposito.gamasonic.dto.ProductoCreateDTO;
import com.deposito.gamasonic.dto.ProductoDTO;
import com.deposito.gamasonic.entity.CategoriaProducto;
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

    // ==============================
    // LISTAR
    // ==============================
    public List<ProductoDTO> listar() {
        return productoRepo.findAll().stream()
                .map(this::toDTO)
                .toList();
    }

    // ==============================
    // CREAR
    // ==============================
    @Transactional
    public ProductoDTO crear(ProductoCreateDTO dto) {
        if (productoRepo.existsByCodigoBarra(dto.getCodigoBarra())) {
            throw new DuplicadoException("Ya existe un producto con código: " + dto.getCodigoBarra());
        }
        Producto producto = new Producto();
        producto.setCodigoBarra(dto.getCodigoBarra());
        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setStock(dto.getStock());
        producto.setCategoria(dto.getCategoria());
        producto.setPrecioCompra(dto.getPrecioCompra());
        producto.setPrecioVenta(dto.getPrecioVenta());
        producto.setMarca(dto.getMarca());
        producto.setUbicacion(dto.getUbicacion());
        producto.setStockMinimo(dto.getStockMinimo());
        producto.setActivo(true);

        producto = productoRepo.save(producto);
        return toDTO(producto);
    }

    // ==============================
    // ACTUALIZAR POR ID
    // ==============================
    @Transactional
    public ProductoDTO actualizar(Long id, ProductoCreateDTO dto) {
        Producto producto = productoRepo.findById(id)
                .orElseThrow(() -> new ProductoNoEncontradoException("Producto no encontrado con ID: " + id));

        if (!producto.getCodigoBarra().equals(dto.getCodigoBarra()) &&
                productoRepo.existsByCodigoBarra(dto.getCodigoBarra())) {
            throw new DuplicadoException("Ya existe un producto con código: " + dto.getCodigoBarra());
        }

        actualizarCampos(producto, dto);
        producto = productoRepo.save(producto);
        return toDTO(producto);
    }

    // ==============================
    // ACTUALIZAR POR CÓDIGO
    // ==============================
    @Transactional
    public ProductoDTO actualizarPorCodigo(String codigoBarra, ProductoCreateDTO dto) {
        Producto producto = productoRepo.findByCodigoBarra(codigoBarra)
                .orElseThrow(() -> new ProductoNoEncontradoException(
                        "Producto no encontrado con código: " + codigoBarra
                ));

        if (!producto.getCodigoBarra().equals(dto.getCodigoBarra()) &&
                productoRepo.existsByCodigoBarra(dto.getCodigoBarra())) {
            throw new DuplicadoException(
                    "Ya existe un producto con código: " + dto.getCodigoBarra()
            );
        }

        actualizarCampos(producto, dto);
        producto = productoRepo.save(producto);
        return toDTO(producto);
    }

    // ==============================
    // ELIMINAR POR ID
    // ==============================
    @Transactional
    public void eliminar(Long id) {
        Producto producto = productoRepo.findById(id)
                .orElseThrow(() -> new ProductoNoEncontradoException("Producto no encontrado con ID: " + id));
        productoRepo.delete(producto);
    }

    // ==============================
    // ELIMINAR POR CÓDIGO
    // ==============================
    @Transactional
    public void eliminarPorCodigo(String codigoBarra) {
        Producto producto = productoRepo.findByCodigoBarra(codigoBarra)
                .orElseThrow(() -> new ProductoNoEncontradoException(
                        "Producto no encontrado con código: " + codigoBarra
                ));
        productoRepo.delete(producto);
    }

    // ==============================
    // ACTIVAR / DESACTIVAR
    // ==============================
    @Transactional
    public void cambiarEstado(Long id, boolean activo) {
        Producto producto = productoRepo.findById(id)
                .orElseThrow(() -> new ProductoNoEncontradoException("Producto no encontrado con ID: " + id));
        producto.setActivo(activo);
        productoRepo.save(producto);
    }

    // ==============================
    // MAPPER
    // ==============================
    private ProductoDTO toDTO(Producto producto) {
        ProductoDTO dto = new ProductoDTO();
        dto.setId(producto.getId());
        dto.setCodigoBarra(producto.getCodigoBarra());
        dto.setNombre(producto.getNombre());
        dto.setDescripcion(producto.getDescripcion());
        dto.setStock(producto.getStock());
        dto.setCategoria(producto.getCategoria());
        dto.setPrecioCompra(producto.getPrecioCompra());
        dto.setPrecioVenta(producto.getPrecioVenta());
        dto.setMarca(producto.getMarca());
        dto.setUbicacion(producto.getUbicacion());
        dto.setStockMinimo(producto.getStockMinimo());
        dto.setActivo(producto.isActivo());
        dto.setFechaCreacion(producto.getFechaCreacion());
        dto.setFechaActualizacion(producto.getFechaActualizacion());

        // 🔥 CORRECCIÓN: Manejar null en stockMinimo
        if (producto.getStockMinimo() != null) {
            dto.setNecesitaReposicion(producto.getStock() <= producto.getStockMinimo());
        } else {
            dto.setNecesitaReposicion(false); // Si no hay stock mínimo, no necesita reposición
        }

        return dto;


    }

    // ==============================
    // UTILITY: Actualizar campos
    // ==============================
    private void actualizarCampos(Producto producto, ProductoCreateDTO dto) {
        producto.setCodigoBarra(dto.getCodigoBarra());
        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setStock(dto.getStock());
        producto.setCategoria(dto.getCategoria());
        producto.setPrecioCompra(dto.getPrecioCompra());
        producto.setPrecioVenta(dto.getPrecioVenta());
        producto.setMarca(dto.getMarca());
        producto.setUbicacion(dto.getUbicacion());
        producto.setStockMinimo(dto.getStockMinimo());
    }
    public List<ProductoDTO> buscarPorNombre(String nombre) {
        return productoRepo.findAll().stream()
                .filter(p -> p.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                .map(this::toDTO)
                .toList();
    }


    public ProductoDTO buscarPorId(Long id) {
        Producto producto = productoRepo.findById(id)
                .orElseThrow(() -> new ProductoNoEncontradoException("Producto no encontrado con ID: " + id));
        return toDTO(producto);
    }
    public ProductoDTO buscarPorCodigo(String codigoBarra) {
        Producto producto = productoRepo.findByCodigoBarra(codigoBarra)
                .orElseThrow(() -> new ProductoNoEncontradoException(
                        "Producto no encontrado con código: " + codigoBarra
                ));
        return toDTO(producto);
    }
    public List<ProductoDTO> buscarPorCategoria(CategoriaProducto categoria) {
        return productoRepo.findAll().stream()
                .filter(p -> p.getCategoria() == categoria)
                .map(this::toDTO)
                .toList();
    }
    public List<ProductoDTO> buscarBajoStock(int limite) {
        return productoRepo.findAll().stream()
                .filter(p -> p.getStock() <= limite)
                .map(this::toDTO)
                .toList();
    }
    public List<ProductoDTO> buscarNecesitanReposicion() {
        return productoRepo.findAll().stream()
                .filter(p -> p.getStock() <= p.getStockMinimo())
                .map(this::toDTO)
                .toList();
    }

}
