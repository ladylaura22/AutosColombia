package com.autoscolombia.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tarifa")
public class Tarifa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tarifa")
    private Integer idTarifa;

    @Column(name = "tarifa_hora_carro", nullable = false, precision = 12, scale = 2)
    private BigDecimal tarifaHoraCarro;

    @Column(name = "tarifa_hora_moto", nullable = false, precision = 12, scale = 2)
    private BigDecimal tarifaHoraMoto;

    @Column(name = "tarifa_mensual", nullable = false, precision = 12, scale = 2)
    private BigDecimal tarifaMensual;

    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    @Column(name = "creado_en")
    private LocalDateTime creadoEn;

    @Column(name = "modificado_en")
    private LocalDateTime modificadoEn;

    public Integer getIdTarifa() { return idTarifa; }
    public void setIdTarifa(Integer idTarifa) { this.idTarifa = idTarifa; }

    public BigDecimal getTarifaHoraCarro() { return tarifaHoraCarro; }
    public void setTarifaHoraCarro(BigDecimal tarifaHoraCarro) { this.tarifaHoraCarro = tarifaHoraCarro; }

    public BigDecimal getTarifaHoraMoto() { return tarifaHoraMoto; }
    public void setTarifaHoraMoto(BigDecimal tarifaHoraMoto) { this.tarifaHoraMoto = tarifaHoraMoto; }

    public BigDecimal getTarifaMensual() { return tarifaMensual; }
    public void setTarifaMensual(BigDecimal tarifaMensual) { this.tarifaMensual = tarifaMensual; }

    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }

    public LocalDateTime getCreadoEn() { return creadoEn; }
    public void setCreadoEn(LocalDateTime creadoEn) { this.creadoEn = creadoEn; }

    public LocalDateTime getModificadoEn() { return modificadoEn; }
    public void setModificadoEn(LocalDateTime modificadoEn) { this.modificadoEn = modificadoEn; }
}
