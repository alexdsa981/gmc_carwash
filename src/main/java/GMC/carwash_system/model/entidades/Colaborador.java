package GMC.carwash_system.model.entidades;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Colaborador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String nombre;
    private String identificacion;
    private String telefono;

    private String descripcion;
    private Boolean isActive;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal sueldo_fijo;

}
