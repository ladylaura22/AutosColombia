package com.autoscolombia.web.controller;

import com.autoscolombia.service.ReporteCajaService;
import com.autoscolombia.web.dto.ReporteCajaDto;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/reportes")
@PreAuthorize("hasRole('ADMIN')")
public class ReportesController {

    private final ReporteCajaService reporteCajaService;

    public ReportesController(ReporteCajaService reporteCajaService) {
        this.reporteCajaService = reporteCajaService;
    }

    @GetMapping("/caja")
    public String cajaForm(Model model) {
        model.addAttribute("fecha", LocalDate.now().toString());
        return "reportes/caja";
    }

    @PostMapping("/caja")
    public String cajaReporte(@RequestParam String fecha, Model model) {
        try {
            LocalDate fechaDate = LocalDate.parse(fecha);
            ReporteCajaDto reporte = reporteCajaService.generarReporte(fechaDate);
            model.addAttribute("reporte", reporte);
            model.addAttribute("fecha", fecha);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("fecha", fecha);
        }
        return "reportes/caja";
    }
}
