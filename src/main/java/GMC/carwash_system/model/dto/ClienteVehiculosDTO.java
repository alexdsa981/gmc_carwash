package GMC.carwash_system.model.dto;

import GMC.carwash_system.model.entidades.Cliente;
import GMC.carwash_system.model.entidades.Vehiculo;
import GMC.carwash_system.repository.entidades.VehiculoRepository;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ClienteVehiculosDTO {
    private Long id;
    private String nombre;
    private String identificacion;
    private String telefono;
    private  List<Vehiculo> listaVehiculos;
    private List<String> listaPlacas;
    private String listaPlacasJson;
    private Integer visitas;

    // Constructor sin argumentos necesario para Jackson
    public ClienteVehiculosDTO() {
    }

    // Constructor que inicializa desde un Cliente
    public ClienteVehiculosDTO(Cliente cliente, VehiculoRepository vehiculoRepository) {
        this.id = cliente.getId();
        this.nombre = cliente.getNombre();
        this.identificacion = cliente.getIdentificacion();
        this.telefono = cliente.getTelefono();
        this.listaVehiculos = new ArrayList<>();
        this.listaPlacas = new ArrayList<>();

        Object resultado = vehiculoRepository.obtenerNumeroDeVisitas(this.id);
        if (resultado != null) {
            Object[] data = (Object[]) resultado;
            this.visitas = ((Number) data[1]).intValue();
        }else{
            visitas=0;
        }

        if (cliente.getListaVehiculos() != null){
            this.listaVehiculos = vehiculoRepository.findByCliente(cliente);
            for (Vehiculo vehiculo : listaVehiculos){
                this.listaPlacas.add(vehiculo.getPlaca());
            }
        }

    }
}
