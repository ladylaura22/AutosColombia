package com.autoscolombia.web.controller;

import com.autoscolombia.service.TarifaService;
import com.autoscolombia.web.dto.TarifaForm;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/tarifas")
@PreAuthorize("hasRole('ADMIN')")
public class TarifasController {

    private final TarifaService tarifaService;

    public TarifasController(TarifaService tarifaService) {
        this.tarifaService = tarifaService;
    }

    @GetMapping
    public String configurar(Model model) {
        try {
            model.addAttribute("tarifa", tarifaService.obtenerTarifaActiva());
        } catch (Exception e) {
            model.addAttribute("tarifa", null);
        }
        model.addAttribute("form", new TarifaForm());
        return "tarifas/configurar";
    }

    @PostMapping
    public String actualizar(@ModelAttribute TarifaForm form, RedirectAttributes ra) {
        try {
            tarifaService.actualizarTarifa(form.getTarifaHoraCarro(), form.getTarifaHoraMoto(), form.getTarifaMensual());
            ra.addFlashAttribute("success", "Tarifas actualizadas exitosamente.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/tarifas";
    }
}
