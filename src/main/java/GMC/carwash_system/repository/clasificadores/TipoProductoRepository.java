package GMC.carwash_system.repository.clasificadores;

import GMC.carwash_system.model.clasificadores.TipoProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoProductoRepository extends JpaRepository<TipoProducto, Long> {
}
