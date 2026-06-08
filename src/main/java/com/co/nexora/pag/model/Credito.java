package com.co.nexora.pag.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "creditos")
public class Credito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @Column(name = "monto_prestado")
    private Double montoPrestado;

    @Column(name = "tipo_credito")
    private String tipoCredito;

    private Double interes;
    private Integer cuotas;

    @Column(name = "valor_cuota")
    private Double valorCuota;

    @Column(name = "fecha_desembolso")
    private LocalDate fechaDesembolso;

    @ManyToOne
    @JoinColumn(name = "prestamista_id")
    private Empleado prestamista;

    @Column(name = "primera_cuota")
    private LocalDate primeraCuota;

    private String titulo;
    private String estado;

    @Column(name = "proxima_cuota")
    private LocalDate proximaCuota;

    @Column(name = "cuota_actual")
    private Integer cuotaActual;

    @Column(name = "ganancia_estimada")
    private Double gananciaEstimada;

    @Column(name = "capital_pendiente")
    private Double capitalPendiente;

    @Column(name = "interes_recaudado")
    private Double interesRecaudado;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public Double getMontoPrestado() { return montoPrestado; }
    public void setMontoPrestado(Double montoPrestado) { this.montoPrestado = montoPrestado; }
    public String getTipoCredito() { return tipoCredito; }
    public void setTipoCredito(String tipoCredito) { this.tipoCredito = tipoCredito; }
    public Double getInteres() { return interes; }
    public void setInteres(Double interes) { this.interes = interes; }
    public Integer getCuotas() { return cuotas; }
    public void setCuotas(Integer cuotas) { this.cuotas = cuotas; }
    public Double getValorCuota() { return valorCuota; }
    public void setValorCuota(Double valorCuota) { this.valorCuota = valorCuota; }
    public LocalDate getFechaDesembolso() { return fechaDesembolso; }
    public void setFechaDesembolso(LocalDate fechaDesembolso) { this.fechaDesembolso = fechaDesembolso; }
    public Empleado getPrestamista() { return prestamista; }
    public void setPrestamista(Empleado prestamista) { this.prestamista = prestamista; }
    public LocalDate getPrimeraCuota() { return primeraCuota; }
    public void setPrimeraCuota(LocalDate primeraCuota) { this.primeraCuota = primeraCuota; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public LocalDate getProximaCuota() { return proximaCuota; }
    public void setProximaCuota(LocalDate proximaCuota) { this.proximaCuota = proximaCuota; }
    public Integer getCuotaActual() { return cuotaActual; }
    public void setCuotaActual(Integer cuotaActual) { this.cuotaActual = cuotaActual; }
    public Double getGananciaEstimada() { return gananciaEstimada; }
    public void setGananciaEstimada(Double gananciaEstimada) { this.gananciaEstimada = gananciaEstimada; }
    public Double getCapitalPendiente() { return capitalPendiente; }
    public void setCapitalPendiente(Double capitalPendiente) { this.capitalPendiente = capitalPendiente; }
    public Double getInteresRecaudado() { return interesRecaudado; }
    public void setInteresRecaudado(Double interesRecaudado) { this.interesRecaudado = interesRecaudado; }
}
