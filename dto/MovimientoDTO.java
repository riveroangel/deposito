package com.deposito.gamasonic.dto;

import java.time.LocalDateTime;
//para observar ya que no se usa nunca
public record MovimientoDTO(Long id,
                            String codigoBarra,
                            String nombreProducto,
                            int cantidad,
                            String usuario,
                            LocalDateTime fecha
) {}


