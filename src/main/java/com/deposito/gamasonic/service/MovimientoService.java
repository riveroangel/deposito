package com.deposito.gamasonic.service;

import com.deposito.gamasonic.dto.MovimientoDTO;
import com.deposito.gamasonic.dto.MovimientoEntradaDTO;
import com.deposito.gamasonic.dto.MovimientoSalidaDTO;
import com.deposito.gamasonic.dto.UsuarioProductividadDTO;
import com.deposito.gamasonic.entity.Movimiento;
import com.deposito.gamasonic.entity.Producto;
import com.deposito.gamasonic.entity.TipoMovimiento;
import com.deposito.gamasonic.entity.Usuario;
import com.deposito.gamasonic.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.deposito.gamasonic.repository.MovimientoRepository;
import com.deposito.gamasonic.repository.ProductoRepository;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MovimientoService {
    private final ProductoRepository productoRepo;
    private final MovimientoRepository movimientoRepo;
    private final UsuarioRepository usuarioRepo;

    public MovimientoService(
            ProductoRepository productoRepo,
            MovimientoRepository movimientoRepo,
            UsuarioRepository usuarioRepo
    ) {
        this.productoRepo = productoRepo;
        this.movimientoRepo = movimientoRepo;
        this.usuarioRepo = usuarioRepo;
    }

    @Transactional
    public MovimientoDTO registrarEntrada(
            MovimientoEntradaDTO dto,
            String username

    ) {
        // Validar cantidad positiva
        if (dto.getCantidad() <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La cantidad debe ser mayor a 0"
            );
        }

        Producto producto = productoRepo.findByCodigoBarra(dto.getCodigoBarra())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Producto no encontrado con código: " + dto.getCodigoBarra()
                ));



        Usuario usuario = usuarioRepo.findByUsername(username)
                .orElse(null); // 👈 permitimos null

        // Actualizar stock
        producto.setStock(producto.getStock() + dto.getCantidad());
     //  productoRepo.save(producto);
      //  productoRepo.flush();

        // Crear movimiento
        Movimiento movimiento = new Movimiento();
        movimiento.setProducto(producto);
        movimiento.setCantidad(dto.getCantidad());
        movimiento.setUsuario(usuario);
        movimiento.setTipo(TipoMovimiento.ENTRADA);
        movimiento.setFecha(LocalDateTime.now());

        movimientoRepo.save(movimiento);
        String usernameFinal = (usuario != null)
                ? usuario.getUsername()
                : "SYSTEM";


        return new MovimientoDTO(
                movimiento.getId(),
                producto.getCodigoBarra(),
                producto.getNombre(),
                dto.getCantidad(), // Positivo para entrada
                usernameFinal,
                movimiento.getFecha()
        );
    }

    @Transactional
    public MovimientoDTO registrarSalida(
            MovimientoSalidaDTO dto,
            String username
    ) {
        // Validar cantidad positiva
        if (dto.getCantidad() <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La cantidad debe ser mayor a 0"
            );
        }

        Producto producto = productoRepo.findByCodigoBarra(dto.getCodigoBarra())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Producto no encontrado con código: " + dto.getCodigoBarra()
                ));


       Usuario usuario = usuarioRepo.findByUsername(username).orElse(null);

        // Validar stock suficiente
        // 🔒 Validación crítica
        if (producto.getStock() < dto.getCantidad()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    String.format(
                            "Stock insuficiente. Disponible: %d, Solicitado: %d",
                            producto.getStock(),
                            dto.getCantidad()
                    )
            );
        }

        // Actualizar stock
        producto.setStock(producto.getStock() - dto.getCantidad());
        //productoRepo.save(producto);
        //productoRepo.flush();

        // Crear movimiento
        Movimiento movimiento = new Movimiento();
        movimiento.setProducto(producto);
        movimiento.setCantidad(dto.getCantidad()); // Guardamos cantidad positiva
        movimiento.setUsuario(usuario);
        movimiento.setTipo(TipoMovimiento.SALIDA);
        movimiento.setFecha(LocalDateTime.now());

        movimientoRepo.save(movimiento);

        String usernameFinal = (usuario != null)
                ? usuario.getUsername()
                : "SYSTEM";

        return new MovimientoDTO(
                movimiento.getId(),
                producto.getCodigoBarra(),
                producto.getNombre(),
                -dto.getCantidad(), // Negativo para mostrar como salida
                usernameFinal,
                movimiento.getFecha()
        );
    }
    @Transactional(readOnly = true)
    public List<MovimientoDTO> listarUltimosCinco() {
        return movimientoRepo.findTop5ByOrderByFechaDesc() // Necesitarás crear este método en el Repository
                .stream()
                .map(m -> new MovimientoDTO(
                        m.getId(),
                        m.getProducto().getNombre(),
                        m.getProducto().getCodigoBarra(),
                        m.getTipo() == TipoMovimiento.SALIDA ? -m.getCantidad() : m.getCantidad(),
                        m.getUsuario() != null ? m.getUsuario().getUsername() : "SISTEMA",
                        m.getFecha()
                ))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<UsuarioProductividadDTO> obtenerRankingHoy() {
        // Definimos el inicio del día actual (00:00:00)
        java.time.LocalDateTime inicioHoy = java.time.LocalDateTime.now()
                .with(java.time.LocalTime.MIN);

        // Llamamos al repositorio que creamos en el paso anterior
        return movimientoRepo.obtenerProductividadDelDia(inicioHoy);
    }

    @Transactional(readOnly = true)
    public List<MovimientoDTO> listar() {
        // Asegúrate de que el repositorio tenga este método con @EntityGraph
        return movimientoRepo.findAllWithRelations()
                .stream()
                .map(m -> new MovimientoDTO(
                        m.getId(),
                        m.getProducto().getCodigoBarra(),
                        m.getProducto().getNombre(),
                        m.getTipo() == TipoMovimiento.SALIDA ? -m.getCantidad() : m.getCantidad(),
                        m.getUsuario() != null ? m.getUsuario().getUsername() : "SYSTEM",
                        m.getFecha()
                ))
                .toList();
    }

    // Método adicional útil para debug
    @Transactional(readOnly = true)
    public MovimientoDTO buscarPorId(Long id) {
        Movimiento movimiento = movimientoRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Movimiento no encontrado con ID: " + id
                ));

        return new MovimientoDTO(
                movimiento.getId(),
                movimiento.getProducto().getCodigoBarra(),
                movimiento.getProducto().getNombre(),
                movimiento.getTipo() == TipoMovimiento.SALIDA ? -movimiento.getCantidad() : movimiento.getCantidad(),
                movimiento.getUsuario() != null ? movimiento.getUsuario().getUsername() : "SYSTEM",
                movimiento.getFecha()
        );
    }
    // En MovimientoService.java agregar:

    @Transactional
    public MovimientoDTO recibirPedido(Long pedidoId, String username) {
        // Este método se llamaría cuando un pedido se marca como COMPLETADO
        // para crear automáticamente los movimientos de entrada

        // Nota: Necesitarías inyectar PedidoRepository y PedidoService
        // Pedido pedido = pedidoRepository.findById(pedidoId).orElseThrow(...);

        // Por cada detalle del pedido que esté completo, crear movimiento de entrada
    /*
    for (DetallePedido detalle : pedido.getDetalles()) {
        if (detalle.estaCompleto()) {
            MovimientoEntradaDTO entradaDto = new MovimientoEntradaDTO(
                detalle.getProducto().getCodigoBarra(),
                detalle.getCantidadRecibida()
            );
            registrarEntrada(entradaDto, username);
        }
    }
    */

        // Por ahora devolvemos un DTO vacío - implementación completa depende de tu estructura
        return new MovimientoDTO(0L, "PEDIDO-" + pedidoId, "Recepción de pedido",
                0, username, LocalDateTime.now());
    }
}