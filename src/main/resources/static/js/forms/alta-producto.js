// Controlador del formulario de alta
const AltaProducto = (function() {
    function init() {
        configurarEventos();
       // cargarValidaciones();
    }

    function configurarEventos() {
        const btnEscanear = document.getElementById('btnEscanear');
        if (btnEscanear) {
            btnEscanear.addEventListener('click', manejarEscaneo);
        }

        const form = document.getElementById('altaProductoForm');
        if (form) {
            form.addEventListener('submit', validarFormulario);
        }

        // Validación de precios en tiempo real
        const precioCompra = document.getElementById('precioCompra');
        const precioVenta = document.getElementById('precioVenta');

        if (precioCompra && precioVenta) {
            precioCompra.addEventListener('input', validarPrecios);
            precioVenta.addEventListener('input', validarPrecios);
        }
    }

    async function manejarEscaneo() {
        if (!await Camara.verificarDisponibilidad()) {
            Notificaciones.error('No hay cámara disponible');
            return;
        }

        const config = {
            fps: 10,
            qrbox: { width: 250, height: 150 }
        };

        const callbacks = {
            onSuccess: (text) => {
                document.getElementById('codigoBarra').value = text;
                Notificaciones.exito(`Código: ${text}`);
                Camara.detener();
            },
            onError: (error) => {
                console.warn('Error escaneo:', error);
            }
        };

        try {
            await Camara.iniciar('reader', config, callbacks);
        } catch (error) {
            Notificaciones.error(MENSAJES.ERROR_CAMARA);
        }
    }

    function validarPrecios() {
        const compra = parseFloat(document.getElementById('precioCompra').value) || 0;
        const venta = parseFloat(document.getElementById('precioVenta').value) || 0;
        const warning = document.getElementById('precioWarning');

        if (venta > 0 && compra > 0 && venta <= compra) {
            warning?.classList.remove('hidden');
        } else {
            warning?.classList.add('hidden');
        }
    }

    function validarFormulario(e) {
        e.preventDefault();

        const datos = {
            codigo: document.getElementById('codigoBarra').value.trim(),
            nombre: document.getElementById('nombre').value.trim(),
            categoria: document.getElementById('categoria').value,
            precioCompra: parseFloat(document.getElementById('precioCompra').value) || 0,
            precioVenta: parseFloat(document.getElementById('precioVenta').value) || 0,
            stock: parseInt(document.getElementById('stock').value) || 0
        };

        // Validaciones
        if (!datos.codigo) {
            Notificaciones.error(MENSAJES.ERROR_CODIGO_REQUERIDO);
            return;
        }

        if (!datos.nombre) {
            Notificaciones.error(MENSAJES.ERROR_NOMBRE_REQUERIDO);
            return;
        }

        if (datos.precioCompra <= 0) {
            Notificaciones.error(MENSAJES.ERROR_PRECIO_COMPRA);
            return;
        }

        if (datos.precioVenta <= 0) {
            Notificaciones.error(MENSAJES.ERROR_PRECIO_VENTA);
            return;
        }

        // Confirmar si precio venta es menor
        if (datos.precioVenta <= datos.precioCompra) {
            if (!confirm(MENSAJES.CONFIRMAR_PRECIO(datos.precioCompra, datos.precioVenta))) {
                return;
            }
        }

        // Enviar formulario
        e.target.submit();
    }

    // Inicializar cuando el DOM esté listo
    document.addEventListener('DOMContentLoaded', init);

    return {
        init
    };
})();