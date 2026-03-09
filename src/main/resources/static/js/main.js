// ===================================================================
// AUTOSCOLOMBIA - JavaScript Principal
// ===================================================================

// Inicializar cuando el DOM esté listo
document.addEventListener('DOMContentLoaded', function() {

    // ===== Auto-cerrar alertas después de 5 segundos =====
    const alerts = document.querySelectorAll('.alert');
    alerts.forEach(alert => {
        setTimeout(() => {
            alert.style.opacity = '0';
            setTimeout(() => alert.remove(), 300);
        }, 5000);
    });

    // ===== Validación de placas en tiempo real =====
    const placaInputs = document.querySelectorAll('input[name="placa"]');
    placaInputs.forEach(input => {
        input.addEventListener('input', function(e) {
            // Convertir a mayúsculas automáticamente
            e.target.value = e.target.value.toUpperCase();

            // Validar formato (letras y números)
            if (e.target.value && !/^[A-Z0-9]+$/.test(e.target.value)) {
                e.target.style.borderColor = '#dc3545';
            } else {
                e.target.style.borderColor = '#667eea';
            }
        });
    });

    // ===== Validación de VIN (17 caracteres alfanuméricos) =====
    const vinInputs = document.querySelectorAll('input[name="vin"]');
    vinInputs.forEach(input => {
        input.addEventListener('input', function(e) {
            // Convertir a mayúsculas
            e.target.value = e.target.value.toUpperCase();

            // Validar longitud y formato
            if (e.target.value.length > 0 && e.target.value.length !== 17) {
                e.target.style.borderColor = '#ffc107';
            } else if (e.target.value.length === 17) {
                e.target.style.borderColor = '#28a745';
            } else {
                e.target.style.borderColor = '#667eea';
            }
        });
    });

    // ===== Validación de cédula (solo números) =====
    const cedulaInputs = document.querySelectorAll('input[name="cedula"]');
    cedulaInputs.forEach(input => {
        input.addEventListener('input', function(e) {
            // Solo permitir números
            e.target.value = e.target.value.replace(/[^0-9]/g, '');
        });
    });

    // ===== Validación de teléfono (solo números) =====
    const telefonoInputs = document.querySelectorAll('input[name="telefono"]');
    telefonoInputs.forEach(input => {
        input.addEventListener('input', function(e) {
            // Solo permitir números y guiones
            e.target.value = e.target.value.replace(/[^0-9-]/g, '');
        });
    });

    // ===== Animación de entrada para tarjetas del menú =====
    const menuCards = document.querySelectorAll('.menu-card');
    menuCards.forEach((card, index) => {
        card.style.opacity = '0';
        card.style.transform = 'translateY(20px)';
        setTimeout(() => {
            card.style.transition = 'all 0.5s ease';
            card.style.opacity = '1';
            card.style.transform = 'translateY(0)';
        }, index * 100);
    });

    // ===== Confirmación antes de cerrar sesión =====
    const logoutButtons = document.querySelectorAll('form[action*="logout"] button');
    logoutButtons.forEach(button => {
        button.addEventListener('click', function(e) {
            if (!confirm('¿Estás seguro de que deseas cerrar sesión?')) {
                e.preventDefault();
            }
        });
    });

    // ===== Confirmación antes de procesar salida =====
    const salidaForm = document.querySelector('form[action*="salida"]');
    if (salidaForm) {
        salidaForm.addEventListener('submit', function(e) {
            if (!confirm('¿Confirmar salida del vehículo y procesar cobro?')) {
                e.preventDefault();
            }
        });
    }

    // ===== Búsqueda en tabla (para página de parqueados) =====
    const searchInput = document.getElementById('tableSearch');
    if (searchInput) {
        searchInput.addEventListener('keyup', function() {
            const searchValue = this.value.toLowerCase();
            const tableRows = document.querySelectorAll('tbody tr');

            tableRows.forEach(row => {
                const text = row.textContent.toLowerCase();
                if (text.includes(searchValue)) {
                    row.style.display = '';
                } else {
                    row.style.display = 'none';
                }
            });
        });
    }

    // ===== Actualizar hora actual en tiempo real =====
    const currentTimeElement = document.getElementById('currentTime');
    if (currentTimeElement) {
        function updateTime() {
            const now = new Date();
            const options = {
                weekday: 'long',
                year: 'numeric',
                month: 'long',
                day: 'numeric',
                hour: '2-digit',
                minute: '2-digit',
                second: '2-digit'
            };
            currentTimeElement.textContent = now.toLocaleDateString('es-CO', options);
        }
        updateTime();
        setInterval(updateTime, 1000);
    }

    // ===== Tooltip simple =====
    const tooltips = document.querySelectorAll('[data-tooltip]');
    tooltips.forEach(element => {
        element.addEventListener('mouseenter', function() {
            const tooltip = document.createElement('div');
            tooltip.className = 'custom-tooltip';
            tooltip.textContent = this.getAttribute('data-tooltip');
            document.body.appendChild(tooltip);

            const rect = this.getBoundingClientRect();
            tooltip.style.position = 'absolute';
            tooltip.style.top = (rect.top - tooltip.offsetHeight - 5) + 'px';
            tooltip.style.left = (rect.left + rect.width / 2 - tooltip.offsetWidth / 2) + 'px';
        });

        element.addEventListener('mouseleave', function() {
            const tooltip = document.querySelector('.custom-tooltip');
            if (tooltip) tooltip.remove();
        });
    });

    console.log('✅ AutosColombia JS cargado correctamente');
});

// ===== Funciones Globales =====

// Formatear número como moneda
function formatCurrency(amount) {
    return new Intl.NumberFormat('es-CO', {
        style: 'currency',
        currency: 'COP'
    }).format(amount);
}

// Calcular tiempo transcurrido
function calcularTiempoTranscurrido(horaIngreso) {
    const ingreso = new Date(horaIngreso);
    const ahora = new Date();
    const diff = ahora - ingreso;

    const horas = Math.floor(diff / (1000 * 60 * 60));
    const minutos = Math.floor((diff % (1000 * 60 * 60)) / (1000 * 60));

    return `${horas}h ${minutos}m`;
}

