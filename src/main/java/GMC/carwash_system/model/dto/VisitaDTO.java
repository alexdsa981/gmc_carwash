package GMC.carwash_system.model.dto;

import GMC.carwash_system.model.entidades.Venta;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VisitaDTO {
    private Long idCliente;
    private String fecha;
    private String hora;

    // Constructor que recibe una Venta
    public VisitaDTO(Venta venta) {
        this.idCliente = venta.getCliente().getId();
        this.fecha = venta.getFecha().toString(); 
        this.hora = venta.getHora().toString();
    }
}
