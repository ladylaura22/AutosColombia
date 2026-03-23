package com.autoscolombia.service;

import com.autoscolombia.domain.*;
import com.autoscolombia.repository.*;
import com.autoscolombia.web.dto.IngresoForm;
import com.autoscolombia.web.dto.SalidaForm;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.List;

@Service
public class ParqueaderoService {

    private final VehiculoRepository vehiculoRepository;
    private final ClienteRepository clienteRepository;
    private final AdministradorRepository administradorRepository;
    private final RegistroParqueoRepository registroParqueoRepository;
    private final MensualidadRepository mensualidadRepository;
    private final CeldaRepository celdaRepository;

    public ParqueaderoService(
            VehiculoRepository vehiculoRepository,
            ClienteRepository clienteRepository,
            AdministradorRepository administradorRepository,
            RegistroParqueoRepository registroParqueoRepository,
            MensualidadRepository mensualidadRepository,
            CeldaRepository celdaRepository
    ) {
        this.vehiculoRepository = vehiculoRepository;
        this.clienteRepository = clienteRepository;
        this.administradorRepository = administradorRepository;
        this.registroParqueoRepository = registroParqueoRepository;
        this.mensualidadRepository = mensualidadRepository;
        this.celdaRepository = celdaRepository;
    }

    @Transactional
    public RegistroParqueo registrarIngreso(IngresoForm form) {
        validarIngreso(form);

        // No permitir un ingreso si ya existe uno activo
        registroParqueoRepository.findActivoByPlaca(form.getPlaca()).ifPresent(rp -> {
            throw new IllegalStateException("Ya existe un ingreso activo para esa placa.");
        });

        // Celda obligatoria
        if (form.getIdCelda() == null) {
            throw new IllegalArgumentException("Debe seleccionar una celda.");
        }
        Celda celda = celdaRepository.findById(form.getIdCelda())
                .orElseThrow(() -> new IllegalArgumentException("Celda no encontrada."));
        if (celda.getEstado() != EstadoCelda.DISPONIBLE) {
            throw new IllegalStateException("La celda seleccionada no está disponible.");
        }
        celda.setEstado(EstadoCelda.OCUPADO);
        celdaRepository.save(celda);

        // Vehiculo (crear o actualizar)
        Vehiculo vehiculo = vehiculoRepository.findById(form.getPlaca()).orElseGet(Vehiculo::new);
        vehiculo.setPlaca(form.getPlaca());
        vehiculo.setVin(form.getVin().trim().toUpperCase());
        vehiculo.setTipoVehiculo(form.getTipoVehiculo());
        vehiculo = vehiculoRepository.save(vehiculo);

        // Cliente (reusar por cedula)
        Cliente cliente = clienteRepository.findByCedula(form.getCedula()).orElseGet(Cliente::new);
        cliente.setCedula(form.getCedula());
        cliente.setNombre(form.getNombre());
        cliente.setTelefono(form.getTelefono());
        cliente = clienteRepository.save(cliente);

        // Admin (opcional: solo si el usuario autenticado es ADMIN)
        Administrador admin = obtenerAdminSiCorresponde();

        boolean mensualActiva = mensualidadRepository.tieneMensualidadActiva(vehiculo.getPlaca(), LocalDate.now());
        MetodoCobro metodoCobro = mensualActiva ? MetodoCobro.mensual : MetodoCobro.horas;

        RegistroParqueo rp = new RegistroParqueo();
        rp.setVehiculo(vehiculo);
        rp.setCliente(cliente);
        rp.setAdministrador(admin);
        rp.setCelda(celda);
        rp.setHoraIngreso(LocalDateTime.now());
        rp.setHoraSalida(null);
        rp.setLugarActual(LugarActual.parqueadero);
        rp.setMetodoCobro(metodoCobro);

        return registroParqueoRepository.save(rp);
    }

    @Transactional
    public long procesarSalida(SalidaForm form) {
        if (form.getPlaca() == null || form.getPlaca().isBlank()
                || form.getVin() == null || form.getVin().isBlank()) {
            throw new IllegalArgumentException("Placa y VIN son obligatorios para la salida.");
        }

        Vehiculo vehiculo = vehiculoRepository.findById(form.getPlaca())
                .orElseThrow(() -> new IllegalArgumentException("No existe vehiculo con placa: " + form.getPlaca()));

        if (!vehiculo.getVin().equalsIgnoreCase(form.getVin().trim())) {
            throw new IllegalArgumentException("VIN no coincide con el vehiculo de esa placa.");
        }

        RegistroParqueo rp = registroParqueoRepository.findActivoByPlaca(form.getPlaca())
                .orElseThrow(() -> new IllegalStateException("No existe un ingreso activo para esa placa."));

        rp.setHoraSalida(LocalDateTime.now());
        rp.setLugarActual(LugarActual.fuera);

        // Liberar celda si tiene una asignada
        if (rp.getCelda() != null) {
            Celda celda = rp.getCelda();
            if (celda.getEstado() == EstadoCelda.OCUPADO) {
                celda.setEstado(EstadoCelda.DISPONIBLE);
                celdaRepository.save(celda);
            }
        }

        // (Opcional) revalidar mensualidad en salida
        boolean mensualActiva = mensualidadRepository.tieneMensualidadActiva(form.getPlaca(), LocalDate.now());
        rp.setMetodoCobro(mensualActiva ? MetodoCobro.mensual : MetodoCobro.horas);

        registroParqueoRepository.save(rp);

        return Duration.between(rp.getHoraIngreso(), rp.getHoraSalida()).toMinutes();
    }

    @Transactional(readOnly = true)
    public RegistroParqueo buscarUltimoRegistro(String placa) {
        List<RegistroParqueo> lista = registroParqueoRepository.findUltimosPorPlaca(placa);
        if (lista.isEmpty()) {
            throw new IllegalArgumentException("No hay registros para la placa: " + placa);
        }
        return lista.get(0);
    }

    @Transactional(readOnly = true)
    public List<RegistroParqueo> listarParqueados() {
        return registroParqueoRepository.findByLugarActual(LugarActual.parqueadero);
    }

    private Administrador obtenerAdminSiCorresponde() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch("ROLE_ADMIN"::equals);
        if (isAdmin) {
            return administradorRepository.findByNombreUsuario(auth.getName()).orElse(null);
        }
        return null;
    }

    private void validarIngreso(IngresoForm form) {
        if (form.getPlaca() == null || form.getPlaca().isBlank()) throw new IllegalArgumentException("Placa obligatoria");
        if (form.getVin() == null || form.getVin().isBlank()) throw new IllegalArgumentException("VIN obligatorio");
        if (form.getTipoVehiculo() == null) throw new IllegalArgumentException("Tipo de vehiculo obligatorio");
        if (form.getCedula() == null || form.getCedula().isBlank()) throw new IllegalArgumentException("Cedula obligatoria");
        if (form.getNombre() == null || form.getNombre().isBlank()) throw new IllegalArgumentException("Nombre obligatorio");
        if (form.getTelefono() == null || form.getTelefono().isBlank()) throw new IllegalArgumentException("Telefono obligatorio");

        String vin = form.getVin().trim().toUpperCase();
        if (!vin.matches("^[A-HJ-NPR-Z0-9]{17}$")) {
            throw new IllegalArgumentException("VIN invalido: 17 alfanumericos sin I, O, Q.");
        }
    }
}
