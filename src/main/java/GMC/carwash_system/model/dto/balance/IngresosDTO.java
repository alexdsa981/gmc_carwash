package GMC.carwash_system.model.dto.balance;

import java.math.BigDecimal;
import java.util.List;

public class IngresosDTO {
    private String fecha;
    private String subDia;
    private String dia;
    private BigDecimal subtotal;
    private Integer cantidad;
    private Integer autos;
    private Integer motosL;
    private Integer motosT;
    private List<String> serviciosEspeciales;


}
