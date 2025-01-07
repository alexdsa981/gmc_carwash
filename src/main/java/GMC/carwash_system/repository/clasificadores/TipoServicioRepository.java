package GMC.carwash_system.repository.clasificadores;

import GMC.carwash_system.model.clasificadores.TipoServicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoServicioRepository extends JpaRepository<TipoServicio, Long> {
}
