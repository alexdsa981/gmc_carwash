package GMC.carwash_system.model.dto;

import java.math.BigDecimal;

public class SueldosDTO {
    private Long id;
    private String nombre;
    private BigDecimal sueldoFijo;
    private BigDecimal montoPagado;
    private String comentario;
    private String fecha;
    private String hora;
    private int tipoOperacion;
    private Boolean estado; // Nuevo campo agregado

    public SueldosDTO(Long id, String nombre, BigDecimal sueldoFijo, BigDecimal montoPagado,
                      String comentario, String fecha, String hora, int tipoOperacion, Boolean estado) {
        this.id = id;
        this.nombre = nombre;
        this.sueldoFijo = sueldoFijo;
        this.montoPagado = montoPagado;
        this.comentario = comentario;
        this.fecha = fecha;
        this.hora = hora;
        this.tipoOperacion = tipoOperacion;
        this.estado = estado; // Inicializamos el nuevo campo
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public BigDecimal getSueldoFijo() {
        return sueldoFijo;
    }

    public BigDecimal getMontoPagado() {
        return montoPagado;
    }

    public String getComentario() {
        return comentario;
    }

    public String getFecha() {
        return fecha;
    }

    public String getHora() {
        return hora;
    }

    public int getTipoOperacion() {
        return tipoOperacion;
    }

    public Boolean getEstado() {
        return estado; // Getter para el nuevo campo
    }
}
