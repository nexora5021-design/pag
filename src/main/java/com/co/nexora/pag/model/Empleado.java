package com.co.nexora.pag.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "empleados")
public class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String usuario;
    private String celular;

    @Column(name = "fecha_ingreso")
    private LocalDate fechaIngreso;

    private Integer creditos;
    private Integer activos;
    private Integer mora;
    private Boolean estado;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }
    public String getCelular() { return celular; }
    public void setCelular(String celular) { this.celular = celular; }
    public LocalDate getFechaIngreso() { return fechaIngreso; }
    public void setFechaIngreso(LocalDate fechaIngreso) { this.fechaIngreso = fechaIngreso; }
    public Integer getCreditos() { return creditos; }
    public void setCreditos(Integer creditos) { this.creditos = creditos; }
    public Integer getActivos() { return activos; }
    public void setActivos(Integer activos) { this.activos = activos; }
    public Integer getMora() { return mora; }
    public void setMora(Integer mora) { this.mora = mora; }
    public Boolean getEstado() { return estado; }
    public void setEstado(Boolean estado) { this.estado = estado; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
