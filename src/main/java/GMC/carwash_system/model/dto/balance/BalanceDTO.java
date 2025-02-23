package GMC.carwash_system.model.dto.balance;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BalanceDTO {
    private String fecha;
    private String subDia;
    private int dia;
    private BigDecimal ingresos;
    private BigDecimal egresos;
    private BigDecimal balance;

}

