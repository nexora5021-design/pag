package com.co.nexora.pag.dto;

public class ChangePasswordRequest {
    private String usuario;
    private String passwordActual;
    private String passwordNuevo;

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }
    public String getPasswordActual() { return passwordActual; }
    public void setPasswordActual(String passwordActual) { this.passwordActual = passwordActual; }
    public String getPasswordNuevo() { return passwordNuevo; }
    public void setPasswordNuevo(String passwordNuevo) { this.passwordNuevo = passwordNuevo; }
}
