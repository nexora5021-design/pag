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

    private Double interes;
    private Integer meses;

    @Column(name = "fecha_desembolso")
    private LocalDate fechaDesembolso;

    @ManyToOne
    @JoinColumn(name = "prestamista_id")
    private Empleado prestamista;

    @ManyToOne
    @JoinColumn(name = "socio_id")
    private Socio socio;

    private String titulo;
    private String estado;

    @Column(name = "fecha_corte")
    private LocalDate fechaCorte;

    @Column(name = "mes_actual")
    private Integer mesActual;

    @Column(name = "ganancia_estimada")
    private Double gananciaEstimada;

    @Column(name = "capital_pendiente")
    private Double capitalPendiente;

    @Column(name = "interes_pendiente")
    private Double interesPendiente;

    private Double ganancias;

    @Column(name = "nombre_cliente")
    private String nombreCliente;

    @Column(name = "nombre_prestamista")
    private String nombrePrestamista;

    @Column(name = "nombre_socio")
    private String nombreSocio;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public Double getMontoPrestado() { return montoPrestado; }
    public void setMontoPrestado(Double montoPrestado) { this.montoPrestado = montoPrestado; }
    public Double getInteres() { return interes; }
    public void setInteres(Double interes) { this.interes = interes; }
    public Integer getMeses() { return meses; }
    public void setMeses(Integer meses) { this.meses = meses; }
    public LocalDate getFechaDesembolso() { return fechaDesembolso; }
    public void setFechaDesembolso(LocalDate fechaDesembolso) { this.fechaDesembolso = fechaDesembolso; }
    public Empleado getPrestamista() { return prestamista; }
    public void setPrestamista(Empleado prestamista) { this.prestamista = prestamista; }
    public Socio getSocio() { return socio; }
    public void setSocio(Socio socio) { this.socio = socio; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public LocalDate getFechaCorte() { return fechaCorte; }
    public void setFechaCorte(LocalDate fechaCorte) { this.fechaCorte = fechaCorte; }
    public Integer getMesActual() { return mesActual; }
    public void setMesActual(Integer mesActual) { this.mesActual = mesActual; }
    public Double getGananciaEstimada() { return gananciaEstimada; }
    public void setGananciaEstimada(Double gananciaEstimada) { this.gananciaEstimada = gananciaEstimada; }
    public Double getCapitalPendiente() { return capitalPendiente; }
    public void setCapitalPendiente(Double capitalPendiente) { this.capitalPendiente = capitalPendiente; }
    public Double getInteresPendiente() { return interesPendiente; }
    public void setInteresPendiente(Double interesPendiente) { this.interesPendiente = interesPendiente; }
    public Double getGanancias() { return ganancias; }
    public void setGanancias(Double ganancias) { this.ganancias = ganancias; }
    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }
    public String getNombrePrestamista() { return nombrePrestamista; }
    public void setNombrePrestamista(String nombrePrestamista) { this.nombrePrestamista = nombrePrestamista; }
    public String getNombreSocio() { return nombreSocio; }
    public void setNombreSocio(String nombreSocio) { this.nombreSocio = nombreSocio; }
}
