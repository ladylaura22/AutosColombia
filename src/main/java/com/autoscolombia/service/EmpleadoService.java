package com.autoscolombia.service;

import com.autoscolombia.domain.Empleado;
import com.autoscolombia.repository.EmpleadoRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EmpleadoService {

    private final EmpleadoRepository empleadoRepository;
    private final PasswordEncoder passwordEncoder;

    public EmpleadoService(EmpleadoRepository empleadoRepository, PasswordEncoder passwordEncoder) {
        this.empleadoRepository = empleadoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public List<Empleado> listarTodos() {
        return empleadoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Empleado obtenerPorId(Integer id) {
        return empleadoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Empleado no encontrado: " + id));
    }

    @Transactional
    public Empleado crear(String nombreCompleto, String documento, String email,
                          String telefono, String contrasenaPlana) {
        validarEmailUnico(email, null);
        validarDocumentoUnico(documento, null);

        Empleado e = new Empleado();
        e.setNombreCompleto(nombreCompleto.trim());
        e.setDocumento(documento.trim());
        e.setEmail(email.trim().toLowerCase());
        e.setTelefono(telefono != null ? telefono.trim() : null);
        e.setContrasenaHash(passwordEncoder.encode(contrasenaPlana));
        return empleadoRepository.save(e);
    }

    @Transactional
    public Empleado actualizar(Integer id, String nombreCompleto, String documento,
                               String email, String telefono, String nuevaContrasena) {
        Empleado e = obtenerPorId(id);
        validarEmailUnico(email, id);
        validarDocumentoUnico(documento, id);

        e.setNombreCompleto(nombreCompleto.trim());
        e.setDocumento(documento.trim());
        e.setEmail(email.trim().toLowerCase());
        e.setTelefono(telefono != null ? telefono.trim() : null);
        if (nuevaContrasena != null && !nuevaContrasena.isBlank()) {
            e.setContrasenaHash(passwordEncoder.encode(nuevaContrasena));
        }
        return empleadoRepository.save(e);
    }

    @Transactional
    public void eliminar(Integer id) {
        if (!empleadoRepository.existsById(id)) {
            throw new IllegalArgumentException("Empleado no encontrado: " + id);
        }
        empleadoRepository.deleteById(id);
    }

    private void validarEmailUnico(String email, Integer excludeId) {
        if (email == null || !email.matches("^[\\w.+\\-]+@[\\w\\-]+\\.[a-zA-Z]{2,}$")) {
            throw new IllegalArgumentException("Email inválido: " + email);
        }
        boolean exists = excludeId == null
                ? empleadoRepository.existsByEmail(email.trim().toLowerCase())
                : empleadoRepository.existsByEmailAndIdEmpleadoNot(email.trim().toLowerCase(), excludeId);
        if (exists) {
            throw new IllegalArgumentException("Ya existe un empleado con el email: " + email);
        }
    }

    private void validarDocumentoUnico(String documento, Integer excludeId) {
        if (documento == null || documento.isBlank()) {
            throw new IllegalArgumentException("Documento obligatorio.");
        }
        boolean exists = excludeId == null
                ? empleadoRepository.existsByDocumento(documento.trim())
                : empleadoRepository.existsByDocumentoAndIdEmpleadoNot(documento.trim(), excludeId);
        if (exists) {
            throw new IllegalArgumentException("Ya existe un empleado con el documento: " + documento);
        }
    }
}
