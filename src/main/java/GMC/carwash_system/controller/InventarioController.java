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
import org.springframework.web.bind.annotation.*;

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
        List<Producto> listaProductos = productoRepository.findByIsActiveTrue();
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
        TipoProducto tipoProducto = new TipoProducto();
        tipoProducto.setNombre(nombre);
        tipoProductoRepository.save(tipoProducto);
        response.sendRedirect("/inventario/lista");
        return ResponseEntity.ok("Tipo producto creado correctamente");
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
            HttpServletResponse response
    ) throws IOException {

        Producto producto = new Producto();
        producto.setNombre(nombre);
        producto.setPrecio_costo(precio_costo);
        producto.setPrecio_venta(precio_venta);
        producto.setStock(0);
        producto.setTipo_producto(tipoProductoRepository.findById(id_tipo_producto).get());
        producto.setIsActive(Boolean.TRUE);
        productoRepository.save(producto);

        response.sendRedirect("/inventario/lista");
        return ResponseEntity.ok("Producto creado correctamente");
    }

    @PostMapping("/producto-{id}/editar")
    public ResponseEntity<String> editarProducto(
            @PathVariable Long id,
            @RequestParam("nombre") String nombre,
            @RequestParam("precio_costo") BigDecimal precioCosto,
            @RequestParam("precio_venta") BigDecimal precioVenta,
            @RequestParam("id_tipo_producto") Long idTipoProducto
    ) {
        // Buscar el producto
        Optional<Producto> optionalProducto = productoRepository.findById(id);
        if (optionalProducto.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Producto no encontrado");
        }

        // Buscar el tipo de producto
        Optional<TipoProducto> optionalTipoProducto = tipoProductoRepository.findById(idTipoProducto);
        if (optionalTipoProducto.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tipo de producto no encontrado");
        }

        // Actualizar los datos del producto
        Producto producto = optionalProducto.get();
        producto.setNombre(nombre);
        producto.setTipo_producto(optionalTipoProducto.get());
        producto.setPrecio_costo(precioCosto);
        producto.setPrecio_venta(precioVenta);

        // Guardar cambios en la base de datos
        productoRepository.save(producto);

        return ResponseEntity.ok("Producto editado correctamente");
    }

    @PostMapping("/producto-{id}/eliminar")
    public ResponseEntity<String> eliminarProducto(@PathVariable Long id) {
        // Buscar el producto por ID
        Optional<Producto> optionalProducto = productoRepository.findById(id);
        if (optionalProducto.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Producto no encontrado");
        }

        // Eliminar el producto
        Producto producto = optionalProducto.get();
        producto.setIsActive(Boolean.FALSE);
        productoRepository.save(producto);

        return ResponseEntity.ok("Producto eliminado correctamente");
    }


    @PostMapping("/producto-{id}/stock")
    public ResponseEntity<String> cambiarStock(
            @PathVariable Long id,
            @RequestParam("cantidad") Integer cantidad,
            @RequestParam("motivo") String motivo,
            @RequestParam("precio_venta") BigDecimal precio_unitario,
            HttpServletResponse response
    ) throws IOException {
        Producto producto = productoRepository.findById(id).get();
        if (cantidad < 0) {
            // Salida de stock (resta)
            if (producto.getStock() < Math.abs(cantidad)) {
                System.out.println("El Stock es insuficiente, valor negativo");
                // Manejar caso de stock insuficiente (por ejemplo, lanzar una excepción o devolver un mensaje)
                return ResponseEntity.badRequest().body("Stock insuficiente");
            } else {
                producto.setStock(producto.getStock() + cantidad); // Resta la cantidad negativa del stock
            }
        } else if (cantidad > 0) {
            // Entrada de stock (suma)
            producto.setStock(producto.getStock() + cantidad); // Sumar la cantidad positiva al stock
        }


        HistorialAlmacen historialAlmacen = new HistorialAlmacen();
        historialAlmacen.setCantidad(cantidad);
        historialAlmacen.setFecha(LocalDate.now());
        historialAlmacen.setHora(LocalTime.now());
        historialAlmacen.setMotivo(motivo);
        historialAlmacen.setPrecioUnitario(precio_unitario);
        historialAlmacen.setProducto(producto);
        historialAlmacenRepository.save(historialAlmacen);

        response.sendRedirect("/inventario/lista");
        return ResponseEntity.ok("Stock actualizado correctamente");
    }


    @DeleteMapping("/eliminar-historial/{id}")
    public ResponseEntity<Void> eliminarHistorial(@PathVariable Long id) {
        Optional<HistorialAlmacen> historialOpt = historialAlmacenRepository.findById(id);
        if (historialOpt.isPresent()) {
            HistorialAlmacen historial = historialOpt.get();

            // Obtener la cantidad del historial
            int cantidad = historial.getCantidad();
            Producto producto = historial.getProducto();

            // Ajustar el stock según la cantidad del historial
            if (cantidad > 0) {
                // Si la cantidad es positiva (entrada), se resta al stock
                producto.setStock(producto.getStock() - cantidad);
            } else {
                // Si la cantidad es negativa (salida), se suma al stock
                producto.setStock(producto.getStock() + Math.abs(cantidad));
            }

            // Guardar el producto con el nuevo stock
            productoRepository.save(producto);

            // Eliminar el historial de almacén
            historialAlmacenRepository.delete(historial);

            return ResponseEntity.noContent().build(); // Devuelve 204 No Content si se eliminó correctamente
        }
        return ResponseEntity.notFound().build(); // Devuelve 404 si no se encuentra el historial
    }



}
