package GMC.carwash_system.model.entidades;

import GMC.carwash_system.model.clasificadores.TipoItem;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class DetalleVenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_venta")
    private Venta venta;

    @ManyToOne
    @JoinColumn(name = "id_tipo_item", nullable = false)
    private TipoItem tipoItem;

    private Long idItem; //se coloca el id del Producto o Servicio

    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio_unitario; //precio por servicio o producto

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal; //multiplicacion de cantidad * precio_unitario

    @ManyToOne
    @JoinColumn(name = "id_colaborador", nullable = false)

    private Colaborador colaborador;

    @ManyToOne
    @JoinColumn(name = "id_entrada_vehiculo", nullable = false)
    private DetalleIngresoVehiculo detalleIngresoVehiculo;

}
