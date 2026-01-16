package com.deposito.gamasonic.service;

import com.deposito.gamasonic.dto.DepositoRequestDTO;
import com.deposito.gamasonic.dto.DepositoResponseDTO;
import com.deposito.gamasonic.entity.Deposito;
import com.deposito.gamasonic.mapper.DepositoMapper;
import com.deposito.gamasonic.repository.DepositoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DepositoService {

    private final DepositoRepository depositoRepository;
    private final DepositoMapper depositoMapper;

    public DepositoResponseDTO crear(DepositoRequestDTO dto) {
        if (depositoRepository.existsByCodigo(dto.getCodigo())) {
            throw new RuntimeException("Ya existe un deposito con el codigo: " + dto.getCodigo());
        }

        Deposito deposito = depositoMapper.toEntity(dto);

        if (depositoRepository.count() == 0) {
            deposito.setEsPrincipal(true);
        }

        Deposito saved = depositoRepository.save(deposito);
        return depositoMapper.toDto(saved);
    }

    public List<DepositoResponseDTO> listarTodos() {
        return depositoRepository.findAll().stream()
                .map(depositoMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<DepositoResponseDTO> listarActivos() {
        return depositoRepository.findByActivoTrue().stream()
                .map(depositoMapper::toDto)
                .collect(Collectors.toList());
    }

    public DepositoResponseDTO buscarPorId(Long id) {
        Deposito deposito = depositoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Deposito no encontrado con ID: " + id));
        return depositoMapper.toDto(deposito);
    }

    public DepositoResponseDTO buscarPorCodigo(String codigo) {
        Deposito deposito = depositoRepository.findByCodigo(codigo)
                .orElseThrow(() -> new EntityNotFoundException("Deposito no encontrado con codigo: " + codigo));
        return depositoMapper.toDto(deposito);
    }

    @Transactional
    public DepositoResponseDTO actualizar(Long id, DepositoRequestDTO dto) {
        Deposito deposito = depositoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Deposito no encontrado con ID: " + id));

        if (dto.getCodigo() != null && !dto.getCodigo().equals(deposito.getCodigo())) {
            if (depositoRepository.existsByCodigo(dto.getCodigo())) {
                throw new RuntimeException("Ya existe otro deposito con el codigo: " + dto.getCodigo());
            }
            deposito.setCodigo(dto.getCodigo());
        }

        depositoMapper.updateEntity(deposito, dto);
        Deposito updated = depositoRepository.save(deposito);
        return depositoMapper.toDto(updated);
    }

    @Transactional
    public void desactivar(Long id) {
        Deposito deposito = depositoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Deposito no encontrado con ID: " + id));

        if (deposito.isEsPrincipal()) {
            throw new RuntimeException("No se puede desactivar el deposito principal");
        }

        deposito.setActivo(false);
        depositoRepository.save(deposito);
    }

    @Transactional
    public void activar(Long id) {
        Deposito deposito = depositoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Deposito no encontrado con ID: " + id));

        deposito.setActivo(true);
        depositoRepository.save(deposito);
    }

    @Transactional
    public DepositoResponseDTO marcarComoPrincipal(Long id) {
        List<Deposito> principales = depositoRepository.findByEsPrincipalTrue();
        for (Deposito principal : principales) {
            principal.setEsPrincipal(false);
        }
        depositoRepository.saveAll(principales);

        Deposito nuevoPrincipal = depositoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Deposito no encontrado con ID: " + id));

        nuevoPrincipal.setEsPrincipal(true);
        nuevoPrincipal.setActivo(true);

        Deposito saved = depositoRepository.save(nuevoPrincipal);
        return depositoMapper.toDto(saved);
    }

    public DepositoResponseDTO obtenerDepositoPrincipal() {
        return depositoRepository.findByEsPrincipalTrue().stream()
                .findFirst()
                .map(depositoMapper::toDto)
                .orElseThrow(() -> new RuntimeException("No hay deposito principal configurado"));
    }

    public List<DepositoResponseDTO> buscarPorNombre(String nombre) {
        return depositoRepository.findByNombreContainingIgnoreCase(nombre).stream()
                .map(depositoMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<DepositoResponseDTO> buscarPorCiudad(String ciudad) {
        return depositoRepository.findByCiudad(ciudad).stream()
                .map(depositoMapper::toDto)
                .collect(Collectors.toList());
    }
}