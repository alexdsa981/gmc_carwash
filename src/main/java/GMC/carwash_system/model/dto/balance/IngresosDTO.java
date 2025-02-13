package GMC.carwash_system.model.dto.balance;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IngresosDTO {
    private String fecha;
    private String subDia;
    private Integer dia;  // Cambiar de String a Integer
    private BigDecimal subtotal;
    private Integer cantidad;
    private Integer autos;
    private Integer motosL;
    private Integer motosT;
    private List<String> serviciosEspeciales;

    // Constructor sin servicios especiales
    public IngresosDTO(String fecha, String subDia, Integer dia, BigDecimal subtotal, Integer cantidad, Integer autos, Integer motosL, Integer motosT) {
        this.fecha = fecha;
        this.subDia = subDia;
        this.dia = dia;
        this.subtotal = subtotal;
        this.cantidad = cantidad;
        this.autos = autos;
        this.motosL = motosL;
        this.motosT = motosT;
        this.serviciosEspeciales = new ArrayList<>();
    }
}
