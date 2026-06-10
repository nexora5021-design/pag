package com.co.nexora.pag.repository;

import com.co.nexora.pag.model.Credito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface CreditoRepository extends JpaRepository<Credito, Long> {
    Page<Credito> findByClienteNombreContainingIgnoreCase(String nombre, Pageable pageable);
    Page<Credito> findByClienteNombreContainingIgnoreCaseAndPrestamistaId(String nombre, Long prestamistaId, Pageable pageable);
    Page<Credito> findByEstadoIgnoreCase(String estado, Pageable pageable);
    Page<Credito> findByPrestamistaId(Long prestamistaId, Pageable pageable);
    Page<Credito> findByEstadoIgnoreCaseAndPrestamistaId(String estado, Long prestamistaId, Pageable pageable);
    Page<Credito> findByClienteIdAndPrestamistaId(Long clienteId, Long prestamistaId, Pageable pageable);
    long countByEstado(String estado);
    long countByPrestamistaId(Long prestamistaId);
    long countByEstadoAndPrestamistaId(String estado, Long prestamistaId);
    Page<Credito> findByClienteId(Long clienteId, Pageable pageable);
    Page<Credito> findBySocioId(Long socioId, Pageable pageable);
    Page<Credito> findByEstadoIgnoreCaseAndSocioId(String estado, Long socioId, Pageable pageable);
    Page<Credito> findByClienteNombreContainingIgnoreCaseAndSocioId(String nombre, Long socioId, Pageable pageable);
    Page<Credito> findByClienteIdAndSocioId(Long clienteId, Long socioId, Pageable pageable);
    long countBySocioId(Long socioId);
    long countByEstadoAndSocioId(String estado, Long socioId);
    List<Credito> findByEstadoAndProximaCuotaBefore(String estado, LocalDate fecha);

    @Query("SELECT COALESCE(SUM(c.interesRecaudado), 0) FROM Credito c WHERE YEAR(c.fechaDesembolso) = :anio")
    Double sumInteresRecaudadoByAnio(@Param("anio") int anio);

    @Query("SELECT COUNT(c) FROM Credito c WHERE c.estado = 'En progreso' AND YEAR(c.fechaDesembolso) = :anio")
    Long countByEstadoProgresoAndAnio(@Param("anio") int anio);

    @Query("SELECT COUNT(c) FROM Credito c WHERE c.estado = 'En mora' AND YEAR(c.fechaDesembolso) = :anio")
    Long countByEstadoMoraAndAnio(@Param("anio") int anio);

    @Query("SELECT COALESCE(SUM(c.capitalPendiente), 0) FROM Credito c WHERE YEAR(c.fechaDesembolso) = :anio")
    Double sumCapitalPendienteByAnio(@Param("anio") int anio);

    @Query("SELECT COALESCE(SUM(c.montoPrestado), 0) FROM Credito c WHERE YEAR(c.fechaDesembolso) = :anio AND MONTH(c.fechaDesembolso) = :mes")
    Double sumMontoPrestadoByAnioAndMes(@Param("anio") int anio, @Param("mes") int mes);

    @Query("SELECT COUNT(c) FROM Credito c WHERE YEAR(c.fechaDesembolso) = :anio AND MONTH(c.fechaDesembolso) = :mes")
    Long countByAnioAndMes(@Param("anio") int anio, @Param("mes") int mes);

    @Query("SELECT COALESCE(SUM(c.gananciaEstimada), 0) FROM Credito c WHERE YEAR(c.fechaDesembolso) = :anio AND MONTH(c.fechaDesembolso) = :mes")
    Double sumGananciaEstimadaByAnioAndMes(@Param("anio") int anio, @Param("mes") int mes);

    @Query("SELECT COALESCE(SUM(c.montoPrestado), 0) FROM Credito c WHERE YEAR(c.fechaDesembolso) = :anio")
    Double sumMontoPrestadoByAnio(@Param("anio") int anio);

    @Query("SELECT COUNT(c) FROM Credito c WHERE YEAR(c.fechaDesembolso) = :anio")
    Long countByAnio(@Param("anio") int anio);

    @Query("SELECT COUNT(c) FROM Credito c WHERE c.estado = 'Finalizado' AND YEAR(c.fechaDesembolso) = :anio")
    Long countByEstadoFinalizadoAndAnio(@Param("anio") int anio);
}
