window.validarFormulario = function() {
    const cantidad = document.getElementById('cantidad').value;
    const productoSelect = document.getElementById('codigoBarra');
    const producto = productoSelect.options[productoSelect.selectedIndex]?.text;
    const tipo = document.querySelector('input[name="tipo"]:checked').value;

    if (cantidad <= 0) {
        mostrarNotificacion(MENSAJES.ERROR_CANTIDAD, 'error');
        return false;
    }

    if (!productoSelect.value) {
        mostrarNotificacion('Debes seleccionar un producto', 'error');
        return false;
    }

    if (tipo === 'SALIDA') {
        return confirm(MENSAJES.CONFIRMAR_SALIDA(cantidad, producto));
    }

    const submitBtn = document.getElementById('submitBtn');
    submitBtn.disabled = true;
    submitBtn.querySelector('.submit-text').classList.add('hidden');
    submitBtn.querySelector('.loading-text').classList.remove('hidden');
    return true;
};