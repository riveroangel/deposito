const API = "/api/productos";

function buscar() {
    const codigo = document.getElementById("codigo").value.trim();
    const resultado = document.getElementById("resultado");

    if (!codigo) {
        resultado.textContent = "Ingresá un código";
        return;
    }

    resultado.textContent = "Buscando...";

    fetch(API + "/codigo/" + codigo)
        .then(res => {
            if (!res.ok) {
                throw new Error("No encontrado");
            }
            return res.json();
        })
        .then(p => {
            resultado.innerHTML = `
                <b>${p.nombre}</b><br>
                Código: ${p.codigoBarra}<br>
                Precio: $${p.precioVenta}<br>
                Stock: ${p.stock}<br>
                Categoría: ${p.categoria}
            `;
        })
        .catch(() => {
            resultado.innerHTML =
                "<span style='color:red'>Producto no encontrado</span>";
        });
}
