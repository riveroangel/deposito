package com.deposito.gamasonic.mapper;

import com.deposito.gamasonic.dto.DepositoRequestDTO;
import com.deposito.gamasonic.dto.DepositoResponseDTO;
import com.deposito.gamasonic.entity.Deposito;
import org.springframework.stereotype.Component;

@Component
public class DepositoMapper {

    public Deposito toEntity(DepositoRequestDTO dto) {
        Deposito deposito = new Deposito();
        deposito.setCodigo(dto.getCodigo());
        deposito.setNombre(dto.getNombre());
        deposito.setDireccion(dto.getDireccion());
        deposito.setCiudad(dto.getCiudad());
        deposito.setResponsable(dto.getResponsable());
        deposito.setTelefono(dto.getTelefono());
        deposito.setObservaciones(dto.getObservaciones());
        deposito.setActivo(dto.isActivo());
        deposito.setEsPrincipal(dto.isEsPrincipal());
        deposito.setCapacidadMaxima(dto.getCapacidadMaxima());
        return deposito;
    }

    public DepositoResponseDTO toDto(Deposito deposito) {
        DepositoResponseDTO dto = new DepositoResponseDTO();
        dto.setId(deposito.getId());
        dto.setCodigo(deposito.getCodigo());
        dto.setNombre(deposito.getNombre());
        dto.setDireccion(deposito.getDireccion());
        dto.setCiudad(deposito.getCiudad());
        dto.setResponsable(deposito.getResponsable());
        dto.setTelefono(deposito.getTelefono());
        dto.setObservaciones(deposito.getObservaciones());
        dto.setActivo(deposito.isActivo());
        dto.setEsPrincipal(deposito.isEsPrincipal());
        dto.setCapacidadMaxima(deposito.getCapacidadMaxima());
        dto.setFechaCreacion(deposito.getFechaCreacion());
        dto.setFechaActualizacion(deposito.getFechaActualizacion());
        return dto;
    }

    public void updateEntity(Deposito deposito, DepositoRequestDTO dto) {
        if (dto.getNombre() != null) deposito.setNombre(dto.getNombre());
        if (dto.getDireccion() != null) deposito.setDireccion(dto.getDireccion());
        if (dto.getCiudad() != null) deposito.setCiudad(dto.getCiudad());
        if (dto.getResponsable() != null) deposito.setResponsable(dto.getResponsable());
        if (dto.getTelefono() != null) deposito.setTelefono(dto.getTelefono());
        if (dto.getObservaciones() != null) deposito.setObservaciones(dto.getObservaciones());
        if (dto.getCapacidadMaxima() != null) deposito.setCapacidadMaxima(dto.getCapacidadMaxima());
    }
}
