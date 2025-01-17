package GMC.carwash_system.repository.entidades;

import GMC.carwash_system.model.entidades.DetalleVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Long> {


        // Buscar por el ID de DetalleIngreso
        List<DetalleVenta> findByDetalleIngresoVehiculoId(Long detalleIngresoVehiculoId);

}
