package GMC.carwash_system.repository.entidades;

import GMC.carwash_system.model.entidades.DetalleIngresoVehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetalleIngresoVehiculoRepository extends JpaRepository<DetalleIngresoVehiculo, Long> {

    // Método para obtener los detalles con 'realizado' == true, ordenados por fecha y hora descendente
    List<DetalleIngresoVehiculo> findByRealizadoTrueOrderByFechaDescHoraDesc();

    // Método para obtener los detalles con 'realizado' == false, ordenados por fecha y hora descendente
    List<DetalleIngresoVehiculo> findByRealizadoFalseOrderByFechaDescHoraDesc();


}
