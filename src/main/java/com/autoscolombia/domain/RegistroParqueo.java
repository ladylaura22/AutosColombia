package com.autoscolombia.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "registro_parqueo")
public class RegistroParqueo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_registro")
    private Integer idRegistro;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "placa", nullable = false)
    private Vehiculo vehiculo;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "id_usuario", nullable = true)
    private Administrador administrador;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "id_celda", nullable = true)
    private Celda celda;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    @Column(name = "hora_ingreso", nullable = false)
    private LocalDateTime horaIngreso;

    @Column(name = "hora_salida")
    private LocalDateTime horaSalida;

    @Enumerated(EnumType.STRING)
    @Column(name = "lugar_actual", nullable = false)
    private LugarActual lugarActual;

    @Enumerated(EnumType.STRING)
    @Column(name = "metodo_cobro", nullable = false)
    private MetodoCobro metodoCobro;

    public Integer getIdRegistro() { return idRegistro; }
    public void setIdRegistro(Integer idRegistro) { this.idRegistro = idRegistro; }

    public Vehiculo getVehiculo() { return vehiculo; }
    public void setVehiculo(Vehiculo vehiculo) { this.vehiculo = vehiculo; }

    public Administrador getAdministrador() { return administrador; }
    public void setAdministrador(Administrador administrador) { this.administrador = administrador; }

    public Celda getCelda() { return celda; }
    public void setCelda(Celda celda) { this.celda = celda; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public LocalDateTime getHoraIngreso() { return horaIngreso; }
    public void setHoraIngreso(LocalDateTime horaIngreso) { this.horaIngreso = horaIngreso; }

    public LocalDateTime getHoraSalida() { return horaSalida; }
    public void setHoraSalida(LocalDateTime horaSalida) { this.horaSalida = horaSalida; }

    public LugarActual getLugarActual() { return lugarActual; }
    public void setLugarActual(LugarActual lugarActual) { this.lugarActual = lugarActual; }

    public MetodoCobro getMetodoCobro() { return metodoCobro; }
    public void setMetodoCobro(MetodoCobro metodoCobro) { this.metodoCobro = metodoCobro; }
}