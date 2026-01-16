package com.deposito.gamasonic.service;

import com.deposito.gamasonic.dto.ProveedorRequestDTO;
import com.deposito.gamasonic.dto.ProveedorResponseDTO;
import com.deposito.gamasonic.entity.Proveedor;
import com.deposito.gamasonic.exception.DuplicadoException;
import com.deposito.gamasonic.exception.ProveedorNoEncontradoException;
import com.deposito.gamasonic.repository.ProveedorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProveedorService {

    private final ProveedorRepository proveedorRepository;

    public ProveedorService(ProveedorRepository proveedorRepository) {
        this.proveedorRepository = proveedorRepository;
    }

    @Transactional
    public ProveedorResponseDTO crear(ProveedorRequestDTO dto) {
        // Validar RUC único
        if (dto.getRuc() != null && !dto.getRuc().isEmpty()) {
            if (proveedorRepository.existsByRuc(dto.getRuc())) {
                throw new DuplicadoException("Ya existe un proveedor con RUC: " + dto.getRuc());
            }
        }

        Proveedor proveedor = new Proveedor();
        proveedor.setNombre(dto.getNombre());
        proveedor.setRuc(dto.getRuc());
        proveedor.setDireccion(dto.getDireccion());
        proveedor.setTelefono(dto.getTelefono());
        proveedor.setEmail(dto.getEmail());
        proveedor.setContacto(dto.getContacto());
        proveedor.setPlazoPago(dto.getPlazoPago());
        proveedor.setObservaciones(dto.getObservaciones());
        proveedor.setActivo(true);

        Proveedor saved = proveedorRepository.save(proveedor);

        // Generar código automático si no se generó
        if (saved.getCodigo() == null || saved.getCodigo().isEmpty()) {
            saved.setCodigo("PROV-" + String.format("%03d", saved.getId()));
            saved = proveedorRepository.save(saved);
        }

        return toDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<ProveedorResponseDTO> listarTodos() {
        return proveedorRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ProveedorResponseDTO> listarActivos() {
        return proveedorRepository.findByActivoTrue()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProveedorResponseDTO buscarPorId(Long id) {
        Proveedor proveedor = proveedorRepository.findById(id)
                .orElseThrow(() -> new ProveedorNoEncontradoException(
                        "Proveedor no encontrado con ID: " + id));
        return toDTO(proveedor);
    }

    @Transactional(readOnly = true)
    public ProveedorResponseDTO buscarPorCodigo(String codigo) {
        Proveedor proveedor = proveedorRepository.findByCodigo(codigo)
                .orElseThrow(() -> new ProveedorNoEncontradoException(
                        "Proveedor no encontrado con código: " + codigo));
        return toDTO(proveedor);
    }

    @Transactional
    public ProveedorResponseDTO actualizar(Long id, ProveedorRequestDTO dto) {
        Proveedor proveedor = proveedorRepository.findById(id)
                .orElseThrow(() -> new ProveedorNoEncontradoException(
                        "Proveedor no encontrado con ID: " + id));

        // Validar RUC único si cambió
        if (dto.getRuc() != null && !dto.getRuc().isEmpty() &&
                !dto.getRuc().equals(proveedor.getRuc())) {
            if (proveedorRepository.existsByRuc(dto.getRuc())) {
                throw new DuplicadoException("Ya existe un proveedor con RUC: " + dto.getRuc());
            }
        }

        proveedor.setNombre(dto.getNombre());
        proveedor.setRuc(dto.getRuc());
        proveedor.setDireccion(dto.getDireccion());
        proveedor.setTelefono(dto.getTelefono());
        proveedor.setEmail(dto.getEmail());
        proveedor.setContacto(dto.getContacto());
        proveedor.setPlazoPago(dto.getPlazoPago());
        proveedor.setObservaciones(dto.getObservaciones());

        Proveedor updated = proveedorRepository.save(proveedor);
        return toDTO(updated);
    }

    @Transactional
    public void desactivar(Long id) {
        Proveedor proveedor = proveedorRepository.findById(id)
                .orElseThrow(() -> new ProveedorNoEncontradoException(
                        "Proveedor no encontrado con ID: " + id));
        proveedor.setActivo(false);
        proveedorRepository.save(proveedor);
    }

    @Transactional
    public void activar(Long id) {
        Proveedor proveedor = proveedorRepository.findById(id)
                .orElseThrow(() -> new ProveedorNoEncontradoException(
                        "Proveedor no encontrado con ID: " + id));
        proveedor.setActivo(true);
        proveedorRepository.save(proveedor);
    }

    @Transactional(readOnly = true)
    public List<ProveedorResponseDTO> buscarPorNombre(String nombre) {
        return proveedorRepository.findByNombreContainingIgnoreCase(nombre)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    private ProveedorResponseDTO toDTO(Proveedor proveedor) {
        Integer totalPedidos = proveedorRepository.countPedidosByProveedorId(proveedor.getId());

        return new ProveedorResponseDTO(
                proveedor.getId(),
                proveedor.getCodigo(),
                proveedor.getNombre(),
                proveedor.getRuc(),
                proveedor.getDireccion(),
                proveedor.getTelefono(),
                proveedor.getEmail(),
                proveedor.getContacto(),
                proveedor.getPlazoPago(),
                proveedor.getObservaciones(),
                proveedor.isActivo(),
                proveedor.getFechaCreacion(),
                proveedor.getFechaActualizacion(),
                totalPedidos
        );
    }
}
