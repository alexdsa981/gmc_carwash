package GMC.carwash_system.repository.clasificadores;

import GMC.carwash_system.model.clasificadores.ConceptoPago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConceptoPagoRepository extends JpaRepository<ConceptoPago, Long> {
    
    List<ConceptoPago> findByIsActiveTrue();
}
