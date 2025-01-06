package GMC.carwash_system.model.clasificadores;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ConceptoPago { //Adelantos, Descuentos, Pago de Sueldo, Horas extra, etc
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private Boolean dentro_sueldo;
}
