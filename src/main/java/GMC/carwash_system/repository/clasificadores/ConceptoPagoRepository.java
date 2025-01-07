package GMC.carwash_system.repository.clasificadores;

import GMC.carwash_system.model.clasificadores.ConceptoPago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConceptoPagoRepository extends JpaRepository<ConceptoPago, Long> {
}
