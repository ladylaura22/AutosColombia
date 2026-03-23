package com.autoscolombia.web.controller;

import com.autoscolombia.domain.Celda;
import com.autoscolombia.domain.EstadoCelda;
import com.autoscolombia.domain.TipoCelda;
import com.autoscolombia.service.CeldaService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Controller
public class CeldaController {

    private final CeldaService celdaService;

    public CeldaController(CeldaService celdaService) {
        this.celdaService = celdaService;
    }

    /** Vista Thymeleaf del dashboard de celdas */
    @GetMapping("/celdas")
    public String dashboard(Model model) {
        model.addAttribute("celdas", celdaService.listarTodas());
        model.addAttribute("estadoCelda", EstadoCelda.values());
        model.addAttribute("tipoCelda", TipoCelda.values());
        return "celdas";
    }

    /** API: lista todas las celdas */
    @GetMapping("/api/celdas")
    @ResponseBody
    public List<Celda> listarCeldas() {
        return celdaService.listarTodas();
    }

    /** API: lista celdas disponibles, opcionalmente filtradas por tipo */
    @GetMapping("/api/celdas/disponibles")
    @ResponseBody
    public List<Celda> listarDisponibles(@RequestParam(required = false) TipoCelda tipo) {
        return celdaService.listarDisponibles(tipo);
    }

    /** API: crear nueva celda (solo ADMIN) */
    @PostMapping("/api/celdas")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseBody
    public ResponseEntity<?> crearCelda(@RequestBody Map<String, String> body) {
        try {
            String codigo = body.get("codigo");
            TipoCelda tipo = TipoCelda.valueOf(body.getOrDefault("tipo", "AUTO"));
            String ubicacion = body.getOrDefault("ubicacion", "");
            Celda c = celdaService.crearCelda(codigo, tipo, ubicacion);
            return ResponseEntity.ok(c);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /** Formulario para crear celda (solo ADMIN) */
    @GetMapping("/celdas/nueva")
    @PreAuthorize("hasRole('ADMIN')")
    public String formNuevaCelda(Model model) {
        model.addAttribute("tipoCelda", TipoCelda.values());
        return "celdas/nueva";
    }

    @PostMapping("/celdas/nueva")
    @PreAuthorize("hasRole('ADMIN')")
    public String guardarNuevaCelda(@RequestParam String codigo,
                                    @RequestParam TipoCelda tipo,
                                    @RequestParam(required = false) String ubicacion,
                                    Model model) {
        try {
            celdaService.crearCelda(codigo, tipo, ubicacion);
            return "redirect:/celdas?exito";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("tipoCelda", TipoCelda.values());
            return "celdas/nueva";
        }
    }

    /** API: cambiar estado de una celda (solo ADMIN) */
    @PostMapping("/api/celdas/{id}/estado")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseBody
    public ResponseEntity<?> cambiarEstado(@PathVariable Integer id,
                                           @RequestBody Map<String, String> body) {
        try {
            EstadoCelda nuevoEstado = EstadoCelda.valueOf(body.get("estado"));
            String motivo = body.get("motivoMantenimiento");
            LocalDate fecha = body.get("fechaEstimadaDisponible") != null
                    ? LocalDate.parse(body.get("fechaEstimadaDisponible")) : null;
            Celda c = celdaService.cambiarEstado(id, nuevoEstado, motivo, fecha);
            return ResponseEntity.ok(c);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /** Formulario para cambiar estado de celda (solo ADMIN) */
    @PostMapping("/celdas/{id}/estado")
    @PreAuthorize("hasRole('ADMIN')")
    public String cambiarEstadoForm(@PathVariable Integer id,
                                    @RequestParam EstadoCelda estado,
                                    @RequestParam(required = false) String motivoMantenimiento,
                                    @RequestParam(required = false)
                                    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaEstimadaDisponible,
                                    Model model) {
        try {
            celdaService.cambiarEstado(id, estado, motivoMantenimiento, fechaEstimadaDisponible);
            return "redirect:/celdas?exito";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("celdas", celdaService.listarTodas());
            model.addAttribute("estadoCelda", EstadoCelda.values());
            model.addAttribute("tipoCelda", TipoCelda.values());
            return "celdas";
        }
    }
}
