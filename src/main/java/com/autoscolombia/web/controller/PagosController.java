package com.autoscolombia.web.controller;

import com.autoscolombia.domain.*;
import com.autoscolombia.repository.RegistroParqueoRepository;
import com.autoscolombia.service.ComprobantePdfService;
import com.autoscolombia.service.PagoService;
import com.autoscolombia.web.dto.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/pagos")
public class PagosController {

    private final PagoService pagoService;
    private final ComprobantePdfService comprobantePdfService;
    private final RegistroParqueoRepository registroParqueoRepository;

    public PagosController(PagoService pagoService,
                            ComprobantePdfService comprobantePdfService,
                            RegistroParqueoRepository registroParqueoRepository) {
        this.pagoService = pagoService;
        this.comprobantePdfService = comprobantePdfService;
        this.registroParqueoRepository = registroParqueoRepository;
    }

    @GetMapping("/historial")
    public String historial(Model model) {
        model.addAttribute("filtro", new FiltroHistorialForm());
        model.addAttribute("pagos", pagoService.listarTodos());
        model.addAttribute("tiposPago", TipoPago.values());
        model.addAttribute("metodosPago", MetodoPago.values());
        model.addAttribute("estadosPago", EstadoPago.values());
        return "pagos/historial";
    }

    @PostMapping("/historial")
    public String historialFiltro(@ModelAttribute FiltroHistorialForm filtro, Model model) {
        LocalDate desde = parseFecha(filtro.getDesde());
        LocalDate hasta = parseFecha(filtro.getHasta());
        TipoPago tipoPago = parseEnum(TipoPago.class, filtro.getTipoPago());
        MetodoPago metodoPago = parseEnum(MetodoPago.class, filtro.getMetodoPago());
        EstadoPago estado = parseEnum(EstadoPago.class, filtro.getEstado());

        List<Pago> pagos = pagoService.buscarConFiltros(desde, hasta, filtro.getPlaca(), tipoPago, metodoPago, estado);
        model.addAttribute("filtro", filtro);
        model.addAttribute("pagos", pagos);
        model.addAttribute("tiposPago", TipoPago.values());
        model.addAttribute("metodosPago", MetodoPago.values());
        model.addAttribute("estadosPago", EstadoPago.values());
        return "pagos/historial";
    }

    @GetMapping("/registrar")
    public String registrarForm(@RequestParam(required = false) Integer idRegistro, Model model) {
        PagoHorasForm form = new PagoHorasForm();
        if (idRegistro != null) {
            form.setIdRegistro(idRegistro);
            registroParqueoRepository.findById(idRegistro).ifPresent(r -> model.addAttribute("registro", r));
        }
        model.addAttribute("form", form);
        model.addAttribute("metodosPago", MetodoPago.values());
        return "pagos/registrar";
    }

    @PostMapping("/registrar")
    public String registrar(@ModelAttribute PagoHorasForm form, RedirectAttributes ra) {
        try {
            String usuario = SecurityContextHolder.getContext().getAuthentication().getName();
            Pago pago = pagoService.registrarPagoHoras(form.getIdRegistro(), form.getMetodoPago(), form.getReferencia(), usuario);
            ra.addFlashAttribute("success", "Pago registrado exitosamente. ID: " + pago.getIdPago());
            return "redirect:/pagos/" + pago.getIdPago();
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/pagos/registrar" + (form.getIdRegistro() != null ? "?idRegistro=" + form.getIdRegistro() : "");
        }
    }

    @GetMapping("/mensualidad")
    public String mensualidadForm(Model model) {
        model.addAttribute("form", new MensualidadPagoForm());
        model.addAttribute("metodosPago", MetodoPago.values());
        return "pagos/mensualidad";
    }

    @PostMapping("/mensualidad")
    public String mensualidad(@ModelAttribute MensualidadPagoForm form, RedirectAttributes ra) {
        try {
            String usuario = SecurityContextHolder.getContext().getAuthentication().getName();
            Pago pago = pagoService.registrarPagoMensualidad(form.getPlaca(), form.getMetodoPago(), form.getReferencia(), usuario);
            ra.addFlashAttribute("success", "Pago de mensualidad registrado. ID: " + pago.getIdPago());
            return "redirect:/pagos/" + pago.getIdPago();
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/pagos/mensualidad";
        }
    }

    @GetMapping("/{id}")
    public String detalle(@PathVariable Integer id, Model model) {
        model.addAttribute("pago", pagoService.obtenerPorId(id));
        return "pagos/detalle";
    }

    @GetMapping("/{id}/comprobante")
    public String comprobante(@PathVariable Integer id, Model model) {
        model.addAttribute("pago", pagoService.obtenerPorId(id));
        return "pagos/comprobante";
    }

    @GetMapping("/{id}/comprobante.pdf")
    public ResponseEntity<byte[]> comprobantePdf(@PathVariable Integer id) {
        Pago pago = pagoService.obtenerPorId(id);
        byte[] pdf = comprobantePdfService.generarPdf(pago);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"comprobante-" + id + ".pdf\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @GetMapping("/{id}/anular")
    @PreAuthorize("hasRole('ADMIN')")
    public String anularForm(@PathVariable Integer id, Model model) {
        model.addAttribute("pago", pagoService.obtenerPorId(id));
        model.addAttribute("form", new AnularPagoForm());
        return "pagos/anular";
    }

    @PostMapping("/{id}/anular")
    @PreAuthorize("hasRole('ADMIN')")
    public String anular(@PathVariable Integer id, @ModelAttribute AnularPagoForm form, RedirectAttributes ra) {
        try {
            String usuario = SecurityContextHolder.getContext().getAuthentication().getName();
            pagoService.anularPago(id, form.getMotivo(), usuario);
            ra.addFlashAttribute("success", "Pago anulado exitosamente.");
            return "redirect:/pagos/" + id;
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/pagos/" + id + "/anular";
        }
    }

    private LocalDate parseFecha(String s) {
        if (s == null || s.isBlank()) return null;
        try { return LocalDate.parse(s); } catch (Exception e) { return null; }
    }

    private <T extends Enum<T>> T parseEnum(Class<T> clazz, String s) {
        if (s == null || s.isBlank()) return null;
        try { return Enum.valueOf(clazz, s); } catch (Exception e) { return null; }
    }
}
