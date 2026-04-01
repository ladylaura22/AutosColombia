package com.autoscolombia.service;

import com.autoscolombia.domain.EstadoPago;
import com.autoscolombia.domain.Pago;
import com.autoscolombia.repository.PagoRepository;
import com.autoscolombia.web.dto.ReporteCajaDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class ReporteCajaService {

    private final PagoRepository pagoRepository;

    public ReporteCajaService(PagoRepository pagoRepository) {
        this.pagoRepository = pagoRepository;
    }

    public ReporteCajaDto generarReporte(LocalDate fecha) {
        LocalDateTime inicio = fecha.atStartOfDay();
        LocalDateTime fin = fecha.plusDays(1).atStartOfDay();
        List<Pago> pagos = pagoRepository.findByFecha(inicio, fin);

        BigDecimal totalBruto = BigDecimal.ZERO;
        BigDecimal totalAnulado = BigDecimal.ZERO;
        Map<String, BigDecimal> desglosePorMetodo = new LinkedHashMap<>();
        Map<String, BigDecimal> desglosePorTipo = new LinkedHashMap<>();

        for (Pago p : pagos) {
            if (p.getEstado() == EstadoPago.PAGADO) {
                totalBruto = totalBruto.add(p.getValor());
                String metodo = p.getMetodoPago().name();
                desglosePorMetodo.merge(metodo, p.getValor(), BigDecimal::add);
                String tipo = p.getTipoPago().name();
                desglosePorTipo.merge(tipo, p.getValor(), BigDecimal::add);
            } else if (p.getEstado() == EstadoPago.ANULADO) {
                totalAnulado = totalAnulado.add(p.getValor());
            }
        }

        ReporteCajaDto dto = new ReporteCajaDto();
        dto.setFecha(fecha);
        dto.setTotalBruto(totalBruto);
        dto.setTotalAnulado(totalAnulado);
        dto.setCantidad(pagos.size());
        dto.setDesglosePorMetodo(desglosePorMetodo);
        dto.setDesglosePorTipo(desglosePorTipo);
        dto.setPagos(pagos);

        return dto;
    }
}
