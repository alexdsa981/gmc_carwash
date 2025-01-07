package GMC.carwash_system.repository.entidades;

import GMC.carwash_system.model.entidades.PrecioServicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrecioServicioRepository extends JpaRepository<PrecioServicio, Long> {
}
