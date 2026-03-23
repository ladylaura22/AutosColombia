package com.autoscolombia.repository;

import com.autoscolombia.domain.Celda;
import com.autoscolombia.domain.EstadoCelda;
import com.autoscolombia.domain.TipoCelda;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CeldaRepository extends JpaRepository<Celda, Integer> {
    Optional<Celda> findByCodigo(String codigo);
    boolean existsByCodigo(String codigo);
    List<Celda> findByEstado(EstadoCelda estado);
    List<Celda> findByEstadoAndTipo(EstadoCelda estado, TipoCelda tipo);
}
