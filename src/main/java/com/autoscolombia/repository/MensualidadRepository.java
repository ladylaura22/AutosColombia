package com.autoscolombia.repository;

import com.autoscolombia.domain.Mensualidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;

public interface MensualidadRepository extends JpaRepository<Mensualidad, Integer> {

    @Query("""
            select (count(m) > 0)
            from Mensualidad m
            where m.vehiculo.placa = :placa
              and m.estaActiva = true
              and :hoy between m.fechaInicio and m.fechaFin
            """)
    boolean tieneMensualidadActiva(String placa, LocalDate hoy);
}