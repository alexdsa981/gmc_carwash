package GMC.carwash_system.model;

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
    @JoinColumn(name = "id_venta", nullable = false)
    private Venta venta;

    @ManyToOne
    @JoinColumn(name = "id_tipo_item", nullable = false)
    private TipoItem tipoItem;

    private Long idItem; //se coloca el id del Producto o Servicio

    @Column(nullable = false, columnDefinition = "VARCHAR(MAX)")
    private String descripcion;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio_unitario; //precio por servicio o producto

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal; //multiplicacion de cantidad * precio_unitario

    @ManyToOne
    @JoinColumn(name = "id_colaborador", nullable = false)
    private Colaborador colaborador;

}
