package com.deposito.gamasonic.service;

import com.deposito.gamasonic.dto.*;
import com.deposito.gamasonic.entity.*;
import com.deposito.gamasonic.exception.DepositoNoEncontradoException;
import com.deposito.gamasonic.exception.ProductoNoEncontradoException;
import com.deposito.gamasonic.exception.StockDepositoNoEncontradoException;
import com.deposito.gamasonic.repository.DepositoRepository;
import com.deposito.gamasonic.repository.ProductoRepository;
import com.deposito.gamasonic.repository.StockDepositoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StockDepositoService {

    private final StockDepositoRepository stockDepositoRepository;
    private final ProductoRepository productoRepository;
    private final DepositoRepository depositoRepository;
    private final ProductoService productoService;
    private final DepositoService depositoService;

    public StockDepositoService(StockDepositoRepository stockDepositoRepository,
                                ProductoRepository productoRepository,
                                DepositoRepository depositoRepository,
                                ProductoService productoService,
                                DepositoService depositoService) {
        this.stockDepositoRepository = stockDepositoRepository;
        this.productoRepository = productoRepository;
        this.depositoRepository = depositoRepository;
        this.productoService = productoService;
        this.depositoService = depositoService;
    }

    @Transactional
    public StockDepositoResponseDTO crearOActualizar(StockDepositoRequestDTO dto) {
        // Validar producto y depósito
        Producto producto = productoRepository.findById(dto.getProductoId())
                .orElseThrow(() -> new ProductoNoEncontradoException(
                        "Producto no encontrado con ID: " + dto.getProductoId()));

        Deposito deposito = depositoRepository.findById(dto.getDepositoId())
                .orElseThrow(() -> new DepositoNoEncontradoException(
                        "Depósito no encontrado con ID: " + dto.getDepositoId()));

        if (!deposito.isActivo()) {
            throw new IllegalStateException("No se puede asignar stock a un depósito inactivo");
        }

        // Buscar si ya existe
        StockDeposito stockDeposito = stockDepositoRepository
                .findByProductoIdAndDepositoId(dto.getProductoId(), dto.getDepositoId())
                .orElse(new StockDeposito());

        stockDeposito.setProducto(producto);
        stockDeposito.setDeposito(deposito);
        stockDeposito.setStock(dto.getStock());
        stockDeposito.setStockMinimo(dto.getStockMinimo());
        stockDeposito.setStockMaximo(dto.getStockMaximo());
        stockDeposito.setUbicacion(dto.getUbicacion());

        StockDeposito saved = stockDepositoRepository.save(stockDeposito);
        return toDTO(saved);
    }

    @Transactional
    public StockDepositoResponseDTO ajustarStock(AjusteStockDTO dto, String username) {
        StockDeposito stockDeposito = stockDepositoRepository
                .findByProductoIdAndDepositoId(dto.getProductoId(), dto.getDepositoId())
                .orElseThrow(() -> new StockDepositoNoEncontradoException(
                        "Stock no encontrado para producto " + dto.getProductoId() +
                                " en depósito " + dto.getDepositoId()));

        if (dto.getCantidad() > 0) {
            stockDeposito.agregarStock(dto.getCantidad());
        } else if (dto.getCantidad() < 0) {
            stockDeposito.reducirStock(Math.abs(dto.getCantidad()));
        }

        // Aquí podrías crear un registro de movimiento de ajuste
        // movimientoService.registrarAjuste(dto, username);

        StockDeposito updated = stockDepositoRepository.save(stockDeposito);
        return toDTO(updated);
    }

    @Transactional
    public StockDepositoResponseDTO transferirStock(Long productoId, Long origenId,
                                                    Long destinoId, Integer cantidad,
                                                    String username) {
        if (origenId.equals(destinoId)) {
            throw new IllegalArgumentException("No se puede transferir al mismo depósito");
        }

        // Verificar stock en origen
        StockDeposito stockOrigen = stockDepositoRepository
                .findByProductoIdAndDepositoId(productoId, origenId)
                .orElseThrow(() -> new StockDepositoNoEncontradoException(
                        "Stock insuficiente en origen"));

        if (!stockOrigen.tieneStockSuficiente(cantidad)) {
            throw new IllegalArgumentException(
                    "Stock insuficiente en depósito origen. Disponible: " +
                            stockOrigen.getStock() + ", Solicitado: " + cantidad);
        }

        // Buscar o crear stock en destino
        StockDeposito stockDestino = stockDepositoRepository
                .findByProductoIdAndDepositoId(productoId, destinoId)
                .orElseGet(() -> {
                    StockDeposito nuevo = new StockDeposito();
                    nuevo.setProducto(stockOrigen.getProducto());
                    nuevo.setDeposito(depositoRepository.findById(destinoId).orElseThrow());
                    nuevo.setStock(0);
                    return nuevo;
                });

        // Realizar transferencia
        stockOrigen.reducirStock(cantidad);
        stockDestino.agregarStock(cantidad);

        stockDepositoRepository.save(stockOrigen);
        StockDeposito savedDestino = stockDepositoRepository.save(stockDestino);

        // Aquí podrías crear un registro de transferencia
        // transferenciaService.crearTransferencia(productoId, origenId, destinoId, cantidad, username);

        return toDTO(savedDestino);
    }

    @Transactional(readOnly = true)
    public StockDepositoResponseDTO buscarPorId(Long id) {
        StockDeposito stockDeposito = stockDepositoRepository.findById(id)
                .orElseThrow(() -> new StockDepositoNoEncontradoException(
                        "Stock no encontrado con ID: " + id));
        return toDTO(stockDeposito);
    }

    @Transactional(readOnly = true)
    public StockDepositoResponseDTO buscarPorProductoYDeposito(Long productoId, Long depositoId) {
        StockDeposito stockDeposito = stockDepositoRepository
                .findByProductoIdAndDepositoId(productoId, depositoId)
                .orElseThrow(() -> new StockDepositoNoEncontradoException(
                        "Stock no encontrado para producto " + productoId +
                                " en depósito " + depositoId));
        return toDTO(stockDeposito);
    }

    @Transactional(readOnly = true)
    public List<StockDepositoResponseDTO> buscarPorProducto(Long productoId) {
        return stockDepositoRepository.findByProductoId(productoId)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<StockDepositoResponseDTO> buscarPorDeposito(Long depositoId) {
        return stockDepositoRepository.findByDepositoId(depositoId)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<StockDepositoResponseDTO> buscarBajoStockEnDeposito(Long depositoId) {
        return stockDepositoRepository.findProductosBajoStockEnDeposito(depositoId)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public Integer getStockTotalProducto(Long productoId) {
        return stockDepositoRepository.getStockTotalProducto(productoId);
    }

    @Transactional(readOnly = true)
    public List<StockDepositoResponseDTO> buscarStockEnTodosDepositos() {
        return stockDepositoRepository.findStockEnDepositosActivos()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<StockDepositoResponseDTO> buscarDisponibilidadProducto(Long productoId, Integer cantidadMinima) {
        return stockDepositoRepository.findByProductoIdAndStockGreaterThan(productoId, cantidadMinima)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    private StockDepositoResponseDTO toDTO(StockDeposito stockDeposito) {
        ProductoDTO productoDTO = productoService.buscarPorId(stockDeposito.getProducto().getId());
        DepositoResponseDTO depositoDTO = depositoService.buscarPorId(stockDeposito.getDeposito().getId());

        return new StockDepositoResponseDTO(
                stockDeposito.getId(),
                productoDTO,
                depositoDTO,
                stockDeposito.getStock(),
                stockDeposito.getStockMinimo(),
                stockDeposito.getStockMaximo(),
                stockDeposito.getUbicacion(),
                stockDeposito.necesitaReposicion(),
                stockDeposito.excedeMaximo()
        );
    }
}