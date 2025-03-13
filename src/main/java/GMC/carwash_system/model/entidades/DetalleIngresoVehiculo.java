package GMC.carwash_system.model.entidades;

import GMC.carwash_system.model.dto.DetalleVentaDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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

    @Transient
    private Venta venta;

    public Venta getVenta(){
        DetalleVentaDTO detalleVentaDTO= this.listaDetalleVentasDTO.get(0);
        this.venta = detalleVentaDTO.getVenta();
        return venta;
    }


    @Transient
    private String formattedFecha;  // Este campo no será persistido en la base de datos

    @Transient
    private String formattedHora;   // Campo para la hora formateada

    public String getFormattedFecha() {
        if (fecha != null) {
            // Usar DateTimeFormatter para formatear LocalDate
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return fecha.format(formatter);
        }
        return "";
    }

    // Método para obtener la hora formateada
    public String getFormattedHora() {
        if (hora != null) {
            // Usar DateTimeFormatter para formatear LocalTime
            return hora.format(DateTimeFormatter.ofPattern("HH:mm"));
        }
        return "";
    }





}
