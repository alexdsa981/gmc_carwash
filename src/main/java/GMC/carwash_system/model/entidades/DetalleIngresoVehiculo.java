package GMC.carwash_system.model.entidades;

import GMC.carwash_system.model.dto.DetalleVentaDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class DetalleIngresoVehiculo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_vehiculo", nullable = false)
    private Vehiculo vehiculo;

    private Boolean realizado;

    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false)
    private LocalTime hora;

    @OneToMany(mappedBy = "detalleIngresoVehiculo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleVenta> listaDetalleVentas = new ArrayList<>();

    @Transient
    private List<DetalleVentaDTO> listaDetalleVentasDTO = new ArrayList<>();

}
