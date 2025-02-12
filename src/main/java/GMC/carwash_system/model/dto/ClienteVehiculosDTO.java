package GMC.carwash_system.model.dto;

import GMC.carwash_system.model.entidades.Cliente;
import GMC.carwash_system.model.entidades.Vehiculo;
import GMC.carwash_system.model.entidades.Venta;
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
    private  List<VisitaDTO> listaVisitas;
    private Integer visitas;

    // Constructor sin argumentos necesario para Jackson
    public ClienteVehiculosDTO() {
    }

    public ClienteVehiculosDTO(Cliente cliente) {
        this.id = cliente.getId();
        this.nombre = cliente.getNombre();
        this.identificacion = cliente.getIdentificacion();
        this.telefono = cliente.getTelefono();
        this.listaVehiculos = new ArrayList<>();
        this.listaPlacas = new ArrayList<>();
        this.listaVisitas = new ArrayList<>();
        for (Venta venta : cliente.getListaCompras()){
            VisitaDTO visitaDTO = new VisitaDTO(venta);
            listaVisitas.add(visitaDTO);
        }

        this.visitas = listaVisitas.isEmpty() ? 0 : listaVisitas.size();

        if (!cliente.getListaVehiculos().isEmpty()){
            this.listaVehiculos = cliente.getListaVehiculos();
            for (Vehiculo vehiculo : listaVehiculos){
                this.listaPlacas.add(vehiculo.getPlaca());
            }
        }
    }
}
