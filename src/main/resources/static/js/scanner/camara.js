// Módulo de cámara
const Camara = (function() {
    let instancia = null;
    let activa = false;

    async function verificarDisponibilidad() {
        if (!navigator.mediaDevices?.enumerateDevices) return false;
        try {
            const devices = await navigator.mediaDevices.enumerateDevices();
            return devices.some(d => d.kind === 'videoinput');
        } catch {
            return false;
        }
    }

    async function iniciar(elementId, config, callbacks) {
        if (activa) return;

        try {
            instancia = new Html5Qrcode(elementId);
            await instancia.start(
                { facingMode: "environment" },
                config,
                callbacks.onSuccess,
                callbacks.onError
            );
            activa = true;
            return instancia;
        } catch (error) {
            console.error('Error al iniciar cámara:', error);
            throw error;
        }
    }

    async function detener() {
        if (instancia && activa) {
            try {
                await instancia.stop();
                activa = false;
                instancia = null;
            } catch (error) {
                console.error('Error al detener cámara:', error);
            }
        }
    }

    return {
        verificarDisponibilidad,
        iniciar,
        detener,
        isActiva: () => activa
    };
})();

window.Camara = Camara;