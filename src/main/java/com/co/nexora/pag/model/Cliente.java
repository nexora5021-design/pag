package com.co.nexora.pag.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "clientes")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String celular;
    private String direccion;
    private String ciudad;
    private String barrio;
    private Boolean moroso;
    private Integer creditos;

    @Column(name = "creditos_activos")
    private Integer creditosActivos;

    @Column(name = "nombre_fiador")
    private String nombreFiador;

    @Column(name = "celular_fiador")
    private String celularFiador;

    @Column(name = "direccion_fiador")
    private String direccionFiador;

    @ManyToOne
    @JoinColumn(name = "prestamista_id")
    private Empleado prestamista;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getCelular() { return celular; }
    public void setCelular(String celular) { this.celular = celular; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }
    public String getBarrio() { return barrio; }
    public void setBarrio(String barrio) { this.barrio = barrio; }
    public Boolean getMoroso() { return moroso; }
    public void setMoroso(Boolean moroso) { this.moroso = moroso; }
    public Integer getCreditos() { return creditos; }
    public void setCreditos(Integer creditos) { this.creditos = creditos; }
    public Integer getCreditosActivos() { return creditosActivos; }
    public void setCreditosActivos(Integer creditosActivos) { this.creditosActivos = creditosActivos; }
    public String getNombreFiador() { return nombreFiador; }
    public void setNombreFiador(String nombreFiador) { this.nombreFiador = nombreFiador; }
    public String getCelularFiador() { return celularFiador; }
    public void setCelularFiador(String celularFiador) { this.celularFiador = celularFiador; }
    public String getDireccionFiador() { return direccionFiador; }
    public void setDireccionFiador(String direccionFiador) { this.direccionFiador = direccionFiador; }
    public Empleado getPrestamista() { return prestamista; }
    public void setPrestamista(Empleado prestamista) { this.prestamista = prestamista; }
}
