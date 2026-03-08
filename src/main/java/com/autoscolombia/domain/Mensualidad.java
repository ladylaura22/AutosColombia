package com.autoscolombia.domain;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "mensualidad")
public class Mensualidad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mensualidad")
    private Long idMensualidad;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "placa", nullable = false)
    private Vehiculo vehiculo;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin", nullable = false)
    private LocalDate fechaFin;

    @Column(name = "esta_activa", nullable = false)
    private Boolean estaActiva = true;

    public Long getIdMensualidad() { return idMensualidad; }
    public void setIdMensualidad(Long idMensualidad) { this.idMensualidad = idMensualidad; }

    public Vehiculo getVehiculo() { return vehiculo; }
    public void setVehiculo(Vehiculo vehiculo) { this.vehiculo = vehiculo; }

    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }

    public Boolean getEstaActiva() { return estaActiva; }
    public void setEstaActiva(Boolean estaActiva) { this.estaActiva = estaActiva; }
}