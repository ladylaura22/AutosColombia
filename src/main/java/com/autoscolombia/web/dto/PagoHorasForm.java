package com.autoscolombia.web.dto;

import com.autoscolombia.domain.MetodoPago;

public class PagoHorasForm {
    private Integer idRegistro;
    private MetodoPago metodoPago;
    private String referencia;

    public Integer getIdRegistro() { return idRegistro; }
    public void setIdRegistro(Integer idRegistro) { this.idRegistro = idRegistro; }

    public MetodoPago getMetodoPago() { return metodoPago; }
    public void setMetodoPago(MetodoPago metodoPago) { this.metodoPago = metodoPago; }

    public String getReferencia() { return referencia; }
    public void setReferencia(String referencia) { this.referencia = referencia; }
}
