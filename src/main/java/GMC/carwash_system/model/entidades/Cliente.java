package GMC.carwash_system.model.entidades;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String identificacion;
    private String telefono;
    private Boolean isActive;

    @OneToMany(mappedBy = "cliente")
    private List<Vehiculo> listaVehiculos;

    @Transient
    private List<String> listaPlacas;

    @Transient
    private String listaPlacasJson;  // Nueva propiedad para almacenar la lista de placas como JSON

    public List<String> getListaPlacas(){
        List<String> listaPlacas = new ArrayList<>();
        for (Vehiculo vehiculo : listaVehiculos){
            listaPlacas.add(vehiculo.getPlaca());
        }
        return listaPlacas;
    }

}
