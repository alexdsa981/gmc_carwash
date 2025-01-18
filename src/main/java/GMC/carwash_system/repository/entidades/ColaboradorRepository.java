package GMC.carwash_system.repository.entidades;

import GMC.carwash_system.model.entidades.Colaborador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ColaboradorRepository extends JpaRepository<Colaborador, Long> {
    List<Colaborador> findByIsActiveTrue();
}
