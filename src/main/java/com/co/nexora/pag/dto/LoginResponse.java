package com.co.nexora.pag.dto;

public class LoginResponse {
    private String token;
    private String rol;
    private String nombre;
    private Long id;

    public LoginResponse(String token, String rol, String nombre, Long id) {
        this.token = token;
        this.rol = rol;
        this.nombre = nombre;
        this.id = id;
    }

    public String getToken() { return token; }
    public String getRol() { return rol; }
    public String getNombre() { return nombre; }
    public Long getId() { return id; }
}
