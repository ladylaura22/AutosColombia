package com.autoscolombia.repository;

import com.autoscolombia.domain.EstadoPago;
import com.autoscolombia.domain.MetodoPago;
import com.autoscolombia.domain.Pago;
import com.autoscolombia.domain.TipoPago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PagoRepository extends JpaRepository<Pago, Integer> {

    Optional<Pago> findByRegistroParqueoIdRegistroAndEstado(Integer idRegistro, EstadoPago estado);

    @Query("SELECT p FROM Pago p " +
           "WHERE (:desde IS NULL OR p.creadoEn >= :desde) " +
           "AND (:hasta IS NULL OR p.creadoEn <= :hasta) " +
           "AND (:tipoPago IS NULL OR p.tipoPago = :tipoPago) " +
           "AND (:metodoPago IS NULL OR p.metodoPago = :metodoPago) " +
           "AND (:estado IS NULL OR p.estado = :estado) " +
           "ORDER BY p.creadoEn DESC")
    List<Pago> buscarConFiltros(
        @Param("desde") LocalDateTime desde,
        @Param("hasta") LocalDateTime hasta,
        @Param("tipoPago") TipoPago tipoPago,
        @Param("metodoPago") MetodoPago metodoPago,
        @Param("estado") EstadoPago estado
    );

    @Query("SELECT p FROM Pago p WHERE p.creadoEn >= :inicio AND p.creadoEn < :fin ORDER BY p.creadoEn ASC")
    List<Pago> findByFecha(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);
}
