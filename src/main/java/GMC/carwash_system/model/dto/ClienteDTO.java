package GMC.carwash_system.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ClienteDTO {
    private String nombre;
    private String identificacion;
    private String telefono;
    private List<String> placas;
}
