package com.co.nexora.pag.dto;

import com.co.nexora.pag.model.Credito;
import org.springframework.data.domain.Page;

public class CreditoPaginadoResponse {

    private Page<Credito> creditos;
    private long total;
    private long progress;
    private long totalMora;
    private long finalizados;

    public CreditoPaginadoResponse(Page<Credito> creditos, long total, long progress, long totalMora, long finalizados) {
        this.creditos = creditos;
        this.total = total;
        this.progress = progress;
        this.totalMora = totalMora;
        this.finalizados = finalizados;
    }

    public Page<Credito> getCreditos() { return creditos; }
    public long getTotal() { return total; }
    public long getProgress() { return progress; }
    public long getTotalMora() { return totalMora; }
    public long getFinalizados() { return finalizados; }
}
