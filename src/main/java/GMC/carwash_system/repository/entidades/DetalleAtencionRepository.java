package GMC.carwash_system.repository.entidades;

import GMC.carwash_system.model.entidades.DetalleAtencion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetalleAtencionRepository extends JpaRepository<DetalleAtencion, Long> {
}
