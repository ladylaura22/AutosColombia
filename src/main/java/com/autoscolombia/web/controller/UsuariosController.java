package com.autoscolombia.web.controller;

import com.autoscolombia.domain.Administrador;
import com.autoscolombia.domain.Empleado;
import com.autoscolombia.service.GestionUsuariosService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/usuarios")
@PreAuthorize("hasRole('ADMIN')")
public class UsuariosController {

    private final GestionUsuariosService service;

    public UsuariosController(GestionUsuariosService service) {
        this.service = service;
    }

    /** Lista todos los administradores y empleados */
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("admins", service.listarAdmins());
        model.addAttribute("empleados", service.listarEmpleados());
        return "usuarios/lista";
    }

    /** Formulario para crear un nuevo usuario (tipo: administrador | empleado) */
    @GetMapping("/nuevo")
    public String formNuevo(@RequestParam(defaultValue = "empleado") String tipo, Model model) {
        model.addAttribute("tipo", tipo);
        model.addAttribute("esNuevo", true);
        return "usuarios/form";
    }

    /** Crear usuario (tipo viene como parámetro en el form) */
    @PostMapping
    public String crear(@RequestParam String tipo,
                        @RequestParam(required = false) String nombreUsuario,
                        @RequestParam(required = false) String nombreCompleto,
                        @RequestParam String documento,
                        @RequestParam String email,
                        @RequestParam String telefono,
                        @RequestParam String contrasena,
                        Model model) {
        try {
            if ("administrador".equalsIgnoreCase(tipo)) {
                service.crearAdmin(nombreUsuario, nombreCompleto, documento, email, telefono, contrasena);
            } else {
                service.crearEmpleado(nombreCompleto, documento, email, telefono, contrasena);
            }
            return "redirect:/usuarios?exito";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("tipo", tipo);
            model.addAttribute("esNuevo", true);
            model.addAttribute("nombreUsuario", nombreUsuario);
            model.addAttribute("nombreCompleto", nombreCompleto);
            model.addAttribute("documento", documento);
            model.addAttribute("emailVal", email);
            model.addAttribute("telefono", telefono);
            return "usuarios/form";
        }
    }

    /** Formulario de edición */
    @GetMapping("/{tipo}/{id}/editar")
    public String formEditar(@PathVariable String tipo, @PathVariable Integer id, Model model) {
        model.addAttribute("tipo", tipo);
        model.addAttribute("esNuevo", false);
        if ("administrador".equalsIgnoreCase(tipo)) {
            model.addAttribute("usuario", service.obtenerAdmin(id));
        } else {
            model.addAttribute("usuario", service.obtenerEmpleado(id));
        }
        return "usuarios/form";
    }

    /** Actualizar usuario */
    @PostMapping("/{tipo}/{id}")
    public String actualizar(@PathVariable String tipo,
                             @PathVariable Integer id,
                             @RequestParam(required = false) String nombreUsuario,
                             @RequestParam(required = false) String nombreCompleto,
                             @RequestParam String documento,
                             @RequestParam String email,
                             @RequestParam String telefono,
                             @RequestParam(required = false) String contrasena,
                             Model model) {
        try {
            if ("administrador".equalsIgnoreCase(tipo)) {
                service.actualizarAdmin(id, nombreUsuario, nombreCompleto, documento, email, telefono, contrasena);
            } else {
                service.actualizarEmpleado(id, nombreCompleto, documento, email, telefono, contrasena);
            }
            return "redirect:/usuarios?exito";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("tipo", tipo);
            model.addAttribute("esNuevo", false);
            if ("administrador".equalsIgnoreCase(tipo)) {
                model.addAttribute("usuario", service.obtenerAdmin(id));
            } else {
                model.addAttribute("usuario", service.obtenerEmpleado(id));
            }
            return "usuarios/form";
        }
    }

    /** Eliminar usuario con confirmación via POST */
    @PostMapping("/{tipo}/{id}/eliminar")
    public String eliminar(@PathVariable String tipo, @PathVariable Integer id) {
        if ("administrador".equalsIgnoreCase(tipo)) {
            service.eliminarAdmin(id);
        } else {
            service.eliminarEmpleado(id);
        }
        return "redirect:/usuarios?eliminado";
    }
}
