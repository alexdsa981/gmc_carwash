package GMC.carwash_system.repository.entidades;

import GMC.carwash_system.model.entidades.DetalleMetodoPago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetalleMetodoPagoRepository extends JpaRepository<DetalleMetodoPago, Long> {

    List<DetalleMetodoPago> findByVentaId(Long idVenta);

}
