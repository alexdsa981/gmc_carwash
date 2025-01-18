package GMC.carwash_system.model.dto;

import GMC.carwash_system.model.clasificadores.TipoItem;
import GMC.carwash_system.model.clasificadores.TipoServicio;
import GMC.carwash_system.model.entidades.Colaborador;
import GMC.carwash_system.model.entidades.DetalleVenta;
import GMC.carwash_system.model.entidades.Venta;
import GMC.carwash_system.repository.clasificadores.TipoServicioRepository;
import GMC.carwash_system.repository.entidades.ProductoRepository;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

@Getter
@Setter
public class DetalleVentaDTO {

    private Long id;
    private Venta venta;
    private String tipoItemNombre;
    private String nombreItem; //se coloca el id del Producto o Servicio
    private Integer cantidad;
    private BigDecimal precio_unitario;
    private BigDecimal subtotal;
    private Colaborador colaborador;
    public DetalleVentaDTO(DetalleVenta detalleVenta) {
        this.id = detalleVenta.getId();
        this.tipoItemNombre = detalleVenta.getTipoItem() != null ? detalleVenta.getTipoItem().getNombre() : "N/A";
        this.cantidad = detalleVenta.getCantidad();
        this.precio_unitario = detalleVenta.getPrecio_unitario();
        this.subtotal = detalleVenta.getSubtotal();
        this.colaborador = detalleVenta.getColaborador();
        this.venta = detalleVenta.getVenta();
    }
}
