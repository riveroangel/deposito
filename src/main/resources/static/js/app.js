// Archivo principal de la aplicación
(function() {
    'use strict';

    console.log('🚀 Gamasonic App iniciada');

    // Configuración global
    const App = {
        version: '2.0.0',
        nombre: 'Gamasonic Mobile',

        init() {
            this.configurarGlobales();
            this.cargarModulos();
        },

        configurarGlobales() {
            // Prevenir zoom no deseado en inputs
            document.addEventListener('touchstart', (e) => {
                if (e.target.tagName === 'INPUT') {
                    e.target.style.fontSize = '16px';
                }
            });
        },

        cargarModulos() {
            // Los módulos se inicializan solos con DOMContentLoaded
            console.log('📦 Módulos cargados');
        },

        // Utilidades globales
        utils: {
            formatearMoneda: (valor) => `$${valor.toFixed(2)}`,
            capitalize: (str) => str.charAt(0).toUpperCase() + str.slice(1).toLowerCase()
        }
    };

    window.App = App;
    App.init();
})();