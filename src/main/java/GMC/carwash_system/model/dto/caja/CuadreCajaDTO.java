package GMC.carwash_system.model.dto.caja;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CuadreCajaDTO {
    private Integer totalVehiculos;
    private List<TipoVehiculoDTO> tiposVehiculos;
    private List<ProductoDTO> productos;
    private List<ServicioDTO> servicios;
    private Double totalDinero;

}

