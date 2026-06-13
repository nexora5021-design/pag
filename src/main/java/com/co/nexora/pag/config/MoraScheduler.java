package com.co.nexora.pag.config;

import com.co.nexora.pag.model.Credito;
import com.co.nexora.pag.repository.CreditoRepository;
import com.co.nexora.pag.service.CreditoService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Component
public class MoraScheduler {

    private final CreditoRepository creditoRepository;
    private final CreditoService creditoService;

    public MoraScheduler(CreditoRepository creditoRepository, CreditoService creditoService) {
        this.creditoRepository = creditoRepository;
        this.creditoService = creditoService;
    }

    @Scheduled(cron = "0 0 0 * * *", zone = "America/Bogota")
    public void procesarCortes() {
        LocalDate hoy = LocalDate.now(ZoneId.of("America/Bogota"));
        LocalDate fechaObjetivo = hoy.minusDays(4);

        List<Credito> creditos = creditoRepository.findByFechaCorteAndEstadoIn(fechaObjetivo, List.of("En progreso", "En mora"));

        for (Credito credito : creditos) {
            creditoService.registrarMovimiento(credito.getId(), "nuevo corte", null);
        }
    }
}
