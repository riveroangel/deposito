const MENSAJES = {
    ERROR_CAMARA: '❌ No se pudo acceder a la cámara. Asegúrate de tener permisos.',
    ERROR_LIBRERIA: 'Error: La librería del escáner no se cargó correctamente',
    ERROR_CANTIDAD: 'La cantidad debe ser mayor a 0',
    CONFIRMAR_SALIDA: (cantidad, producto) => `¿Confirmas la SALIDA de ${cantidad} unidades de ${producto}?`,
    ESCANEANDO: 'Escaneando código...',
    CAMARA_INICIANDO: 'Iniciando cámara...',
    PRODUCTO_NO_ENCONTRADO: (codigo) => `⚠️ El producto [${codigo}] no existe en la base de datos`,
    EXITO_LECTURA: '✅ Código leído correctamente',
    BUSCANDO: '🔍 Buscando en base de datos...',
    ERROR_RED: '❌ Error de red: No se pudo conectar con el servidor'
};