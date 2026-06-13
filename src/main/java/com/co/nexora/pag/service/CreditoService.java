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

    public Page<Credito> listarPorSocio(Long idSocio, int page, int size) {
        return repository.findBySocioId(idSocio, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id")));
    }

    public Page<Credito> buscarPorNombreClienteYSocio(String nombre, Long idSocio, int page, int size) {
        return repository.findByClienteNombreContainingIgnoreCaseAndSocioId(nombre, idSocio, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id")));
    }

    public CreditoPaginadoResponse listarPorEstadoConContadores(String estado, Long idEmpleado, Long idSocio, int page, int size) {
        Page<Credito> creditos;
        boolean sinEstado = (estado == null || estado.isBlank() || estado.equalsIgnoreCase("null"));
        Long filtroId = idEmpleado != null ? idEmpleado : idSocio;
        boolean filtrarPorSocio = idEmpleado == null && idSocio != null;

        if (sinEstado && filtroId == null) {
            creditos = repository.findAll(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id")));
        } else if (sinEstado && filtrarPorSocio) {
            creditos = repository.findBySocioId(filtroId, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id")));
        } else if (sinEstado) {
            creditos = repository.findByPrestamistaId(filtroId, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id")));
        } else if (filtroId == null) {
            creditos = repository.findByEstadoIgnoreCase(estado, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id")));
        } else if (filtrarPorSocio) {
            creditos = repository.findByEstadoIgnoreCaseAndSocioId(estado, filtroId, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id")));
        } else {
            creditos = repository.findByEstadoIgnoreCaseAndPrestamistaId(estado, filtroId, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id")));
        }

        long total;
        long progress;
        long totalMora;
        long finalizados;

        if (filtrarPorSocio) {
            total = repository.countBySocioId(filtroId);
            progress = repository.countByEstadoAndSocioId("En progreso", filtroId);
            totalMora = repository.countByEstadoAndSocioId("En mora", filtroId);
            finalizados = repository.countByEstadoAndSocioId("Finalizado", filtroId);
        } else if (filtroId != null) {
            total = repository.countByPrestamistaId(filtroId);
            progress = repository.countByEstadoAndPrestamistaId("En progreso", filtroId);
            totalMora = repository.countByEstadoAndPrestamistaId("En mora", filtroId);
            finalizados = repository.countByEstadoAndPrestamistaId("Finalizado", filtroId);
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

    public Page<Credito> buscarPorCliente(Long clienteId, Long idEmpleado, Long idSocio, int page, int size) {
        if (idEmpleado != null) {
            return repository.findByClienteIdAndPrestamistaId(clienteId, idEmpleado, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id")));
        }
        if (idSocio != null) {
            return repository.findByClienteIdAndSocioId(clienteId, idSocio, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id")));
        }
        return repository.findByClienteId(clienteId, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id")));
    }

    public Optional<Credito> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public Credito crear(Credito credito) {
        double porcentaje = credito.getInteres() != null ? credito.getInteres() / 100 : 0.0;
        double intereses = credito.getMontoPrestado() != null ? Math.round(credito.getMontoPrestado() * porcentaje * 100.0) / 100.0 : 0.0;
        credito.setGananciaEstimada(intereses);
        credito.setInteresPendiente(intereses);
        credito.setGanancias(0.0);
        credito.setMesActual(1);
        credito.setCapitalPendiente(credito.getMontoPrestado());
        if (credito.getFechaDesembolso() != null) {
            credito.setFechaCorte(credito.getFechaDesembolso().plusDays(30));
        }
        if (credito.getCliente() != null && credito.getCliente().getNombre() != null) {
            credito.setNombreCliente(credito.getCliente().getNombre());
        }
        if (credito.getPrestamista() != null && credito.getPrestamista().getNombre() != null) {
            credito.setNombrePrestamista(credito.getPrestamista().getNombre());
        }
        if (credito.getSocio() != null && credito.getSocio().getNombre() != null) {
            credito.setNombreSocio(credito.getSocio().getNombre());
        }
        Credito saved = repository.save(credito);
        saved.setTitulo("Credito " + saved.getId());
        return repository.save(saved);
    }

    public Credito actualizar(Long id, Credito credito) {
        credito.setId(id);
        if (credito.getFechaDesembolso() != null) {
            credito.setFechaCorte(credito.getFechaDesembolso().plusDays(30));
        }
        Optional<Credito> existente = repository.findById(id);
        if (existente.isPresent()) {
            Credito actual = existente.get();

            if (credito.getMesActual() != null && actual.getMesActual() != null
                    && credito.getMesActual() > actual.getMesActual()) {

                int nuevoMesActual = credito.getMesActual();
                double tasaMensual = actual.getInteres() / 100;
                double capitalPorMes = actual.getMontoPrestado() / actual.getMeses();
                double saldo = actual.getMontoPrestado();
                double interesPagado = 0;

                for (int i = 0; i < nuevoMesActual; i++) {
                    interesPagado += saldo * tasaMensual;
                    saldo -= capitalPorMes;
                }

                credito.setCapitalPendiente(Math.round(saldo * 100.0) / 100.0);
                credito.setInteresPendiente(Math.round((actual.getGananciaEstimada() - interesPagado) * 100.0) / 100.0);

                // Actualizar fecha de corte
                LocalDate fechaCorte = actual.getFechaCorte();
                int mesesAvanzados = nuevoMesActual - actual.getMesActual();
                if (fechaCorte != null) {
                    fechaCorte = fechaCorte.plusMonths(mesesAvanzados);
                    credito.setFechaCorte(fechaCorte);
                }

                // Si mesActual == meses, finalizar
                if (credito.getMesActual().equals(actual.getMeses())) {
                    credito.setEstado("Finalizado");
                } else if ("En mora".equalsIgnoreCase(actual.getEstado())) {
                    LocalDate hoy = LocalDate.now(ZoneId.of("America/Bogota"));
                    if (fechaCorte != null && fechaCorte.isAfter(hoy)) {
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

    public Optional<Credito> registrarMovimiento(Long id, String accion, Double valor) {
        Optional<Credito> opt = repository.findById(id);
        if (opt.isEmpty()) return opt;
        Credito credito = opt.get();

        if ("abono capital".equalsIgnoreCase(accion)) {
            credito.setCapitalPendiente(Math.round((credito.getCapitalPendiente() - valor) * 100.0) / 100.0);
            if (credito.getCapitalPendiente() <= 0 && credito.getInteresPendiente() <= 0) {
                credito.setEstado("Finalizado");
            } else if ("En mora".equalsIgnoreCase(credito.getEstado())) {
                credito.setEstado("En progreso");
            }
        } else if ("pago intereses".equalsIgnoreCase(accion)) {
            credito.setInteresPendiente(Math.round((credito.getInteresPendiente() - valor) * 100.0) / 100.0);
            credito.setGanancias(Math.round((credito.getGanancias() + valor) * 100.0) / 100.0);
            if (credito.getCapitalPendiente() <= 0 && credito.getInteresPendiente() <= 0) {
                credito.setEstado("Finalizado");
            } else if ("En mora".equalsIgnoreCase(credito.getEstado())) {
                credito.setEstado("En progreso");
            }
        } else if ("nuevo corte".equalsIgnoreCase(accion)) {
            double deudaTotal = credito.getCapitalPendiente() + credito.getInteresPendiente();
            double nuevoInteres = deudaTotal * (credito.getInteres() / 100);
            credito.setInteresPendiente(Math.round((credito.getInteresPendiente() + nuevoInteres) * 100.0) / 100.0);
            credito.setGananciaEstimada(Math.round((credito.getGananciaEstimada() + nuevoInteres) * 100.0) / 100.0);
            credito.setMesActual(credito.getMesActual() + 1);
            credito.setFechaCorte(credito.getFechaCorte().plusDays(30));
            credito.setEstado("En mora");
        }

        return Optional.of(repository.save(credito));
    }

    public void desasociarPrestamista(Long prestamistaId) {
        List<Credito> creditos = repository.findByPrestamistaId(prestamistaId);
        for (Credito c : creditos) {
            c.setPrestamista(null);
            repository.save(c);
        }
    }

    public void desasociarSocio(Long socioId) {
        List<Credito> creditos = repository.findBySocioId(socioId);
        for (Credito c : creditos) {
            c.setSocio(null);
            repository.save(c);
        }
    }

    public void desasociarCliente(Long clienteId) {
        List<Credito> creditos = repository.findByClienteId(clienteId);
        for (Credito c : creditos) {
            c.setCliente(null);
            repository.save(c);
        }
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
