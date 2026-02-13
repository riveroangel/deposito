package com.deposito.gamasonic;

import com.deposito.gamasonic.dto.ProductoCreateDTO;
import com.deposito.gamasonic.dto.ProductoDTO;
import com.deposito.gamasonic.entity.CategoriaProducto;
import com.deposito.gamasonic.exception.DuplicadoException;
import com.deposito.gamasonic.exception.ProductoNoEncontradoException;
import com.deposito.gamasonic.service.ProductoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
@ActiveProfiles("test")
@Transactional
@SpringBootTest
class GamasonicApplicationTests {


	@Autowired
	private ProductoService productoService;

	@Test
	void contextLoads() {
	}

	private ProductoCreateDTO productoValido() {
		ProductoCreateDTO dto = new ProductoCreateDTO();
		dto.setCodigoBarra("TEST-123");
		dto.setNombre("Producto Test");
		dto.setStock(10);
		dto.setCategoria(CategoriaProducto.ELECTRONICA);
		dto.setPrecioCompra(BigDecimal.valueOf(100));
		dto.setPrecioVenta(BigDecimal.valueOf(150));
		dto.setStockMinimo(5);
		return dto;
	}

	@Test
	void crearProducto_ok() {
		ProductoDTO creado = productoService.crear(productoValido());

		assertNotNull(creado.getId());
		assertEquals("Producto Test", creado.getNombre());
		assertTrue(creado.isActivo());
	}

	@Test
	void crearProducto_codigoDuplicado_lanzaException() {
		productoService.crear(productoValido());

		assertThrows(DuplicadoException.class, () -> {
			productoService.crear(productoValido());
		});
	}

	@Test
	void buscarPorId_inexistente_lanzaException() {
		assertThrows(ProductoNoEncontradoException.class, () -> {
			productoService.buscarPorId(999L);
		});
	}

	@Test
	void eliminarProducto_inexistente_lanzaException() {
		assertThrows(ProductoNoEncontradoException.class, () -> {
			productoService.eliminar(999L);
		});
	}
}
