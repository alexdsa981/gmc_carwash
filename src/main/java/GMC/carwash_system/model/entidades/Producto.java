package GMC.carwash_system.model.entidades;

import GMC.carwash_system.model.clasificadores.TipoProducto;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio_costo;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio_venta;

    @ManyToOne
    @JoinColumn(name = "id_tipo_producto", nullable = false)
    private TipoProducto tipo_producto;

    @Column(nullable = false)
    private Integer stock;


}
