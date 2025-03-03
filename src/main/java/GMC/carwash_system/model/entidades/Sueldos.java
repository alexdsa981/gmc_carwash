package GMC.carwash_system.model.entidades;

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
public class Sueldos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_colaborador", nullable = false)
    private Colaborador colaborador;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal monto;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false)
    private LocalTime hora;

    private String comentario;

    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private int tipoOperacion;

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
