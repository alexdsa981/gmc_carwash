package GMC.carwash_system.repository.clasificadores;

import GMC.carwash_system.model.clasificadores.TipoVehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoVehiculoRepository extends JpaRepository<TipoVehiculo, Long> {
}
