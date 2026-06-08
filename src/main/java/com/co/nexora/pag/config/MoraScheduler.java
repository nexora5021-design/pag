package com.co.nexora.pag.config;

import com.co.nexora.pag.model.Credito;
import com.co.nexora.pag.repository.CreditoRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Component
public class MoraScheduler {

    private final CreditoRepository creditoRepository;

    public MoraScheduler(CreditoRepository creditoRepository) {
        this.creditoRepository = creditoRepository;
    }

    @Scheduled(cron = "0 0 0 * * *", zone = "America/Bogota")
    public void marcarCreditosEnMora() {
        LocalDate hoy = LocalDate.now(ZoneId.of("America/Bogota"));
        LocalDate limite = hoy.minusDays(2);

        List<Credito> creditosVencidos = creditoRepository.findByEstadoAndProximaCuotaBefore("En progreso", limite);

        for (Credito credito : creditosVencidos) {
            credito.setEstado("En mora");
            creditoRepository.save(credito);
        }
    }
}
