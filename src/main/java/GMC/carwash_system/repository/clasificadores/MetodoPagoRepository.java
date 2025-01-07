package GMC.carwash_system.repository.clasificadores;

import GMC.carwash_system.model.clasificadores.MetodoPago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MetodoPagoRepository extends JpaRepository<MetodoPago, Long> {
}
