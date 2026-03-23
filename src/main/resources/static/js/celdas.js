// js/celdas.js
// MÓDULO DE GESTIÓN DE CELDAS - Iteración 2

class Celda {
    constructor(id, tipo, sector, estado) {
        this.id     = id;
        this.tipo   = tipo;
        this.sector = sector;
        this.estado = estado;
    }

    cambiarEstado(nuevoEstado) {
        this.estado = nuevoEstado;
        console.log(`Celda ${this.id} → ${nuevoEstado}`);
    }

    getPosicionVisual() {
        const num = parseInt(this.id.replace(/\D/g, '')) || 1;
        return {
            fila: (num % 10) + 1,
            columna: Math.floor(num / 10) + 1
        };
    }
}

// Módulo principal de gestión de celdas
const ParqueaderoModule = (function() {
    const celdas = [];

    function cargarCeldas() {
        celdas.length = 0; // limpia si se recarga
        celdas.push(
            new Celda("A-101", "Auto", "Piso 1", "Disponible"),
            new Celda("A-102", "Auto", "Piso 1", "Disponible"),
            new Celda("A-103", "Moto", "Piso 1", "Ocupada"),
            new Celda("B-201", "Auto", "Piso 2", "Disponible"),
            new Celda("B-202", "Auto", "Piso 2", "Mantenimiento"),
            new Celda("C-301", "Moto", "Piso 3", "Disponible")
        );
        renderGrid();
        renderSelect();
    }

    function renderGrid() {
        const grid = document.getElementById("gridCeldas");
        if (!grid) return;

        grid.innerHTML = "";

        celdas.forEach(celda => {
            const div = document.createElement("div");
            div.className = `celda ${celda.estado.toLowerCase()}`;
            div.innerHTML = `
                <strong>${celda.id}</strong><br>
                <small>${celda.tipo} • ${celda.sector}</small><br>
                <span style="color:${celda.estado === 'Disponible' ? 'green' : 'red'}">
                    ${celda.estado}
                </span>
            `;
            div.onclick = () => {
                if (celda.estado === "Disponible") {
                    const select = document.getElementById("celdaSelect");
                    if (select) select.value = celda.id;
                    alert(`Celda ${celda.id} seleccionada`);
                }
            };
            grid.appendChild(div);
        });
    }

    function renderSelect() {
        const select = document.getElementById("celdaSelect");
        if (!select) return;

        select.innerHTML = '<option value="">-- Seleccione celda disponible --</option>';

        celdas
            .filter(c => c.estado === "Disponible")
            .forEach(c => {
                const opt = document.createElement("option");
                opt.value = c.id;
                opt.textContent = `${c.id} (${c.tipo} - ${c.sector})`;
                select.appendChild(opt);
            });
    }

    function asignarCelda() {
        const placaInput = document.getElementById("placa");
        const select     = document.getElementById("celdaSelect");

        if (!placaInput || !select) {
            alert("❌ Elementos del formulario no encontrados");
            return;
        }

        const placa   = placaInput.value.trim().toUpperCase();
        const celdaId = select.value;

        if (!placa || !celdaId) {
            alert("❌ Complete placa y seleccione celda");
            return;
        }

        const celda = celdas.find(c => c.id === celdaId);
        if (celda) {
            celda.cambiarEstado("Ocupada");
            alert(`✅ Vehículo ${placa} asignado a celda ${celdaId}`);
            renderGrid();
            renderSelect();
        }
    }

    // API pública del módulo
    return {
        init: function() {
            cargarCeldas();
        },
        asignarCelda: asignarCelda
    };
})();

// Expone solo lo necesario al global para evitar contaminación de espacio de nombres
window.asignarCelda = ParqueaderoModule.asignarCelda;

// Inicializar cuando esté listo el DOM
if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', () => ParqueaderoModule.init());
} else {
    ParqueaderoModule.init();
}