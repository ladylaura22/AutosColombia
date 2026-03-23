package com.autoscolombia.web.controller;

import com.autoscolombia.service.EmpleadoService;
import com.autoscolombia.domain.Empleado;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/empleados")
@PreAuthorize("hasRole('ADMIN')")
public class EmpleadoController {

    private final EmpleadoService empleadoService;

    public EmpleadoController(EmpleadoService empleadoService) {
        this.empleadoService = empleadoService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("empleados", empleadoService.listarTodos());
        return "empleados/lista";
    }

    @GetMapping("/nuevo")
    public String formNuevo() {
        return "empleados/crear";
    }

    @PostMapping("/nuevo")
    public String crear(@RequestParam String nombreCompleto,
                        @RequestParam String documento,
                        @RequestParam String email,
                        @RequestParam(required = false) String telefono,
                        @RequestParam String contrasena,
                        Model model) {
        try {
            empleadoService.crear(nombreCompleto, documento, email, telefono, contrasena);
            return "redirect:/empleados?exito";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "empleados/crear";
        }
    }

    @GetMapping("/{id}/editar")
    public String formEditar(@PathVariable Integer id, Model model) {
        model.addAttribute("empleado", empleadoService.obtenerPorId(id));
        return "empleados/editar";
    }

    @PostMapping("/{id}/editar")
    public String actualizar(@PathVariable Integer id,
                             @RequestParam String nombreCompleto,
                             @RequestParam String documento,
                             @RequestParam String email,
                             @RequestParam(required = false) String telefono,
                             @RequestParam(required = false) String contrasena,
                             Model model) {
        try {
            empleadoService.actualizar(id, nombreCompleto, documento, email, telefono, contrasena);
            return "redirect:/empleados?exito";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("empleado", empleadoService.obtenerPorId(id));
            return "empleados/editar";
        }
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Integer id) {
        empleadoService.eliminar(id);
        return "redirect:/empleados?eliminado";
    }
}
