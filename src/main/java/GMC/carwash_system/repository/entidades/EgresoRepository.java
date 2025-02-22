package GMC.carwash_system.repository.entidades;

import GMC.carwash_system.model.entidades.Egreso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EgresoRepository extends JpaRepository<Egreso, Long> {

}
