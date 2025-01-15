package GMC.carwash_system.repository.clasificadores;

import GMC.carwash_system.model.clasificadores.TipoServicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TipoServicioRepository extends JpaRepository<TipoServicio, Long> {

    // Obtener lista de servicios especiales (isEspecial = true)
    List<TipoServicio> findByIsEspecialTrue();

    // Obtener lista de servicios básicos (isEspecial = false)
    List<TipoServicio> findByIsEspecialFalse();
}
