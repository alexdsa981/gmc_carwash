package GMC.carwash_system.model.dto;

import GMC.carwash_system.model.entidades.Cliente;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ClienteDTO {
    private String nombre;
    private String identificacion;
    private String telefono;
    private List<String> placas;

    // Constructor sin argumentos necesario para Jackson
    public ClienteDTO() {
    }

    // Constructor que inicializa desde un Cliente
    public ClienteDTO(Cliente cliente) {
        this.nombre = cliente.getNombre();
        this.identificacion = cliente.getIdentificacion();
        this.telefono = cliente.getTelefono();
        // Si la lista de vehículos no es nula, obtenemos las placas de los vehículos
        if (cliente.getListaVehiculos() != null) {
            this.placas = cliente.getListaPlacas();
        } else {
            this.placas = new ArrayList<>();
        }
    }
}
