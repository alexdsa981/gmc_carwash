package GMC.carwash_system.repository.entidades;

import GMC.carwash_system.model.entidades.Cliente;
import GMC.carwash_system.model.entidades.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {
    List<Vehiculo> findByCliente(Cliente cliente);
    Optional<Vehiculo> findByPlaca(String placa);

    boolean existsByPlaca(String placa);



    @Query("SELECT c.id, COUNT(distinct v.id) " +
            "FROM Cliente c " +
            "INNER JOIN DetalleIngresoVehiculo div ON div.cliente.id = c.id " +
            "INNER JOIN Venta v ON v.cliente.id = div.cliente.id " +
            "WHERE div.realizado = true AND c.id = :clienteId " +
            "GROUP BY c.id")
    Object obtenerNumeroDeVisitas(@Param("clienteId") Long clienteId);

}
