package GMC.carwash_system.model.dto.caja;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TipoVehiculoDTO {
    private String tipoVehiculo;
    private Integer cantidad;
}