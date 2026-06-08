package com.co.nexora.pag.service;

import com.co.nexora.pag.dto.CreditoPaginadoResponse;
import com.co.nexora.pag.dto.MesResumen;
import com.co.nexora.pag.dto.VisionGeneralResponse;
import com.co.nexora.pag.model.Credito;
import com.co.nexora.pag.repository.CreditoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
public class CreditoService {

    private final CreditoRepository repository;

    public CreditoService(CreditoRepository repository) {
        this.repository = repository;
    }

    public List<Credito> listarTodos() {
        return repository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    public Page<Credito> listarPaginado(int page, int size) {
        return repository.findAll(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id")));
    }

    public Page<Credito> buscarPorNombreCliente(String nombre, int page, int size) {
        return repository.findByClienteNombreContainingIgnoreCase(nombre, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id")));
    }

    public Page<Credito> buscarPorNombreClienteYPrestamista(String nombre, Long idEmpleado, int page, int size) {
        return repository.findByClienteNombreContainingIgnoreCaseAndPrestamistaId(nombre, idEmpleado, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id")));
    }

    public Page<Credito> listarPorPrestamista(Long idEmpleado, int page, int size) {
        return repository.findByPrestamistaId(idEmpleado, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id")));
    }

    public CreditoPaginadoResponse listarPorEstadoConContadores(String estado, Long idEmpleado, int page, int size) {
        Page<Credito> creditos;
        boolean sinEstado = (estado == null || estado.isBlank() || estado.equalsIgnoreCase("null"));

        if (sinEstado && idEmpleado == null) {
            creditos = repository.findAll(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id")));
        } else if (sinEstado) {
            creditos = repository.findByPrestamistaId(idEmpleado, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id")));
        } else if (idEmpleado == null) {
            creditos = repository.findByEstadoIgnoreCase(estado, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id")));
        } else {
            creditos = repository.findByEstadoIgnoreCaseAndPrestamistaId(estado, idEmpleado, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id")));
        }

        long total;
        long progress;
        long totalMora;
        long finalizados;

        if (idEmpleado != null) {
            total = repository.countByPrestamistaId(idEmpleado);
            progress = repository.countByEstadoAndPrestamistaId("En progreso", idEmpleado);
            totalMora = repository.countByEstadoAndPrestamistaId("En mora", idEmpleado);
            finalizados = repository.countByEstadoAndPrestamistaId("Finalizado", idEmpleado);
        } else {
            total = repository.count();
            progress = repository.countByEstado("En progreso");
            totalMora = repository.countByEstado("En mora");
            finalizados = repository.countByEstado("Finalizado");
        }

        return new CreditoPaginadoResponse(creditos, total, progress, totalMora, finalizados);
    }

    public Page<Credito> buscarPorEstado(String estado, int page, int size) {
        return repository.findByEstadoIgnoreCase(estado, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id")));
    }

    public Page<Credito> buscarPorCliente(Long clienteId, Long idEmpleado, int page, int size) {
        if (idEmpleado != null) {
            return repository.findByClienteIdAndPrestamistaId(clienteId, idEmpleado, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id")));
        }
        return repository.findByClienteId(clienteId, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id")));
    }

    public Optional<Credito> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public Credito crear(Credito credito) {
        if (credito.getMontoPrestado() != null && credito.getInteres() != null && credito.getCuotas() != null && credito.getCuotas() > 0) {
            double p = credito.getMontoPrestado();
            double tasaMensual = credito.getInteres() / 100;
            int n = credito.getCuotas();
            int cuotasPorMes;
            if ("semanal".equalsIgnoreCase(credito.getTipoCredito())) {
                cuotasPorMes = 4;
            } else if ("quincenal".equalsIgnoreCase(credito.getTipoCredito())) {
                cuotasPorMes = 2;
            } else {
                cuotasPorMes = 1;
            }

            double capitalPorCuota = p / n;
            double saldo = p;
            double totalIntereses = 0;
            double primeraCuotaValor = 0;
            int cuotasProcesadas = 0;

            while (cuotasProcesadas < n) {
                double interesMes = saldo * tasaMensual;
                int cuotasRestantes = n - cuotasProcesadas;
                int cuotasEsteMes = Math.min(cuotasPorMes, cuotasRestantes);
                double interesPorCuota = interesMes / cuotasEsteMes;

                for (int j = 0; j < cuotasEsteMes; j++) {
                    totalIntereses += interesPorCuota;
                    if (cuotasProcesadas == 0) {
                        primeraCuotaValor = capitalPorCuota + interesPorCuota;
                    }
                    cuotasProcesadas++;
                }
                saldo -= capitalPorCuota * cuotasEsteMes;
            }

            credito.setValorCuota(Math.round(primeraCuotaValor * 100.0) / 100.0);
            credito.setGananciaEstimada(Math.round(totalIntereses * 100.0) / 100.0);
        }
        credito.setProximaCuota(credito.getPrimeraCuota());
        credito.setCuotaActual(0);
        credito.setCapitalPendiente(credito.getMontoPrestado());
        credito.setInteresRecaudado(0.0);
        Credito saved = repository.save(credito);
        saved.setTitulo("Credito " + saved.getId());
        return repository.save(saved);
    }

    public Credito actualizar(Long id, Credito credito) {
        credito.setId(id);
        Optional<Credito> existente = repository.findById(id);
        if (existente.isPresent()) {
            Credito actual = existente.get();

            if (credito.getCuotaActual() != null && actual.getCuotaActual() != null
                    && credito.getCuotaActual() > actual.getCuotaActual()) {

                int nuevaCuotaActual = credito.getCuotaActual();
                double tasaMensual = actual.getInteres() / 100;
                int cuotasPorMes;
                if ("semanal".equalsIgnoreCase(actual.getTipoCredito())) {
                    cuotasPorMes = 4;
                } else if ("quincenal".equalsIgnoreCase(actual.getTipoCredito())) {
                    cuotasPorMes = 2;
                } else {
                    cuotasPorMes = 1;
                }

                double capitalPorCuota = actual.getMontoPrestado() / actual.getCuotas();
                double saldo = actual.getMontoPrestado();
                double interesTotal = 0;
                int cuotasProcesadas = 0;

                while (cuotasProcesadas < nuevaCuotaActual) {
                    double interesMes = saldo * tasaMensual;
                    int cuotasRestantesTotal = actual.getCuotas() - cuotasProcesadas;
                    int cuotasEsteMes = Math.min(cuotasPorMes, cuotasRestantesTotal);
                    double interesPorCuota = interesMes / cuotasEsteMes;
                    int cuotasAProcesar = Math.min(cuotasEsteMes, nuevaCuotaActual - cuotasProcesadas);

                    for (int j = 0; j < cuotasAProcesar; j++) {
                        interesTotal += interesPorCuota;
                        cuotasProcesadas++;
                    }
                    saldo -= capitalPorCuota * cuotasAProcesar;
                }

                credito.setCapitalPendiente(Math.round(saldo * 100.0) / 100.0);
                credito.setInteresRecaudado(Math.round(interesTotal * 100.0) / 100.0);

                // Calcular valor de la siguiente cuota
                if (cuotasProcesadas < actual.getCuotas()) {
                    // Determinar el saldo al inicio del mes actual de la siguiente cuota
                    double saldoParaSiguiente = actual.getMontoPrestado();
                    int cuotasRecorridas = 0;
                    double interesSiguienteCuota = 0;

                    while (cuotasRecorridas <= cuotasProcesadas) {
                        double interesMesCalc = saldoParaSiguiente * tasaMensual;
                        int cuotasRestantesCalc = actual.getCuotas() - cuotasRecorridas;
                        int cuotasEsteMesCalc = Math.min(cuotasPorMes, cuotasRestantesCalc);
                        interesSiguienteCuota = interesMesCalc / cuotasEsteMesCalc;

                        if (cuotasRecorridas + cuotasEsteMesCalc > cuotasProcesadas) {
                            // La siguiente cuota está en este mes
                            break;
                        }
                        saldoParaSiguiente -= capitalPorCuota * cuotasEsteMesCalc;
                        cuotasRecorridas += cuotasEsteMesCalc;
                    }

                    credito.setValorCuota(Math.round((capitalPorCuota + interesSiguienteCuota) * 100.0) / 100.0);
                }

                // Actualizar próxima cuota
                int cuotasPagadasNuevas = credito.getCuotaActual() - actual.getCuotaActual();
                LocalDate proximaCuota = actual.getProximaCuota() != null ? actual.getProximaCuota() : actual.getPrimeraCuota();
                for (int i = 0; i < cuotasPagadasNuevas; i++) {
                    if ("semanal".equalsIgnoreCase(actual.getTipoCredito())) {
                        proximaCuota = proximaCuota.plusDays(7);
                    } else if ("quincenal".equalsIgnoreCase(actual.getTipoCredito())) {
                        proximaCuota = proximaCuota.plusDays(15);
                    } else {
                        proximaCuota = proximaCuota.plusDays(30);
                    }
                }
                credito.setProximaCuota(proximaCuota);

                // Si cuotaActual == cuotas, finalizar
                if (credito.getCuotaActual().equals(actual.getCuotas())) {
                    credito.setEstado("Finalizado");
                } else if ("En mora".equalsIgnoreCase(actual.getEstado())) {
                    LocalDate hoy = LocalDate.now(ZoneId.of("America/Bogota"));
                    if (proximaCuota.isAfter(hoy)) {
                        credito.setEstado("En progreso");
                    }
                }
            }
        }
        return repository.save(credito);
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }

    public VisionGeneralResponse obtenerVisionGeneral() {
        LocalDate hoy = LocalDate.now(ZoneId.of("America/Bogota"));
        int anio = hoy.getYear();
        int mes = hoy.getMonthValue();

        Double gananciaGenerada = repository.sumInteresRecaudadoByAnio(anio);
        Long prestamosActivos = repository.countByEstadoProgresoAndAnio(anio);
        Long prestamosMora = repository.countByEstadoMoraAndAnio(anio);
        Double capitalCobrar = repository.sumCapitalPendienteByAnio(anio);
        Double prestadoMes = repository.sumMontoPrestadoByAnioAndMes(anio, mes);

        String[] nombresMeses = {"Ene", "Feb", "Mar", "Abr", "May", "Jun",
                "Jul", "Ago", "Sep", "Oct", "Nov", "Dic"};

        List<MesResumen> meses = new java.util.ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            Long creditos = repository.countByAnioAndMes(anio, i);
            Double gananciaMes = repository.sumGananciaEstimadaByAnioAndMes(anio, i);
            meses.add(new MesResumen(nombresMeses[i - 1], creditos, gananciaMes));
        }

        double sumaGanancias = meses.stream().mapToDouble(MesResumen::getGananciaMes).sum();
        long mesesConGanancia = meses.stream().filter(m -> m.getGananciaMes() > 0).count();
        Double promedioMes = mesesConGanancia > 0 ? Math.round((sumaGanancias / mesesConGanancia) * 100.0) / 100.0 : 0.0;

        Double totalPrestamos = repository.sumMontoPrestadoByAnio(anio);
        Long totalPrestamosCount = repository.countByAnio(anio);
        Long prestamosPagados = repository.countByEstadoFinalizadoAndAnio(anio);

        return new VisionGeneralResponse(gananciaGenerada, prestamosActivos, prestamosMora, capitalCobrar, prestadoMes, promedioMes, totalPrestamosCount, prestamosPagados, meses);
    }
}
