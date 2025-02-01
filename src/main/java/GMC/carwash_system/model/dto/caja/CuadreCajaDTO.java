package GMC.carwash_system.model.dto.caja;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CuadreCajaDTO {

    private String fecha;
    private Integer totalVehiculos;
    private List<TipoVehiculoDTO> tiposVehiculos = new ArrayList<>(); // Inicialización
    private List<ProductoDTO> productos = new ArrayList<>(); // Inicialización
    private List<ServicioDTO> servicios = new ArrayList<>(); // Inicialización
    private List<MetodoPagoDTO> metodosPago = new ArrayList<>(); // Inicialización
    private Double totalDinero;

    // Otros métodos si es necesario
}
