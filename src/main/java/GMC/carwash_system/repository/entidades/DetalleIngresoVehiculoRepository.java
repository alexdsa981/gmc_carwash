package GMC.carwash_system.repository.entidades;

import GMC.carwash_system.model.entidades.DetalleIngresoVehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetalleIngresoVehiculoRepository extends JpaRepository<DetalleIngresoVehiculo, Long> {

    // Método para obtener los detalles con 'realizado' == true
    List<DetalleIngresoVehiculo> findByRealizadoTrue();

    // Método para obtener los detalles con 'realizado' == false
    List<DetalleIngresoVehiculo> findByRealizadoFalse();

}
