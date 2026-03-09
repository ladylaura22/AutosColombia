package com.autoscolombia.domain;

import jakarta.persistence.*;

@Entity
@Table(
        name = "vehiculo",
        uniqueConstraints = @UniqueConstraint(name = "uq_vehiculo_vin", columnNames = "vin")
)
public class Vehiculo {

    @Id
    @Column(name = "placa", length = 10)
    private String placa;

    // IMPORTANTE: forzar CHAR(17) para que coincida con MySQL
    @Column(name = "vin", nullable = false, columnDefinition = "CHAR(17)")
    private String vin;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_vehiculo", nullable = false)
    private TipoVehiculo tipoVehiculo;

    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }

    public String getVin() { return vin; }
    public void setVin(String vin) { this.vin = vin; }

    public TipoVehiculo getTipoVehiculo() { return tipoVehiculo; }
    public void setTipoVehiculo(TipoVehiculo tipoVehiculo) { this.tipoVehiculo = tipoVehiculo; }
}