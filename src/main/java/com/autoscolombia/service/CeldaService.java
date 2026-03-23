package com.autoscolombia.service;

import com.autoscolombia.domain.Celda;
import com.autoscolombia.domain.EstadoCelda;
import com.autoscolombia.domain.TipoCelda;
import com.autoscolombia.repository.CeldaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class CeldaService {

    private final CeldaRepository celdaRepository;

    public CeldaService(CeldaRepository celdaRepository) {
        this.celdaRepository = celdaRepository;
    }

    @Transactional(readOnly = true)
    public List<Celda> listarTodas() {
        return celdaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Celda> listarDisponibles(TipoCelda tipo) {
        if (tipo != null) {
            return celdaRepository.findByEstadoAndTipo(EstadoCelda.DISPONIBLE, tipo);
        }
        return celdaRepository.findByEstado(EstadoCelda.DISPONIBLE);
    }

    @Transactional
    public Celda crearCelda(String codigo, TipoCelda tipo, String ubicacion) {
        if (celdaRepository.existsByCodigo(codigo)) {
            throw new IllegalArgumentException("Ya existe una celda con el código: " + codigo);
        }
        Celda c = new Celda();
        c.setCodigo(codigo.trim().toUpperCase());
        c.setTipo(tipo);
        c.setUbicacion(ubicacion);
        c.setEstado(EstadoCelda.DISPONIBLE);
        return celdaRepository.save(c);
    }

    @Transactional
    public Celda cambiarEstado(Integer id, EstadoCelda nuevoEstado,
                               String motivoMantenimiento, LocalDate fechaEstimadaDisponible) {
        Celda c = celdaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Celda no encontrada: " + id));
        c.setEstado(nuevoEstado);
        if (nuevoEstado == EstadoCelda.MANTENIMIENTO) {
            c.setMotivoMantenimiento(motivoMantenimiento);
            c.setFechaEstimadaDisponible(fechaEstimadaDisponible);
        } else {
            c.setMotivoMantenimiento(null);
            c.setFechaEstimadaDisponible(null);
        }
        return celdaRepository.save(c);
    }

    @Transactional
    public Celda asignar(Integer id) {
        Celda c = celdaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Celda no encontrada: " + id));
        if (c.getEstado() != EstadoCelda.DISPONIBLE) {
            throw new IllegalStateException("La celda " + c.getCodigo() + " no está disponible.");
        }
        c.setEstado(EstadoCelda.OCUPADO);
        return celdaRepository.save(c);
    }

    @Transactional
    public void liberar(Integer id) {
        celdaRepository.findById(id).ifPresent(c -> {
            if (c.getEstado() == EstadoCelda.OCUPADO) {
                c.setEstado(EstadoCelda.DISPONIBLE);
                celdaRepository.save(c);
            }
        });
    }

    @Transactional(readOnly = true)
    public Celda obtenerPorId(Integer id) {
        return celdaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Celda no encontrada: " + id));
    }
}
