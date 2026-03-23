package com.autoscolombia.repository;

import com.autoscolombia.domain.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmpleadoRepository extends JpaRepository<Empleado, Integer> {
    Optional<Empleado> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByDocumento(String documento);
    boolean existsByEmailAndIdEmpleadoNot(String email, Integer id);
    boolean existsByDocumentoAndIdEmpleadoNot(String documento, Integer id);
}
