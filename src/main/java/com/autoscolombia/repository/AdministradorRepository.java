package com.autoscolombia.repository;

import com.autoscolombia.domain.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdministradorRepository extends JpaRepository<Administrador, Integer> {
    Optional<Administrador> findByNombreUsuario(String nombreUsuario);
    boolean existsByEmail(String email);
    boolean existsByDocumento(String documento);
    boolean existsByEmailAndIdUsuarioNot(String email, Integer id);
    boolean existsByDocumentoAndIdUsuarioNot(String documento, Integer id);
}