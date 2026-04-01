package com.autoscolombia.web.dto;

import com.autoscolombia.domain.Pago;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class ReporteCajaDto {
    private LocalDate fecha;
    private BigDecimal totalBruto;
    private BigDecimal totalAnulado;
    private int cantidad;
    private Map<String, BigDecimal> desglosePorMetodo;
    private Map<String, BigDecimal> desglosePorTipo;
    private List<Pago> pagos;

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public BigDecimal getTotalBruto() { return totalBruto; }
    public void setTotalBruto(BigDecimal totalBruto) { this.totalBruto = totalBruto; }

    public BigDecimal getTotalAnulado() { return totalAnulado; }
    public void setTotalAnulado(BigDecimal totalAnulado) { this.totalAnulado = totalAnulado; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public Map<String, BigDecimal> getDesglosePorMetodo() { return desglosePorMetodo; }
    public void setDesglosePorMetodo(Map<String, BigDecimal> desglosePorMetodo) { this.desglosePorMetodo = desglosePorMetodo; }

    public Map<String, BigDecimal> getDesglosePorTipo() { return desglosePorTipo; }
    public void setDesglosePorTipo(Map<String, BigDecimal> desglosePorTipo) { this.desglosePorTipo = desglosePorTipo; }

    public List<Pago> getPagos() { return pagos; }
    public void setPagos(List<Pago> pagos) { this.pagos = pagos; }
}
