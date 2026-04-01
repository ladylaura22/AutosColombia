package com.autoscolombia.repository;

import com.autoscolombia.domain.Tarifa;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TarifaRepository extends JpaRepository<Tarifa, Integer> {
    Optional<Tarifa> findFirstByActivoTrueOrderByIdTarifaDesc();
}
