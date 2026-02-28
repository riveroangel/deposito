// Versión simple del escáner (para pruebas)
let html5QrCode;

window.empezarEscaneoSimple = async function() {
    const reader = document.getElementById('reader');
    reader.classList.remove('hidden');

    html5QrCode = new Html5Qrcode("reader");

    try {
        await html5QrCode.start(
            { facingMode: "environment" },
            { fps: 10, qrbox: 250 },
            (decodedText) => {
                document.getElementById('codigoBarra').value = decodedText;
                detenerCamaraSimple();
                window.navigator.vibrate?.(100);
            }
        );
    } catch (err) {
        alert("Error de cámara: " + err.message);
    }
};

window.detenerCamaraSimple = async function() {
    if (html5QrCode) {
        await html5QrCode.stop();
        document.getElementById('reader').classList.add('hidden');
    }
};