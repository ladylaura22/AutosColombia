package com.autoscolombia.web.controller;

import com.autoscolombia.domain.RegistroParqueo;
import com.autoscolombia.service.ParqueaderoService;
import com.autoscolombia.web.dto.BuscarForm;
import com.autoscolombia.web.dto.IngresoForm;
import com.autoscolombia.web.dto.SalidaForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ParqueaderoController {

    private final ParqueaderoService parqueaderoService;

    public ParqueaderoController(ParqueaderoService parqueaderoService) {
        this.parqueaderoService = parqueaderoService;
    }

    @GetMapping("/ingreso")
    public String verIngreso(Model model) {
        model.addAttribute("form", new IngresoForm());
        return "ingreso";
    }

    @PostMapping("/ingreso")
    public String procesarIngreso(@ModelAttribute("form") IngresoForm form, Model model) {
        try {
            RegistroParqueo rp = parqueaderoService.registrarIngreso(form);
            model.addAttribute("registro", rp);
            model.addAttribute("mensaje", "Ingreso registrado con éxito.");
            model.addAttribute("form", new IngresoForm());
            return "ingreso";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "ingreso";
        }
    }

    @GetMapping("/salida")
    public String verSalida(Model model) {
        model.addAttribute("form", new SalidaForm());
        return "salida";
    }

    @PostMapping("/salida")
    public String procesarSalida(@ModelAttribute("form") SalidaForm form, Model model) {
        try {
            long minutos = parqueaderoService.procesarSalida(form);
            model.addAttribute("mensaje", "Salida registrada. Permanencia: " + minutos + " minutos.");
            model.addAttribute("form", new SalidaForm());
            return "salida";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "salida";
        }
    }

    @GetMapping("/buscar")
    public String verBuscar(Model model) {
        model.addAttribute("form", new BuscarForm());
        return "buscar";
    }

    @PostMapping("/buscar")
    public String procesarBuscar(@ModelAttribute("form") BuscarForm form, Model model) {
        try {
            RegistroParqueo rp = parqueaderoService.buscarUltimoRegistro(form.getPlaca());
            model.addAttribute("registro", rp);
            return "buscar";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "buscar";
        }
    }

    @GetMapping("/parqueados")
    public String verParqueados(Model model) {
        model.addAttribute("parqueados", parqueaderoService.listarParqueados());
        return "parqueados";
    }
}