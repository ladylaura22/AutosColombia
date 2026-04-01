package com.autoscolombia.service;

import com.autoscolombia.domain.Tarifa;
import com.autoscolombia.repository.TarifaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class TarifaService {

    private final TarifaRepository tarifaRepository;

    public TarifaService(TarifaRepository tarifaRepository) {
        this.tarifaRepository = tarifaRepository;
    }

    @Transactional(readOnly = true)
    public Tarifa obtenerTarifaActiva() {
        return tarifaRepository.findFirstByActivoTrueOrderByIdTarifaDesc()
                .orElseThrow(() -> new IllegalStateException("No hay tarifa activa configurada"));
    }

    public Tarifa actualizarTarifa(BigDecimal horaCarro, BigDecimal horaMoto, BigDecimal mensual) {
        if (horaCarro == null || horaCarro.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Tarifa hora carro debe ser mayor a 0");
        if (horaMoto == null || horaMoto.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Tarifa hora moto debe ser mayor a 0");
        if (mensual == null || mensual.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Tarifa mensual debe ser mayor a 0");

        Tarifa t = tarifaRepository.findFirstByActivoTrueOrderByIdTarifaDesc()
                .orElse(new Tarifa());
        t.setTarifaHoraCarro(horaCarro);
        t.setTarifaHoraMoto(horaMoto);
        t.setTarifaMensual(mensual);
        t.setActivo(true);
        if (t.getCreadoEn() == null) t.setCreadoEn(LocalDateTime.now());
        t.setModificadoEn(LocalDateTime.now());
        return tarifaRepository.save(t);
    }

    @Transactional(readOnly = true)
    public List<Tarifa> listarTarifas() {
        return tarifaRepository.findAll();
    }
}
