// Módulo de escáner
const Scanner = (function() {
    let html5QrCode = null;
    let flashDisponible = false;

    async function iniciar() {
        try {
            const tieneCamara = await CameraUtils.verificar();
            if (!tieneCamara) {
                Notificaciones.mostrar('No hay cámara disponible', 'error');
                return;
            }

            html5QrCode = new Html5Qrcode("reader");
            await html5QrCode.start(
                { facingMode: "environment" },
                { fps: 10, qrbox: { width: 250, height: 150 } },
                onScanSuccess,
                onScanError
            );

        } catch (error) {
            Notificaciones.mostrar('Error al iniciar cámara', 'error');
            console.error(error);
        }
    }

    function detener() {
        if (html5QrCode) {
            html5QrCode.stop();
            html5QrCode = null;
        }
    }

    return {
        iniciar,
        detener
    };
})();