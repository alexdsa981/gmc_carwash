package GMC.carwash_system.repository.entidades;

import GMC.carwash_system.model.entidades.DetalleIngresoVehiculo;
import GMC.carwash_system.model.entidades.HistorialVisitasCliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistorialVisitasClienteRepository extends JpaRepository<HistorialVisitasCliente, Long> {

    @Query("SELECT COUNT(h) FROM HistorialVisitasCliente h " +
            "WHERE h.cliente.id = :idCliente " +
            "AND MONTH(h.fecha) = :mes " +
            "AND YEAR(h.fecha) = :anio")
    Integer contarVisitasPorClienteYMes(@Param("idCliente") Long idCliente,
                                        @Param("mes") Integer mes,
                                        @Param("anio") Integer anio);
}
