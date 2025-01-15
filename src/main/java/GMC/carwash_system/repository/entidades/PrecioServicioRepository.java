package GMC.carwash_system.repository.entidades;

import GMC.carwash_system.model.entidades.PrecioServicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PrecioServicioRepository extends JpaRepository<PrecioServicio, Long> {
    @Query("SELECT ps FROM PrecioServicio ps WHERE ps.tipo_servicio.id = :idTipoServicio AND ps.tipo_vehiculo.id = :idTipoVehiculo")
    Optional<PrecioServicio> buscarPorTipoServicioYVehiculo(@Param("idTipoServicio") Long idTipoServicio, @Param("idTipoVehiculo") Long idTipoVehiculo);
}
