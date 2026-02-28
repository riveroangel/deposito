async function onScanSuccess(decodedText) {
    const codigoBarraSelect = document.getElementById('codigoBarra');
    const scannerStatus = document.getElementById('scannerStatus');

    scannerStatus.textContent = MENSAJES.BUSCANDO;

    try {
        const response = await fetch(`/api/productos/codigo/${decodedText}`);

        if (response.ok) {
            const producto = await response.json();

            let option = Array.from(codigoBarraSelect.options).find(opt => opt.value === decodedText);

            if (!option) {
                option = document.createElement('option');
                option.value = producto.codigoBarra;
                option.text = producto.nombre;
                codigoBarraSelect.add(option);
                console.log("Producto agregado dinámicamente:", producto.nombre);
            }

            codigoBarraSelect.value = decodedText;
            codigoBarraSelect.classList.add('border-green-500', 'bg-green-50', 'ring-2', 'ring-green-200');

            mostrarNotificacion(`✅ ${producto.nombre} - Stock: ${producto.stock}`, 'exito');
        } else {
            mostrarNotificacion(`⚠️ Producto [${decodedText}] no existe en la base de datos`, 'error');
            codigoBarraSelect.classList.add('border-red-500', 'bg-red-50');
        }
    } catch (error) {
        console.error("Error en Fetch:", error);
        mostrarNotificacion(MENSAJES.ERROR_RED, 'error');
        codigoBarraSelect.classList.add('border-red-500', 'bg-red-50');
    }

    await detenerEscaneo();

    setTimeout(() => {
        codigoBarraSelect.classList.remove('border-green-500', 'bg-green-50', 'border-red-500', 'bg-red-50', 'ring-2', 'ring-green-200');
    }, 2000);

    if (window.navigator && window.navigator.vibrate) {
        window.navigator.vibrate([100, 50, 100]);
    }
}