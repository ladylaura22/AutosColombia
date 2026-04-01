package com.autoscolombia.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pago")
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pago")
    private Integer idPago;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_pago", nullable = false)
    private TipoPago tipoPago;

    @Enumerated(EnumType.STRING)
    @Column(name = "metodo_pago", nullable = false)
    private MetodoPago metodoPago;

    @Column(name = "referencia", length = 100)
    private String referencia;

    @Column(name = "valor", nullable = false, precision = 12, scale = 2)
    private BigDecimal valor;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoPago estado = EstadoPago.PAGADO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_registro")
    private RegistroParqueo registroParqueo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_mensualidad")
    private Mensualidad mensualidad;

    @Column(name = "creado_por", length = 80)
    private String creadoPor;

    @Column(name = "creado_en")
    private LocalDateTime creadoEn;

    @Column(name = "anulado_por", length = 80)
    private String anuladoPor;

    @Column(name = "anulado_en")
    private LocalDateTime anuladoEn;

    @Column(name = "motivo_anulacion", length = 500)
    private String motivoAnulacion;

    public Integer getIdPago() { return idPago; }
    public void setIdPago(Integer idPago) { this.idPago = idPago; }

    public TipoPago getTipoPago() { return tipoPago; }
    public void setTipoPago(TipoPago tipoPago) { this.tipoPago = tipoPago; }

    public MetodoPago getMetodoPago() { return metodoPago; }
    public void setMetodoPago(MetodoPago metodoPago) { this.metodoPago = metodoPago; }

    public String getReferencia() { return referencia; }
    public void setReferencia(String referencia) { this.referencia = referencia; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    public EstadoPago getEstado() { return estado; }
    public void setEstado(EstadoPago estado) { this.estado = estado; }

    public RegistroParqueo getRegistroParqueo() { return registroParqueo; }
    public void setRegistroParqueo(RegistroParqueo registroParqueo) { this.registroParqueo = registroParqueo; }

    public Mensualidad getMensualidad() { return mensualidad; }
    public void setMensualidad(Mensualidad mensualidad) { this.mensualidad = mensualidad; }

    public String getCreadoPor() { return creadoPor; }
    public void setCreadoPor(String creadoPor) { this.creadoPor = creadoPor; }

    public LocalDateTime getCreadoEn() { return creadoEn; }
    public void setCreadoEn(LocalDateTime creadoEn) { this.creadoEn = creadoEn; }

    public String getAnuladoPor() { return anuladoPor; }
    public void setAnuladoPor(String anuladoPor) { this.anuladoPor = anuladoPor; }

    public LocalDateTime getAnuladoEn() { return anuladoEn; }
    public void setAnuladoEn(LocalDateTime anuladoEn) { this.anuladoEn = anuladoEn; }

    public String getMotivoAnulacion() { return motivoAnulacion; }
    public void setMotivoAnulacion(String motivoAnulacion) { this.motivoAnulacion = motivoAnulacion; }
}
