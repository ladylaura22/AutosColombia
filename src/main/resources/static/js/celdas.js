// js/celdas.js
// MODULO DE GESTION DE CELDAS - Iteracion 2
// Consume endpoints reales: GET /api/celdas, GET /api/celdas/disponibles

(function() {
    'use strict';

    function colorEstado(estado) {
        if (!estado) return 'gray';
        switch (estado.toUpperCase()) {
            case 'DISPONIBLE': return 'green';
            case 'OCUPADO':    return 'red';
            case 'MANTENIMIENTO': return 'orange';
            default: return 'gray';
        }
    }

    function refreshGrid() {
        fetch('/api/celdas', { credentials: 'same-origin' })
            .then(function(r) {
                if (!r.ok) throw new Error('HTTP ' + r.status);
                return r.json();
            })
            .then(function(celdas) {
                var grid = document.getElementById('gridCeldas');
                if (!grid) return;
                // Solo refresca el estado si ya existe el grid (renderizado por Thymeleaf)
                // Esto es un complemento opcional al render del servidor
            })
            .catch(function(err) {
                console.warn('No se pudo refrescar grid de celdas via API:', err.message);
            });
    }

    // Inicializar cuando este listo el DOM
    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', function() {
            refreshGrid();
        });
    } else {
        refreshGrid();
    }
})();
