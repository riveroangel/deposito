package com.deposito.gamasonic.config;

import com.deposito.gamasonic.entity.Deposito;
import com.deposito.gamasonic.entity.Rol;
import com.deposito.gamasonic.entity.Usuario;
import com.deposito.gamasonic.repository.DepositoRepository;
import com.deposito.gamasonic.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final DepositoRepository depositoRepository;
    // SIN PasswordEncoder - ya que no usamos Spring Security

    @Override
    public void run(String... args) throws Exception {
        log.info("=== INICIALIZANDO DATOS DE PRUEBA ===");

        inicializarUsuarios();
        inicializarDepositos();

        log.info("=== INICIALIZACION COMPLETADA ===");
    }

    private void inicializarUsuarios() {
        // Crear usuario admin si no existe
        if (usuarioRepository.findByUsername("admin").isEmpty()) {
            Usuario admin = new Usuario();
            admin.setUsername("admin");
            admin.setPassword("admin123");  // SIN ENCRIPTAR - temporalmente
            admin.setRol(Rol.ADMIN);
            admin.setEnabled(true);
            usuarioRepository.save(admin);
            log.info("Usuario admin creado: admin / admin123");
        }

        // Crear usuario operador si no existe
        if (usuarioRepository.findByUsername("operador").isEmpty()) {
            Usuario operador = new Usuario();
            operador.setUsername("operador");
            operador.setPassword("operador123");  // SIN ENCRIPTAR - temporalmente
            operador.setRol(Rol.OPERADOR);
            operador.setEnabled(true);
            usuarioRepository.save(operador);
            log.info("Usuario operador creado: operador / operador123");
        }

        log.info("Total usuarios en BD: {}", usuarioRepository.count());
    }

    private void inicializarDepositos() {
        // Verificar si ya hay depósitos
        long totalDepositos = depositoRepository.count();

        if (totalDepositos == 0) {
            log.info("Creando depositos iniciales...");

            // Depósito 1 - Principal
            Deposito deposito1 = new Deposito();
            deposito1.setCodigo("DEP-001");
            deposito1.setNombre("Deposito Central");
            deposito1.setDireccion("Av. Principal 123, Lima 15001");
            deposito1.setCiudad("Lima");
            deposito1.setResponsable("Juan Perez");
            deposito1.setTelefono("987654321");
            deposito1.setObservaciones("Deposito principal de la empresa");
            deposito1.setActivo(true);
            deposito1.setEsPrincipal(true);
            deposito1.setCapacidadMaxima(10000);
            deposito1.setFechaCreacion(LocalDateTime.now());
            depositoRepository.save(deposito1);

            // Depósito 2 - Secundario
            Deposito deposito2 = new Deposito();
            deposito2.setCodigo("DEP-002");
            deposito2.setNombre("Almacen Norte");
            deposito2.setDireccion("Calle Comercio 456, Trujillo 13001");
            deposito2.setCiudad("Trujillo");
            deposito2.setResponsable("Maria Lopez");
            deposito2.setTelefono("987654322");
            deposito2.setObservaciones("Almacen regional para el norte del pais");
            deposito2.setActivo(true);
            deposito2.setEsPrincipal(false);
            deposito2.setCapacidadMaxima(5000);
            deposito2.setFechaCreacion(LocalDateTime.now());
            depositoRepository.save(deposito2);

            // Depósito 3 - Secundario
            Deposito deposito3 = new Deposito();
            deposito3.setCodigo("DEP-003");
            deposito3.setNombre("Bodega Sur");
            deposito3.setDireccion("Av. Industrial 789, Arequipa 04001");
            deposito3.setCiudad("Arequipa");
            deposito3.setResponsable("Carlos Gomez");
            deposito3.setTelefono("987654323");
            deposito3.setObservaciones("Bodega para productos del sur");
            deposito3.setActivo(true);
            deposito3.setEsPrincipal(false);
            deposito3.setCapacidadMaxima(3000);
            deposito3.setFechaCreacion(LocalDateTime.now());
            depositoRepository.save(deposito3);

            log.info("Se crearon {} depositos iniciales", depositoRepository.count());
        } else {
            log.info("Ya existen {} depositos en la base de datos", totalDepositos);
        }
    }
}