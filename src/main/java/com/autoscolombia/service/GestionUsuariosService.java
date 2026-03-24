package com.autoscolombia.service;

import com.autoscolombia.domain.Administrador;
import com.autoscolombia.domain.Empleado;
import com.autoscolombia.repository.AdministradorRepository;
import com.autoscolombia.repository.EmpleadoRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GestionUsuariosService {

    private final AdministradorRepository adminRepository;
    private final EmpleadoRepository empleadoRepository;
    private final PasswordEncoder passwordEncoder;

    public GestionUsuariosService(AdministradorRepository adminRepository,
                                   EmpleadoRepository empleadoRepository,
                                   PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.empleadoRepository = empleadoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ---- Administrador ----

    @Transactional(readOnly = true)
    public List<Administrador> listarAdmins() {
        return adminRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Administrador obtenerAdmin(Integer id) {
        return adminRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Administrador no encontrado: " + id));
    }

    @Transactional
    public Administrador crearAdmin(String nombreUsuario, String nombreCompleto, String documento,
                                    String email, String telefono, String contrasenaPlana) {
        validarEmailAdmin(email, null);
        validarDocumentoAdmin(documento, null);
        if (nombreUsuario == null || nombreUsuario.isBlank()) {
            throw new IllegalArgumentException("Nombre de usuario obligatorio.");
        }
        Administrador a = new Administrador();
        a.setNombreUsuario(nombreUsuario.trim());
        a.setNombreCompleto(nombreCompleto != null ? nombreCompleto.trim() : null);
        a.setDocumento(documento != null ? documento.trim() : null);
        a.setEmail(email != null ? email.trim().toLowerCase() : null);
        a.setTelefono(telefono != null ? telefono.trim() : null);
        a.setContrasenaHash(passwordEncoder.encode(contrasenaPlana));
        return adminRepository.save(a);
    }

    @Transactional
    public Administrador actualizarAdmin(Integer id, String nombreUsuario, String nombreCompleto,
                                         String documento, String email, String telefono,
                                         String nuevaContrasena) {
        Administrador a = obtenerAdmin(id);
        validarEmailAdmin(email, id);
        validarDocumentoAdmin(documento, id);
        if (nombreUsuario == null || nombreUsuario.isBlank()) {
            throw new IllegalArgumentException("Nombre de usuario obligatorio.");
        }
        a.setNombreUsuario(nombreUsuario.trim());
        a.setNombreCompleto(nombreCompleto != null ? nombreCompleto.trim() : null);
        a.setDocumento(documento != null ? documento.trim() : null);
        a.setEmail(email != null ? email.trim().toLowerCase() : null);
        a.setTelefono(telefono != null ? telefono.trim() : null);
        if (nuevaContrasena != null && !nuevaContrasena.isBlank()) {
            a.setContrasenaHash(passwordEncoder.encode(nuevaContrasena));
        }
        return adminRepository.save(a);
    }

    @Transactional
    public void eliminarAdmin(Integer id) {
        if (!adminRepository.existsById(id)) {
            throw new IllegalArgumentException("Administrador no encontrado: " + id);
        }
        adminRepository.deleteById(id);
    }

    // ---- Empleado ----

    @Transactional(readOnly = true)
    public List<Empleado> listarEmpleados() {
        return empleadoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Empleado obtenerEmpleado(Integer id) {
        return empleadoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Empleado no encontrado: " + id));
    }

    @Transactional
    public Empleado crearEmpleado(String nombreCompleto, String documento, String email,
                                   String telefono, String contrasenaPlana) {
        validarEmailEmpleado(email, null);
        validarDocumentoEmpleado(documento, null);
        if (telefono == null || telefono.isBlank()) {
            throw new IllegalArgumentException("Teléfono obligatorio.");
        }
        Empleado e = new Empleado();
        e.setNombreCompleto(nombreCompleto != null ? nombreCompleto.trim() : null);
        e.setDocumento(documento.trim());
        e.setEmail(email.trim().toLowerCase());
        e.setTelefono(telefono.trim());
        e.setContrasenaHash(passwordEncoder.encode(contrasenaPlana));
        return empleadoRepository.save(e);
    }

    @Transactional
    public Empleado actualizarEmpleado(Integer id, String nombreCompleto, String documento,
                                        String email, String telefono, String nuevaContrasena) {
        Empleado e = obtenerEmpleado(id);
        validarEmailEmpleado(email, id);
        validarDocumentoEmpleado(documento, id);
        if (telefono == null || telefono.isBlank()) {
            throw new IllegalArgumentException("Teléfono obligatorio.");
        }
        e.setNombreCompleto(nombreCompleto != null ? nombreCompleto.trim() : null);
        e.setDocumento(documento.trim());
        e.setEmail(email.trim().toLowerCase());
        e.setTelefono(telefono.trim());
        if (nuevaContrasena != null && !nuevaContrasena.isBlank()) {
            e.setContrasenaHash(passwordEncoder.encode(nuevaContrasena));
        }
        return empleadoRepository.save(e);
    }

    @Transactional
    public void eliminarEmpleado(Integer id) {
        if (!empleadoRepository.existsById(id)) {
            throw new IllegalArgumentException("Empleado no encontrado: " + id);
        }
        empleadoRepository.deleteById(id);
    }

    // ---- Validations ----

    private static final String EMAIL_REGEX = "^[\\w.+\\-]+@[\\w\\-]+\\.[a-zA-Z]{2,}$";

    private void validarEmailAdmin(String email, Integer excludeId) {
        if (email == null || email.isBlank()) {
            return; // email optional for admin (backward compat)
        }
        if (!email.matches(EMAIL_REGEX)) {
            throw new IllegalArgumentException("Formato de email inválido: " + email);
        }
        boolean exists = excludeId == null
                ? adminRepository.existsByEmail(email.trim().toLowerCase())
                : adminRepository.existsByEmailAndIdUsuarioNot(email.trim().toLowerCase(), excludeId);
        if (exists) {
            throw new IllegalArgumentException("Ya existe un administrador con el email: " + email);
        }
    }

    private void validarDocumentoAdmin(String documento, Integer excludeId) {
        if (documento == null || documento.isBlank()) {
            return; // document optional for admin (backward compat)
        }
        boolean exists = excludeId == null
                ? adminRepository.existsByDocumento(documento.trim())
                : adminRepository.existsByDocumentoAndIdUsuarioNot(documento.trim(), excludeId);
        if (exists) {
            throw new IllegalArgumentException("Ya existe un administrador con el documento: " + documento);
        }
    }

    private void validarEmailEmpleado(String email, Integer excludeId) {
        if (email == null || !email.matches(EMAIL_REGEX)) {
            throw new IllegalArgumentException("Email inválido o no proporcionado.");
        }
        boolean exists = excludeId == null
                ? empleadoRepository.existsByEmail(email.trim().toLowerCase())
                : empleadoRepository.existsByEmailAndIdEmpleadoNot(email.trim().toLowerCase(), excludeId);
        if (exists) {
            throw new IllegalArgumentException("Ya existe un empleado con el email: " + email);
        }
    }

    private void validarDocumentoEmpleado(String documento, Integer excludeId) {
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
