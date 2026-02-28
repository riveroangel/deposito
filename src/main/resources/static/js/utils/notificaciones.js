// Sistema de notificaciones unificado
const Notificaciones = (function() {
    function mostrar(mensaje, tipo = 'exito', duracion = 3000) {
        const toastContainer = document.getElementById('toastContainer');
        if (!toastContainer) {
            console.warn('Toast container no encontrado');
            return;
        }

        const toast = document.createElement('div');
        toast.className = `toast-notification ${tipo}`;
        toast.textContent = mensaje;
        toastContainer.appendChild(toast);

        setTimeout(() => {
            toast.classList.add('fade-out');
            setTimeout(() => toast.remove(), 500);
        }, duracion);
    }

    function error(mensaje) {
        mostrar(mensaje, 'error');
    }

    function exito(mensaje) {
        mostrar(mensaje, 'exito');
    }

    return {
        mostrar,
        error,
        exito
    };
})();

// Exportar para usar en otros archivos
window.Notificaciones = Notificaciones;