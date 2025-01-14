package GMC.carwash_system.model.dto;

import GMC.carwash_system.model.clasificadores.TipoVehiculo;
import GMC.carwash_system.model.entidades.Vehiculo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VehiculoDTO {
    private Long id;
    private String placa;
    private String marca;
    private String modelo;
    private TipoVehiculo tipoVehiculo;

    public VehiculoDTO(Vehiculo vehiculo) {
        this.id = vehiculo.getId();
        this.placa = vehiculo.getPlaca();
        this.marca = vehiculo.getMarca();
        this.modelo = vehiculo.getModelo();
        this.tipoVehiculo = vehiculo.getTipo_vehiculo();
    }



}
