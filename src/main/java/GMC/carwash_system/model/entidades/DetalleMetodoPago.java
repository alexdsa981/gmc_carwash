package GMC.carwash_system.model.entidades;

import GMC.carwash_system.model.clasificadores.MetodoPago;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class DetalleMetodoPago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_detalle_metodo", nullable = false)
    private MetodoPago metodoPago;

    @ManyToOne
    @JoinColumn(name = "id_venta", nullable = false)
    @JsonIgnore
    private Venta venta;


    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal monto;

}
