package com.deposito.gamasonic.service;

import com.deposito.gamasonic.dto.*;
import com.deposito.gamasonic.entity.*;
import com.deposito.gamasonic.exception.PedidoNoEncontradoException;
import com.deposito.gamasonic.exception.ProductoNoEncontradoException;
import com.deposito.gamasonic.exception.ProveedorNoEncontradoException;
import com.deposito.gamasonic.repository.PedidoRepository;
import com.deposito.gamasonic.repository.ProductoRepository;
import com.deposito.gamasonic.repository.ProveedorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProveedorRepository proveedorRepository;
    private final ProductoRepository productoRepository;
    private final ProveedorService proveedorService;
    private final ProductoService productoService;

    public PedidoService(PedidoRepository pedidoRepository,
                         ProveedorRepository proveedorRepository,
                         ProductoRepository productoRepository,
                         ProveedorService proveedorService,
                         ProductoService productoService) {
        this.pedidoRepository = pedidoRepository;
        this.proveedorRepository = proveedorRepository;
        this.productoRepository = productoRepository;
        this.proveedorService = proveedorService;
        this.productoService = productoService;
    }

    @Transactional
    public PedidoResponseDTO crear(PedidoRequestDTO dto) {
        // Validar proveedor
        Proveedor proveedor = proveedorRepository.findById(dto.getProveedorId())
                .orElseThrow(() -> new ProveedorNoEncontradoException(
                        "Proveedor no encontrado con ID: " + dto.getProveedorId()));

        // Crear pedido
        Pedido pedido = new Pedido();
        pedido.setProveedor(proveedor);
        pedido.setEstado(EstadoPedido.BORRADOR);
        pedido.setFechaPedido(LocalDate.now());
        pedido.setFechaEsperadaEntrega(dto.getFechaEsperadaEntrega());
        pedido.setObservaciones(dto.getObservaciones());

        // Agregar detalles
        for (DetallePedidoRequestDTO detalleDto : dto.getDetalles()) {
            Producto producto = productoRepository.findById(detalleDto.getProductoId())
                    .orElseThrow(() -> new ProductoNoEncontradoException(
                            "Producto no encontrado con ID: " + detalleDto.getProductoId()));

            DetallePedido detalle = new DetallePedido();
            detalle.setProducto(producto);
            detalle.setCantidad(detalleDto.getCantidad());
            detalle.setPrecioUnitario(detalleDto.getPrecioUnitario());
            detalle.setCantidadRecibida(0);

            pedido.agregarDetalle(detalle);
        }

        Pedido saved = pedidoRepository.save(pedido);

        // Generar número de pedido
        if (saved.getNumeroPedido() == null) {
            String numeroPedido = "PED-" + LocalDate.now().getYear() + "-" +
                    String.format("%03d", saved.getId());
            saved.setNumeroPedido(numeroPedido);
            saved = pedidoRepository.save(saved);
        }

        return toDTO(saved);
    }

    @Transactional
    public PedidoResponseDTO enviarPedido(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new PedidoNoEncontradoException(
                        "Pedido no encontrado con ID: " + id));

        if (pedido.getEstado() != EstadoPedido.BORRADOR) {
            throw new IllegalStateException("Solo pedidos en estado BORRADOR pueden enviarse");
        }

        if (pedido.getDetalles().isEmpty()) {
            throw new IllegalStateException("El pedido debe tener al menos un detalle");
        }

        pedido.setEstado(EstadoPedido.PENDIENTE);
        Pedido updated = pedidoRepository.save(pedido);

        return toDTO(updated);
    }

    @Transactional
    public PedidoResponseDTO recibirMercaderia(Long pedidoId,
                                               List<RecepcionDetalleDTO> recepcion) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new PedidoNoEncontradoException(
                        "Pedido no encontrado con ID: " + pedidoId));

        if (pedido.getEstado() != EstadoPedido.PENDIENTE &&
                pedido.getEstado() != EstadoPedido.PARCIAL) {
            throw new IllegalStateException("Solo pedidos PENDIENTES o PARCIALES pueden recibir mercadería");
        }

        for (RecepcionDetalleDTO recepcionDto : recepcion) {
            DetallePedido detalle = pedido.getDetalles().stream()
                    .filter(d -> d.getId().equals(recepcionDto.getDetallePedidoId()))
                    .findFirst()
                    .orElseThrow(() -> new PedidoNoEncontradoException(
                            "Detalle no encontrado con ID: " + recepcionDto.getDetallePedidoId()));

            int nuevaCantidadRecibida = detalle.getCantidadRecibida() + recepcionDto.getCantidadRecibida();
            if (nuevaCantidadRecibida > detalle.getCantidad()) {
                throw new IllegalArgumentException(
                        "Cantidad recibida excede la cantidad pedida para el producto: " +
                                detalle.getProducto().getNombre());
            }

            detalle.setCantidadRecibida(nuevaCantidadRecibida);
        }

        // Verificar estado del pedido
        boolean todosCompletos = pedido.getDetalles().stream()
                .allMatch(DetallePedido::estaCompleto);

        boolean algunoRecibido = pedido.getDetalles().stream()
                .anyMatch(d -> d.getCantidadRecibida() > 0);

        if (todosCompletos) {
            pedido.setEstado(EstadoPedido.COMPLETADO);
            pedido.setFechaRealEntrega(LocalDate.now());
        } else if (algunoRecibido) {
            pedido.setEstado(EstadoPedido.PARCIAL);
        }

        Pedido updated = pedidoRepository.save(pedido);

        return toDTO(updated);
    }

    @Transactional
    public PedidoResponseDTO cancelarPedido(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new PedidoNoEncontradoException(
                        "Pedido no encontrado con ID: " + id));

        if (pedido.getEstado() == EstadoPedido.COMPLETADO) {
            throw new IllegalStateException("No se puede cancelar un pedido COMPLETADO");
        }

        pedido.setEstado(EstadoPedido.CANCELADO);
        Pedido updated = pedidoRepository.save(pedido);

        return toDTO(updated);
    }

    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> listarTodos() {
        return pedidoRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public PedidoResponseDTO buscarPorId(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new PedidoNoEncontradoException(
                        "Pedido no encontrado con ID: " + id));
        return toDTO(pedido);
    }

    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> buscarPorProveedor(Long proveedorId) {
        return pedidoRepository.findByProveedorId(proveedorId)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> buscarPorEstado(String estado) {
        try {
            EstadoPedido estadoEnum = EstadoPedido.valueOf(estado.toUpperCase());
            return pedidoRepository.findByEstado(estadoEnum)
                    .stream()
                    .map(this::toDTO)
                    .toList();
        } catch (IllegalArgumentException e) {
            return List.of();
        }
    }

    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> buscarPendientes() {
        List<EstadoPedido> estados = List.of(
                EstadoPedido.PENDIENTE,
                EstadoPedido.PARCIAL
        );
        return pedidoRepository.findByEstados(estados)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    private PedidoResponseDTO toDTO(Pedido pedido) {
        PedidoResponseDTO dto = new PedidoResponseDTO();
        dto.setId(pedido.getId());
        dto.setNumeroPedido(pedido.getNumeroPedido());
        dto.setProveedor(proveedorService.buscarPorId(pedido.getProveedor().getId()));
        dto.setEstado(pedido.getEstado());
        dto.setFechaPedido(pedido.getFechaPedido());
        dto.setFechaEsperadaEntrega(pedido.getFechaEsperadaEntrega());
        dto.setFechaRealEntrega(pedido.getFechaRealEntrega());
        dto.setObservaciones(pedido.getObservaciones());
        dto.setSubtotal(pedido.getSubtotal());
        dto.setImpuestos(pedido.getImpuestos());
        dto.setTotal(pedido.getTotal());
        dto.setFechaCreacion(pedido.getFechaCreacion());
        dto.setFechaActualizacion(pedido.getFechaActualizacion());

        // Convertir detalles
        List<DetallePedidoResponseDTO> detallesDTO = new ArrayList<>();
        for (DetallePedido detalle : pedido.getDetalles()) {
            DetallePedidoResponseDTO detalleDTO = new DetallePedidoResponseDTO();
            detalleDTO.setId(detalle.getId());
            detalleDTO.setProducto(productoService.buscarPorId(detalle.getProducto().getId()));
            detalleDTO.setCantidad(detalle.getCantidad());
            detalleDTO.setCantidadRecibida(detalle.getCantidadRecibida());
            detalleDTO.setCantidadPendiente(detalle.getCantidadPendiente());
            detalleDTO.setPrecioUnitario(detalle.getPrecioUnitario());
            detalleDTO.setSubtotal(detalle.getSubtotal());
            detalleDTO.setCompleto(detalle.estaCompleto());
            detallesDTO.add(detalleDTO);
        }

        dto.setDetalles(detallesDTO);
        return dto;
    }
}