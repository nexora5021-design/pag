package com.co.nexora.pag.dto;

public class MesResumen {
    private String nombreMes;
    private Long creditos;
    private Double gananciaMes;

    public MesResumen(String nombreMes, Long creditos, Double gananciaMes) {
        this.nombreMes = nombreMes;
        this.creditos = creditos;
        this.gananciaMes = gananciaMes;
    }

    public String getNombreMes() { return nombreMes; }
    public Long getCreditos() { return creditos; }
    public Double getGananciaMes() { return gananciaMes; }
}
