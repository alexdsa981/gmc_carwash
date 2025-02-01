package GMC.carwash_system.model.dto.caja;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MetodoPagoDTO {
    private String nombre;
    private Double total;
}
