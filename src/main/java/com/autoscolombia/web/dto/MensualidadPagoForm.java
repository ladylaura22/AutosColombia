package com.autoscolombia.web.dto;

import com.autoscolombia.domain.MetodoPago;

public class MensualidadPagoForm {
    private String placa;
    private MetodoPago metodoPago;
    private String referencia;

    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }

    public MetodoPago getMetodoPago() { return metodoPago; }
    public void setMetodoPago(MetodoPago metodoPago) { this.metodoPago = metodoPago; }

    public String getReferencia() { return referencia; }
    public void setReferencia(String referencia) { this.referencia = referencia; }
}
