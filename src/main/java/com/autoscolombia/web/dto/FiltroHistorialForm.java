package com.autoscolombia.web.dto;

public class FiltroHistorialForm {
    private String desde;
    private String hasta;
    private String placa;
    private String tipoPago;
    private String metodoPago;
    private String estado;

    public String getDesde() { return desde; }
    public void setDesde(String desde) { this.desde = desde; }

    public String getHasta() { return hasta; }
    public void setHasta(String hasta) { this.hasta = hasta; }

    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }

    public String getTipoPago() { return tipoPago; }
    public void setTipoPago(String tipoPago) { this.tipoPago = tipoPago; }

    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
