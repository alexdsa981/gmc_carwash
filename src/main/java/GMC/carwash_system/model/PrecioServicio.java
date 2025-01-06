package GMC.carwash_system.model;


import GMC.carwash_system.model.clasificadores.TipoServicio;
import GMC.carwash_system.model.clasificadores.TipoVehiculo;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class PrecioServicio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_tipo_servicio", nullable = false)
    private TipoServicio tipo_servicio;

    @ManyToOne
    @JoinColumn(name = "id_tipo_vehiculo", nullable = false)
    private TipoVehiculo tipo_vehiculo;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;
}
