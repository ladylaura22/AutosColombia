package com.autoscolombia.web.dto;

import java.math.BigDecimal;

public class TarifaForm {
    private BigDecimal tarifaHoraCarro;
    private BigDecimal tarifaHoraMoto;
    private BigDecimal tarifaMensual;

    public BigDecimal getTarifaHoraCarro() { return tarifaHoraCarro; }
    public void setTarifaHoraCarro(BigDecimal tarifaHoraCarro) { this.tarifaHoraCarro = tarifaHoraCarro; }

    public BigDecimal getTarifaHoraMoto() { return tarifaHoraMoto; }
    public void setTarifaHoraMoto(BigDecimal tarifaHoraMoto) { this.tarifaHoraMoto = tarifaHoraMoto; }

    public BigDecimal getTarifaMensual() { return tarifaMensual; }
    public void setTarifaMensual(BigDecimal tarifaMensual) { this.tarifaMensual = tarifaMensual; }
}
