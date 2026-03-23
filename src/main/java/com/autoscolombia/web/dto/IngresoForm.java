package com.autoscolombia.web.dto;

import com.autoscolombia.domain.TipoVehiculo;

public class IngresoForm {
    private String placa;
    private String vin;
    private TipoVehiculo tipoVehiculo;

    private String cedula;
    private String nombre;
    private String telefono;

    private Integer idCelda;

    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }

    public String getVin() { return vin; }
    public void setVin(String vin) { this.vin = vin; }

    public TipoVehiculo getTipoVehiculo() { return tipoVehiculo; }
    public void setTipoVehiculo(TipoVehiculo tipoVehiculo) { this.tipoVehiculo = tipoVehiculo; }

    public String getCedula() { return cedula; }
    public void setCedula(String cedula) { this.cedula = cedula; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public Integer getIdCelda() { return idCelda; }
    public void setIdCelda(Integer idCelda) { this.idCelda = idCelda; }
}
