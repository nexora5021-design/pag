package com.co.nexora.pag.dto;

import java.util.List;

public class VisionGeneralResponse {
    private Double gananciaGenerada;
    private Long prestamosActivos;
    private Long prestamosMora;
    private Double capitalCobrar;
    private Double prestadoMes;
    private Double promedioMes;
    private Long totalPrestamos;
    private Long prestamosPagados;
    private List<MesResumen> meses;

    public VisionGeneralResponse(Double gananciaGenerada, Long prestamosActivos, Long prestamosMora, Double capitalCobrar, Double prestadoMes, Double promedioMes, Long totalPrestamos, Long prestamosPagados, List<MesResumen> meses) {
        this.gananciaGenerada = gananciaGenerada;
        this.prestamosActivos = prestamosActivos;
        this.prestamosMora = prestamosMora;
        this.capitalCobrar = capitalCobrar;
        this.prestadoMes = prestadoMes;
        this.promedioMes = promedioMes;
        this.totalPrestamos = totalPrestamos;
        this.prestamosPagados = prestamosPagados;
        this.meses = meses;
    }

    public Double getGananciaGenerada() { return gananciaGenerada; }
    public Long getPrestamosActivos() { return prestamosActivos; }
    public Long getPrestamosMora() { return prestamosMora; }
    public Double getCapitalCobrar() { return capitalCobrar; }
    public Double getPrestadoMes() { return prestadoMes; }
    public Double getPromedioMes() { return promedioMes; }
    public Long getTotalPrestamos() { return totalPrestamos; }
    public Long getPrestamosPagados() { return prestamosPagados; }
    public List<MesResumen> getMeses() { return meses; }
}
