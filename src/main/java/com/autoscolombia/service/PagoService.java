package com.autoscolombia.service;

import com.autoscolombia.domain.*;
import com.autoscolombia.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PagoService {

    private final PagoRepository pagoRepository;
    private final RegistroParqueoRepository registroParqueoRepository;
    private final MensualidadRepository mensualidadRepository;
    private final VehiculoRepository vehiculoRepository;
    private final TarifaService tarifaService;

    public PagoService(PagoRepository pagoRepository,
                       RegistroParqueoRepository registroParqueoRepository,
                       MensualidadRepository mensualidadRepository,
                       VehiculoRepository vehiculoRepository,
                       TarifaService tarifaService) {
        this.pagoRepository = pagoRepository;
        this.registroParqueoRepository = registroParqueoRepository;
        this.mensualidadRepository = mensualidadRepository;
        this.vehiculoRepository = vehiculoRepository;
        this.tarifaService = tarifaService;
    }

    public Pago registrarPagoHoras(Integer idRegistro, MetodoPago metodo, String referencia, String usuarioActual) {
        RegistroParqueo registro = registroParqueoRepository.findById(idRegistro)
                .orElseThrow(() -> new IllegalArgumentException("Registro no encontrado: " + idRegistro));

        pagoRepository.findByRegistroParqueoIdRegistroAndEstado(idRegistro, EstadoPago.PAGADO)
                .ifPresent(p -> { throw new IllegalStateException("Este registro ya tiene un pago registrado"); });

        if (registro.getMetodoCobro() == null || registro.getMetodoCobro() != MetodoCobro.horas) {
            throw new IllegalStateException("Este registro no corresponde a cobro por horas");
        }

        LocalDateTime ingreso = registro.getHoraIngreso();
        LocalDateTime salida = registro.getHoraSalida();
        if (salida == null) {
            salida = LocalDateTime.now();
            registro.setHoraSalida(salida);
            registroParqueoRepository.save(registro);
        }

        long minutos = ChronoUnit.MINUTES.between(ingreso, salida);
        long horas = (long) Math.ceil(minutos / 60.0);
        if (horas < 1) horas = 1;

        Tarifa tarifa = tarifaService.obtenerTarifaActiva();
        BigDecimal tarifaHora;
        if (registro.getVehiculo() != null && registro.getVehiculo().getTipoVehiculo() == TipoVehiculo.Moto) {
            tarifaHora = tarifa.getTarifaHoraMoto();
        } else {
            tarifaHora = tarifa.getTarifaHoraCarro();
        }

        BigDecimal valor = tarifaHora.multiply(BigDecimal.valueOf(horas));

        Pago pago = new Pago();
        pago.setTipoPago(TipoPago.HORAS);
        pago.setMetodoPago(metodo);
        pago.setReferencia(referencia);
        pago.setValor(valor);
        pago.setEstado(EstadoPago.PAGADO);
        pago.setRegistroParqueo(registro);
        pago.setCreadoPor(usuarioActual);
        pago.setCreadoEn(LocalDateTime.now());

        return pagoRepository.save(pago);
    }

    public Pago registrarPagoMensualidad(String placa, MetodoPago metodo, String referencia, String usuarioActual) {
        Vehiculo vehiculo = vehiculoRepository.findById(placa)
                .orElseThrow(() -> new IllegalArgumentException("Vehículo no encontrado: " + placa));

        List<Mensualidad> activas = mensualidadRepository.findActivasByPlaca(placa, LocalDate.now());
        for (Mensualidad m : activas) {
            m.setEstaActiva(false);
            mensualidadRepository.save(m);
        }

        Mensualidad nueva = new Mensualidad();
        nueva.setVehiculo(vehiculo);
        nueva.setFechaInicio(LocalDate.now());
        nueva.setFechaFin(LocalDate.now().plusDays(30));
        nueva.setEstaActiva(true);
        nueva = mensualidadRepository.save(nueva);

        Tarifa tarifa = tarifaService.obtenerTarifaActiva();
        BigDecimal valor = tarifa.getTarifaMensual();

        Pago pago = new Pago();
        pago.setTipoPago(TipoPago.MENSUALIDAD);
        pago.setMetodoPago(metodo);
        pago.setReferencia(referencia);
        pago.setValor(valor);
        pago.setEstado(EstadoPago.PAGADO);
        pago.setMensualidad(nueva);
        pago.setCreadoPor(usuarioActual);
        pago.setCreadoEn(LocalDateTime.now());

        return pagoRepository.save(pago);
    }

    public Pago anularPago(Integer idPago, String motivo, String usuarioActual) {
        Pago pago = pagoRepository.findById(idPago)
                .orElseThrow(() -> new IllegalArgumentException("Pago no encontrado: " + idPago));
        if (pago.getEstado() == EstadoPago.ANULADO) {
            throw new IllegalStateException("El pago ya está anulado");
        }
        pago.setEstado(EstadoPago.ANULADO);
        pago.setAnuladoPor(usuarioActual);
        pago.setAnuladoEn(LocalDateTime.now());
        pago.setMotivoAnulacion(motivo);
        return pagoRepository.save(pago);
    }

    @Transactional(readOnly = true)
    public List<Pago> buscarConFiltros(LocalDate desde, LocalDate hasta, String placa,
                                        TipoPago tipoPago, MetodoPago metodoPago, EstadoPago estado) {
        LocalDateTime desdedt = (desde != null) ? desde.atStartOfDay() : null;
        LocalDateTime hastaDt = (hasta != null) ? hasta.atTime(23, 59, 59) : null;

        List<Pago> pagos = pagoRepository.buscarConFiltros(desdedt, hastaDt, tipoPago, metodoPago, estado);

        if (placa != null && !placa.isBlank()) {
            String placaUpper = placa.trim().toUpperCase();
            pagos = pagos.stream().filter(p -> {
                String pv = null;
                if (p.getRegistroParqueo() != null && p.getRegistroParqueo().getVehiculo() != null) {
                    pv = p.getRegistroParqueo().getVehiculo().getPlaca();
                } else if (p.getMensualidad() != null && p.getMensualidad().getVehiculo() != null) {
                    pv = p.getMensualidad().getVehiculo().getPlaca();
                }
                return placaUpper.equals(pv);
            }).collect(Collectors.toList());
        }

        return pagos;
    }

    @Transactional(readOnly = true)
    public Pago obtenerPorId(Integer id) {
        return pagoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pago no encontrado: " + id));
    }

    @Transactional(readOnly = true)
    public List<Pago> listarTodos() {
        return pagoRepository.findAll();
    }
}
