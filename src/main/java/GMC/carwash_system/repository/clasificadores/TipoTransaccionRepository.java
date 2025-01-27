package GMC.carwash_system.repository.clasificadores;

import GMC.carwash_system.model.clasificadores.TipoTransaccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TipoTransaccionRepository extends JpaRepository<TipoTransaccion, Long> {
    List<TipoTransaccion> findByIsActiveTrue();

}
