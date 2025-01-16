package GMC.carwash_system.repository.entidades;

import GMC.carwash_system.model.entidades.DetalleIngresoVehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetalleIngresoVehiculoRepository extends JpaRepository<DetalleIngresoVehiculo, Long> {
}
