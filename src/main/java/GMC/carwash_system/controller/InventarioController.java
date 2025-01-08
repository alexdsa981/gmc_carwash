package GMC.carwash_system.controller;


import GMC.carwash_system.model.clasificadores.TipoProducto;
import GMC.carwash_system.model.entidades.HistorialAlmacen;
import GMC.carwash_system.model.entidades.Producto;
import GMC.carwash_system.repository.clasificadores.TipoProductoRepository;
import GMC.carwash_system.repository.entidades.HistorialAlmacenRepository;
import GMC.carwash_system.repository.entidades.ProductoRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/app/inventario")
public class InventarioController {

    @Autowired
    ProductoRepository productoRepository;
    @Autowired
    HistorialAlmacenRepository historialAlmacenRepository;
    @Autowired
    TipoProductoRepository tipoProductoRepository;

    public Model retornalistaTipoProductos(Model model) {
        List<TipoProducto> listaTipoProductos = tipoProductoRepository.findAll();
        model.addAttribute("listaTipoProductos", listaTipoProductos);
        return model;
    }

    public Model retornalistaProductos(Model model) {
        List<Producto> listaProductos = productoRepository.findAll();
        model.addAttribute("listaProductos", listaProductos);
        return model;
    }

    public Model retornaHistorialAlmacen(Model model) {
        List<HistorialAlmacen> listaAlmacen = historialAlmacenRepository.findAll();
        model.addAttribute("listaAlmacen", listaAlmacen);
        return model;
    }
    @PostMapping("/tipoproducto/crear")
    public ResponseEntity<String> crearTipoProducto(
            @RequestParam("nombre")String nombre,
            HttpServletResponse response
    ) throws IOException {
        TipoProducto tipoproducto = TipoProducto.builder()
                .nombre(nombre)
                .build();
        tipoProductoRepository.save(tipoproducto);
        response.sendRedirect("/inventario/lista");
        return ResponseEntity.ok("Producto creado correctamente");
    }

    @PostMapping("/tipoproducto-{id}/editar")
    public ResponseEntity<String> editarTipoProducto(
            @PathVariable Long id,
            @RequestParam("nombre")String nombre,
            HttpServletResponse response
    ) {
        // Buscar al colaborador
        Optional<TipoProducto> optionalTipoProducto = tipoProductoRepository.findById(id);
        if (optionalTipoProducto.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tipo Producto no encontrado");
        }
        TipoProducto tipoProducto = optionalTipoProducto.get();
        tipoProducto.setNombre(nombre);
        tipoProductoRepository.save(tipoProducto);

        return ResponseEntity.ok("Tipo producto editado correctamente");
    }
    //
    //
    //Manejar logica de desactivacion o eliminacion de tipo producot
    //
    //

    @PostMapping("/producto/crear")
    public ResponseEntity<String> crearProducto(
            @RequestParam("nombre")String nombre,
            @RequestParam("precio_costo")BigDecimal precio_costo,
            @RequestParam("precio_venta")BigDecimal precio_venta,
            @RequestParam("id_tipo_producto")Long id_tipo_producto,
            @RequestParam("stock")Integer stock,
            HttpServletResponse response
    ) throws IOException {

        Producto producto = Producto.builder()
                .nombre(nombre)
                .precio_costo(precio_costo)
                .precio_venta(precio_venta)
                .stock(stock)
                .tipo_producto(tipoProductoRepository.findById(id_tipo_producto).get())
                .build();
        productoRepository.save(producto);

        response.sendRedirect("/inventario/lista");
        return ResponseEntity.ok("Producto creado correctamente");
    }

    @PostMapping("/producto-{id}/editar")
    public ResponseEntity<String> editarProducto(
            @PathVariable Long id,
            @RequestParam("nombre")String nombre,
            @RequestParam("precio_costo")BigDecimal precio_costo,
            @RequestParam("precio_venta")BigDecimal precio_venta,
            @RequestParam("id_tipo_producto")Long id_tipo_producto,
            HttpServletResponse response

    ) {
        // Buscar al colaborador
        Optional<Producto> optionalProducto = productoRepository.findById(id);
        if (optionalProducto.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Producto no encontrado");
        }

        Producto producto = optionalProducto.get();
        producto.setNombre(nombre);
        producto.setTipo_producto(tipoProductoRepository.findById(id_tipo_producto).get());
        producto.setPrecio_costo(precio_costo);
        producto.setPrecio_venta(precio_venta);

        //El stock debe cambiarse en otro modal donde se llenen los campos de hisotorial inventario
        //producto.setStock(stock);

        productoRepository.save(producto);
        return ResponseEntity.ok("Producto editado correctamente");
    }

    //
    //
    //falta logica de eliminacion o desactivacion de producto
    //
    //

    @PostMapping("/producto-{id}/stock")
    public ResponseEntity<String> cambiarStock(
            @PathVariable Long id,
            @RequestParam("cantidad")Integer cantidad,
            @RequestParam("motivo")String motivo,
            @RequestParam("precio_venta")BigDecimal precio_unitario, //costo del producto comprado
            @RequestParam("id_producto")Long id_producto,
            HttpServletResponse response
    ) throws IOException {
        Producto producto = productoRepository.findById(id).get();
        if (cantidad < 0){
            producto.setStock(producto.getStock() + cantidad);
        }else if(cantidad > 0){
            if (producto.getStock() < cantidad){
                System.out.println("El Stock es insuficiente, valor negativo");
                //
                //Manejar logica cuando no hay stock
                //
                producto.setStock(producto.getStock() - cantidad);
            }else{
                producto.setStock(producto.getStock() - cantidad);
            }
        }
        HistorialAlmacen historialAlmacen = HistorialAlmacen.builder()
                .cantidad(cantidad)
                .fecha(LocalDate.now())
                .hora(LocalTime.now())
                .motivo(motivo)
                .precioUnitario(precio_unitario)
                .producto(producto)
                .build();
        historialAlmacenRepository.save(historialAlmacen);

        response.sendRedirect("/inventario/lista");
        return ResponseEntity.ok("Stock actualizado  correctamente");
    }

}
