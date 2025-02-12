package GMC.carwash_system.model.entidades;

import GMC.carwash_system.model.clasificadores.MetodoPago;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "id_metodo_pago", nullable = false)
    private MetodoPago metodoPago;


    @Column(nullable = false)
    private LocalDate fecha;
    @Column(nullable = false)
    private LocalTime hora;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

}
