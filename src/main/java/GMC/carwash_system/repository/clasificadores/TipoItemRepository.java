package GMC.carwash_system.repository.clasificadores;

import GMC.carwash_system.model.clasificadores.TipoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoItemRepository extends JpaRepository<TipoItem, Long> {
}
