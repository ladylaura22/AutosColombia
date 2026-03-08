package com.autoscolombia.service;

import com.autoscolombia.domain.*;
import com.autoscolombia.repository.*;
import com.autoscolombia.web.dto.IngresoForm;
import com.autoscolombia.web.dto.SalidaForm;
import org.springframework.security.core.Authentication;
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

    public ParqueaderoService(
            VehiculoRepository vehiculoRepository,
            ClienteRepository clienteRepository,
            AdministradorRepository administradorRepository,
            RegistroParqueoRepository registroParqueoRepository,
            MensualidadRepository mensualidadRepository
    ) {
        this.vehiculoRepository = vehiculoRepository;
        this.clienteRepository = clienteRepository;
        this.administradorRepository = administradorRepository;
        this.registroParqueoRepository = registroParqueoRepository;
        this.mensualidadRepository = mensualidadRepository;
    }

    @Transactional
    public RegistroParqueo registrarIngreso(IngresoForm form) {
        validarIngreso(form);

        // No permitir un ingreso si ya existe uno activo
        registroParqueoRepository.findActivoByPlaca(form.getPlaca()).ifPresent(rp -> {
            throw new IllegalStateException("Ya existe un ingreso activo para esa placa.");
        });

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

        Administrador admin = obtenerAdminAutenticado();

        boolean mensualActiva = mensualidadRepository.tieneMensualidadActiva(vehiculo.getPlaca(), LocalDate.now());
        MetodoCobro metodoCobro = mensualActiva ? MetodoCobro.mensual : MetodoCobro.horas;

        RegistroParqueo rp = new RegistroParqueo();
        rp.setVehiculo(vehiculo);
        rp.setCliente(cliente);
        rp.setAdministrador(admin);
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
                .orElseThrow(() -> new IllegalArgumentException("No existe vehículo con placa: " + form.getPlaca()));

        if (!vehiculo.getVin().equalsIgnoreCase(form.getVin().trim())) {
            throw new IllegalArgumentException("VIN no coincide con el vehículo de esa placa.");
        }

        RegistroParqueo rp = registroParqueoRepository.findActivoByPlaca(form.getPlaca())
                .orElseThrow(() -> new IllegalStateException("No existe un ingreso activo para esa placa."));

        rp.setHoraSalida(LocalDateTime.now());
        rp.setLugarActual(LugarActual.fuera);

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

    private Administrador obtenerAdminAutenticado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return administradorRepository.findByNombreUsuario(username)
                .orElseThrow(() -> new IllegalStateException("Administrador autenticado no encontrado: " + username));
    }

    private void validarIngreso(IngresoForm form) {
        if (form.getPlaca() == null || form.getPlaca().isBlank()) throw new IllegalArgumentException("Placa obligatoria");
        if (form.getVin() == null || form.getVin().isBlank()) throw new IllegalArgumentException("VIN obligatorio");
        if (form.getTipoVehiculo() == null) throw new IllegalArgumentException("Tipo de vehículo obligatorio");
        if (form.getCedula() == null || form.getCedula().isBlank()) throw new IllegalArgumentException("Cédula obligatoria");
        if (form.getNombre() == null || form.getNombre().isBlank()) throw new IllegalArgumentException("Nombre obligatorio");
        if (form.getTelefono() == null || form.getTelefono().isBlank()) throw new IllegalArgumentException("Teléfono obligatorio");

        String vin = form.getVin().trim().toUpperCase();
        if (!vin.matches("^[A-HJ-NPR-Z0-9]{17}$")) {
            throw new IllegalArgumentException("VIN inválido: 17 alfanuméricos sin I, O, Q.");
        }
    }
}