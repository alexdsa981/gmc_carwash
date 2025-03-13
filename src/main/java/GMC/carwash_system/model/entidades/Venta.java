package GMC.carwash_system.model.entidades;

import GMC.carwash_system.model.clasificadores.MetodoPago;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

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


    @OneToMany(mappedBy = "venta")
    private List<DetalleMetodoPago> listaDetalleMetodoPago;



    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total;


    @Column(nullable = false)
    private LocalDate fecha;
    @Column(nullable = false)
    private LocalTime hora;
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
