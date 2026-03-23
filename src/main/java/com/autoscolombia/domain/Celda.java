package com.autoscolombia.domain;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "celda")
public class Celda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_celda")
    private Integer idCelda;

    @Column(name = "codigo", nullable = false, unique = true, length = 20)
    private String codigo;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoCelda tipo;

    @Column(name = "ubicacion", length = 100)
    private String ubicacion;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoCelda estado;

    @Column(name = "motivo_mantenimiento", length = 255)
    private String motivoMantenimiento;

    @Column(name = "fecha_estimada_disponible")
    private LocalDate fechaEstimadaDisponible;

    public Integer getIdCelda() { return idCelda; }
    public void setIdCelda(Integer idCelda) { this.idCelda = idCelda; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public TipoCelda getTipo() { return tipo; }
    public void setTipo(TipoCelda tipo) { this.tipo = tipo; }

    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }

    public EstadoCelda getEstado() { return estado; }
    public void setEstado(EstadoCelda estado) { this.estado = estado; }

    public String getMotivoMantenimiento() { return motivoMantenimiento; }
    public void setMotivoMantenimiento(String motivoMantenimiento) { this.motivoMantenimiento = motivoMantenimiento; }

    public LocalDate getFechaEstimadaDisponible() { return fechaEstimadaDisponible; }
    public void setFechaEstimadaDisponible(LocalDate fechaEstimadaDisponible) { this.fechaEstimadaDisponible = fechaEstimadaDisponible; }
}
