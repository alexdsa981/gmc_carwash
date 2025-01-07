package GMC.carwash_system.repository.entidades;

import GMC.carwash_system.model.entidades.HistorialAlmacen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistorialAlmacenRepository extends JpaRepository<HistorialAlmacen, Long> {
}
