package com.autoscolombia.repository;

import com.autoscolombia.domain.LugarActual;
import com.autoscolombia.domain.RegistroParqueo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RegistroParqueoRepository extends JpaRepository<RegistroParqueo, Integer> {

    @Query("""
           select rp
           from RegistroParqueo rp
           where rp.vehiculo.placa = :placa
             and rp.horaSalida is null
           """)
    Optional<RegistroParqueo> findActivoByPlaca(String placa);

    @Query("""
           select rp
           from RegistroParqueo rp
           where rp.vehiculo.placa = :placa
           order by rp.horaIngreso desc
           """)
    List<RegistroParqueo> findUltimosPorPlaca(String placa);

    List<RegistroParqueo> findByLugarActual(LugarActual lugarActual);
}
