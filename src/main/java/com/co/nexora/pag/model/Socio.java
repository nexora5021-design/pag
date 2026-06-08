package com.co.nexora.pag.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "socios")
public class Socio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String usuario;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private Boolean estado;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public Boolean getEstado() { return estado; }
    public void setEstado(Boolean estado) { this.estado = estado; }
}
