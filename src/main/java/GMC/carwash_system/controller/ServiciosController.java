package GMC.carwash_system.controller;


import GMC.carwash_system.model.clasificadores.ConceptoPago;
import GMC.carwash_system.model.clasificadores.TipoServicio;
import GMC.carwash_system.model.clasificadores.TipoVehiculo;
import GMC.carwash_system.model.entidades.PrecioServicio;
import GMC.carwash_system.model.entidades.Producto;
import GMC.carwash_system.model.entidades.Sueldos;
import GMC.carwash_system.repository.clasificadores.TipoServicioRepository;
import GMC.carwash_system.repository.clasificadores.TipoVehiculoRepository;
import GMC.carwash_system.repository.entidades.PrecioServicioRepository;
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
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/app/servicios")
public class ServiciosController {

    @Autowired
    TipoServicioRepository tipoServicioRepository;
    @Autowired
    PrecioServicioRepository precioServicioRepository;
    @Autowired
    TipoVehiculoRepository tipoVehiculoRepository;
    @Autowired
    ProductoRepository productoRepository;


    public Model retornaListaTipoServicio(Model model) {
        List<TipoServicio> listaTipoServicio = tipoServicioRepository.findAll();
        model.addAttribute("listaTipoServicio", listaTipoServicio);
        return model;
    }
    public Model retornaListaPrecioServicio(Model model) {
        List<PrecioServicio> listaPrecioServicio = precioServicioRepository.findAll();
        model.addAttribute("listaPrecioServicio", listaPrecioServicio);
        return model;
    }
    public Model retornaListaTipoVehiculo(Model model) {
        List<TipoVehiculo> listaTipoVehiculo= tipoVehiculoRepository.findAll();
        model.addAttribute("listaTipoVehiculo", listaTipoVehiculo);
        return model;
    }
    public Model retornaListaProducto(Model model) {
        List<Producto> listaProducto= productoRepository.findAll();
        model.addAttribute("listaProducto", listaProducto);
        return model;
    }


    @PostMapping("/crear-tipo-servicio")
    public ResponseEntity<String> crearTipoServicio(
            @RequestParam("nombre")String nombre,
            @RequestParam("descripcion")String descripcion,
            HttpServletResponse response
    ) throws IOException {
        TipoServicio tipoServicio = new TipoServicio();
        tipoServicio.setDescripcion(descripcion);
        tipoServicio.setNombre(nombre);
        tipoServicioRepository.save(tipoServicio);
        response.sendRedirect("/servicios/lista");
        return ResponseEntity.ok("Tipo Servicio creado correctamente");
    }

    @PostMapping("/editar-tipo-servicio")
    public ResponseEntity<String> editarTipoServicio(
            @PathVariable Long id,
            @RequestParam("nombre")String nombre,
            @RequestParam("descripcion")String descripcion,
            HttpServletResponse response
    ) throws IOException {
        // Buscar al colaborador
        Optional<TipoServicio> optionalTipoServicio = tipoServicioRepository.findById(id);
        if (optionalTipoServicio.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Concepto Pago no encontrado");
        }
        TipoServicio tipoServicio = optionalTipoServicio.get();
        tipoServicio.setNombre(nombre);
        tipoServicio.setDescripcion(descripcion);
        tipoServicioRepository.save(tipoServicio);

        response.sendRedirect("/colaboradores/lista");
        return ResponseEntity.ok("Tipo Servicio editado correctamente");
    }

    //
    //Logica de Desactivación o eliminacion de tipo servicio
    //

    @PostMapping("/crear-precio-servicio")
    public ResponseEntity<String> crearPrecioServicio(
            @RequestParam("precio") BigDecimal precio,
            @RequestParam("idTipoServicio")Long idTipoServicio,
            @RequestParam("idTipoVehiculo")Long idTipoVehiculo,
            HttpServletResponse response
    ) throws IOException {

        PrecioServicio nuevoPrecioServicio = new PrecioServicio();
        nuevoPrecioServicio.setPrecio(precio);
        nuevoPrecioServicio.setTipo_servicio(tipoServicioRepository.findById(idTipoServicio).get());
        nuevoPrecioServicio.setTipo_vehiculo(tipoVehiculoRepository.findById(idTipoVehiculo).get());
        precioServicioRepository.save(nuevoPrecioServicio);
        response.sendRedirect("/servicios/lista");
        return ResponseEntity.ok("Tipo Servicio creado correctamente");
    }

    @PostMapping("/editar-precio-servicio")
    public ResponseEntity<String> editarPrecioServicio(
            @PathVariable Long id,
            @RequestParam("precio") BigDecimal precio,
            @RequestParam("idTipoServicio")Long idTipoServicio,
            @RequestParam("idTipoVehiculo")Long idTipoVehiculo,
            HttpServletResponse response
    ) throws IOException {
        // Buscar al colaborador
        Optional<PrecioServicio> optionalPrecioServicio= precioServicioRepository.findById(id);
        if (optionalPrecioServicio.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("PrecioServicio no encontrado");
        }
        PrecioServicio precioServicio = optionalPrecioServicio.get();
        precioServicio.setTipo_servicio(tipoServicioRepository.findById(idTipoServicio).get());
        precioServicio.setTipo_vehiculo(tipoVehiculoRepository.findById(idTipoVehiculo).get());
        precioServicio.setPrecio(precio);
        precioServicioRepository.save(precioServicio);

        response.sendRedirect("/servicios/lista");
        return ResponseEntity.ok("PrecioServicio editado correctamente");
    }

    //
    //Logica de Desactivación o eliminacion de precio servicio
    //
}
