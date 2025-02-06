package GMC.carwash_system.repository.clasificadores;

import GMC.carwash_system.model.clasificadores.TipoServicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TipoServicioRepository extends JpaRepository<TipoServicio, Long> {

    List<TipoServicio> findByIsActiveTrue();
    // Obtener lista de servicios especiales activos (isEspecial = true, isActive = true)
    List<TipoServicio> findByIsEspecialTrueAndIsActiveTrue();

    // Obtener lista de servicios básicos activos (isEspecial = false, isActive = true)
    List<TipoServicio> findByIsEspecialFalseAndIsActiveTrue();
}
