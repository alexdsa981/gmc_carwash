package GMC.carwash_system.controller;

import GMC.carwash_system.model.clasificadores.*;
import GMC.carwash_system.repository.clasificadores.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/app/clasificadores")
public class ClasificadoresController {
    @Autowired
    ConceptoPagoRepository conceptoPagoRepository;
    @Autowired
    MetodoPagoRepository metodoPagoRepository;
    @Autowired
    TipoProductoRepository productoRepository;
    @Autowired
    TipoServicioRepository tipoServicioRepository;
    @Autowired
    TipoTransaccionRepository tipoTransaccionRepository;
    @Autowired
    TipoVehiculoRepository tipoVehiculoRepository;


    public Model getListaConceptoPagoActivos(Model model) {
        List<ConceptoPago> ListaConceptoPago = conceptoPagoRepository.findByIsActiveTrue();
        model.addAttribute("ListaConceptoPago", ListaConceptoPago);
        return model;
    }
    // MetodoPago
    public Model getListaMetodoPagoActivos(Model model) {
        List<MetodoPago> listaMetodoPago = metodoPagoRepository.findByIsActiveTrue();
        model.addAttribute("ListaMetodoPago", listaMetodoPago);
        return model;
    }

    // TipoProducto
    public Model getListaTipoProductoActivos(Model model) {
        List<TipoProducto> listaTipoProducto = productoRepository.findByIsActiveTrue();
        model.addAttribute("ListaTipoProducto", listaTipoProducto);
        return model;
    }

    // TipoServicio
    public Model getListaTipoServicioActivos(Model model) {
        List<TipoServicio> listaTipoServicio = tipoServicioRepository.findByIsActiveTrue();
        model.addAttribute("ListaTipoServicio", listaTipoServicio);
        return model;
    }

    // TipoTransaccion
    public Model getListaTipoTransaccionActivos(Model model) {
        List<TipoTransaccion> listaTipoTransaccion = tipoTransaccionRepository.findByIsActiveTrue();
        model.addAttribute("ListaTipoTransaccion", listaTipoTransaccion);
        return model;
    }

    // TipoVehiculo
    public Model getListaTipoVehiculoActivos(Model model) {
        List<TipoVehiculo> listaTipoVehiculo = tipoVehiculoRepository.findByIsActiveTrue();
        model.addAttribute("ListaTipoVehiculo", listaTipoVehiculo);
        return model;
    }


    // Crear ConceptoPago nuevo
    @PostMapping("/Concepto-Pago/nuevo")
    public ResponseEntity<String> crearConceptoPago(
            @RequestParam("nombre") String nombre,
            HttpServletResponse response) throws IOException {
        ConceptoPago conceptoPago = new ConceptoPago();
        conceptoPago.setNombre(nombre);
        conceptoPago.setIsActive(Boolean.TRUE);
        conceptoPagoRepository.save(conceptoPago);
        response.sendRedirect("/clasificadores");
        return ResponseEntity.ok("Clasificación ConceptoPago creada correctamente");
    }

    // Crear MetodoPago nuevo
    @PostMapping("/Metodo-Pago/nuevo")
    public ResponseEntity<String> crearMetodoPago(
            @RequestParam("nombre") String nombre,
            HttpServletResponse response) throws IOException {
        MetodoPago metodoPago = new MetodoPago();
        metodoPago.setNombre(nombre);
        metodoPago.setIsActive(Boolean.TRUE);
        metodoPagoRepository.save(metodoPago);
        response.sendRedirect("/clasificadores");
        return ResponseEntity.ok("Clasificación MetodoPago creada correctamente");
    }

    // Crear TipoProducto nuevo
    @PostMapping("/Tipo-Producto/nuevo")
    public ResponseEntity<String> crearTipoProducto(
            @RequestParam("nombre") String nombre,
            HttpServletResponse response) throws IOException {
        TipoProducto tipoProducto = new TipoProducto();
        tipoProducto.setNombre(nombre);
        tipoProducto.setIsActive(Boolean.TRUE);
        productoRepository.save(tipoProducto);
        response.sendRedirect("/clasificadores");
        return ResponseEntity.ok("Clasificación TipoProducto creada correctamente");
    }

    // Crear TipoServicio nuevo
    @PostMapping("/Tipo-Servicio/nuevo")
    public ResponseEntity<String> crearTipoServicio(
            @RequestParam("nombre") String nombre,
            HttpServletResponse response) throws IOException {
        TipoServicio tipoServicio = new TipoServicio();
        tipoServicio.setNombre(nombre);
        tipoServicio.setIsActive(Boolean.TRUE);
        tipoServicioRepository.save(tipoServicio);
        response.sendRedirect("/clasificadores");
        return ResponseEntity.ok("Clasificación TipoServicio creada correctamente");
    }

    // Crear TipoTransaccion nuevo
    @PostMapping("/Tipo-Transaccion/nuevo")
    public ResponseEntity<String> crearTipoTransaccion(
            @RequestParam("nombre") String nombre,
            HttpServletResponse response) throws IOException {
        TipoTransaccion tipoTransaccion = new TipoTransaccion();
        tipoTransaccion.setNombre(nombre);
        tipoTransaccion.setIsActive(Boolean.TRUE);
        tipoTransaccionRepository.save(tipoTransaccion);
        response.sendRedirect("/clasificadores");
        return ResponseEntity.ok("Clasificación TipoTransaccion creada correctamente");
    }

    // Crear TipoVehiculo nuevo
    @PostMapping("/Tipo-Vehiculo/nuevo")
    public ResponseEntity<String> crearTipoVehiculo(
            @RequestParam("nombre") String nombre,
            HttpServletResponse response) throws IOException {
        TipoVehiculo tipoVehiculo = new TipoVehiculo();
        tipoVehiculo.setNombre(nombre);
        tipoVehiculo.setIsActive(Boolean.TRUE);
        tipoVehiculoRepository.save(tipoVehiculo);
        response.sendRedirect("/clasificadores");
        return ResponseEntity.ok("Clasificación TipoVehiculo creada correctamente");
    }







    // Actualizar un ConceptoPago existente
    @PostMapping("/actualizar/Concepto-Pago/{id}")
    public String actualizarConceptoPago(@PathVariable Long id,
                                         @RequestParam("nombre") String nombre) {
        ConceptoPago conceptoPago = conceptoPagoRepository.findById(id).get();
        conceptoPago.setNombre(nombre);
        conceptoPago.setIsActive(Boolean.TRUE);
        conceptoPagoRepository.save(conceptoPago);
        return "redirect:/clasificadores";
    }

    // Actualizar un MetodoPago existente
    @PostMapping("/actualizar/Metodo-Pago/{id}")
    public String actualizarMetodoPago(@PathVariable Long id,
                                       @RequestParam("nombre") String nombre) {
        MetodoPago metodoPago = metodoPagoRepository.findById(id).get();
        metodoPago.setNombre(nombre);
        metodoPago.setIsActive(Boolean.TRUE);
        metodoPagoRepository.save(metodoPago);
        return "redirect:/clasificadores";
    }

    // Actualizar un TipoProducto existente
    @PostMapping("/actualizar/Tipo-Producto/{id}")
    public String actualizarTipoProducto(@PathVariable Long id,
                                         @RequestParam("nombre") String nombre) {
        TipoProducto tipoProducto = productoRepository.findById(id).get();
        tipoProducto.setNombre(nombre);
        tipoProducto.setIsActive(Boolean.TRUE);
        productoRepository.save(tipoProducto);
        return "redirect:/clasificadores";
    }

    // Actualizar un TipoServicio existente
    @PostMapping("/actualizar/Tipo-Servicio/{id}")
    public String actualizarTipoServicio(@PathVariable Long id,
                                         @RequestParam("nombre") String nombre) {
        TipoServicio tipoServicio = tipoServicioRepository.findById(id).get();
        tipoServicio.setNombre(nombre);
        tipoServicio.setIsActive(Boolean.TRUE);
        tipoServicioRepository.save(tipoServicio);
        return "redirect:/clasificadores";
    }

    // Actualizar un TipoTransaccion existente
    @PostMapping("/actualizar/Tipo-Transaccion/{id}")
    public String actualizarTipoTransaccion(@PathVariable Long id,
                                            @RequestParam("nombre") String nombre) {
        TipoTransaccion tipoTransaccion = tipoTransaccionRepository.findById(id).get();
        tipoTransaccion.setNombre(nombre);
        tipoTransaccion.setIsActive(Boolean.TRUE);
        tipoTransaccionRepository.save(tipoTransaccion);
        return "redirect:/clasificadores";
    }

    // Desactivar ConceptoPago
    @GetMapping("/desactivar/Concepto-Pago/{id}")
    public String desactivarConceptoPago(@PathVariable Long id) {
        ConceptoPago conceptoPago = conceptoPagoRepository.findById(id).get();
        conceptoPago.setIsActive(Boolean.FALSE);
        conceptoPagoRepository.save(conceptoPago);
        return "redirect:/clasificadores";
    }

    // Desactivar MetodoPago
    @GetMapping("/desactivar/Metodo-Pago/{id}")
    public String desactivarMetodoPago(@PathVariable Long id) {
        MetodoPago metodoPago = metodoPagoRepository.findById(id).get();
        metodoPago.setIsActive(Boolean.FALSE);
        metodoPagoRepository.save(metodoPago);
        return "redirect:/clasificadores";
    }

    // Desactivar TipoProducto
    @GetMapping("/desactivar/Tipo-Producto/{id}")
    public String desactivarTipoProducto(@PathVariable Long id) {
        TipoProducto tipoProducto = productoRepository.findById(id).get();
        tipoProducto.setIsActive(Boolean.FALSE);
        productoRepository.save(tipoProducto);
        return "redirect:/clasificadores";
    }

    // Desactivar TipoServicio
    @GetMapping("/desactivar/Tipo-Servicio/{id}")
    public String desactivarTipoServicio(@PathVariable Long id) {
        TipoServicio tipoServicio = tipoServicioRepository.findById(id).get();
        tipoServicio.setIsActive(Boolean.FALSE);
        tipoServicioRepository.save(tipoServicio);
        return "redirect:/clasificadores";
    }

    // Desactivar TipoTransaccion
    @GetMapping("/desactivar/Tipo-Transaccion/{id}")
    public String desactivarTipoTransaccion(@PathVariable Long id) {
        TipoTransaccion tipoTransaccion = tipoTransaccionRepository.findById(id).get();
        tipoTransaccion.setIsActive(Boolean.FALSE);
        tipoTransaccionRepository.save(tipoTransaccion);
        return "redirect:/clasificadores";
    }

    // Desactivar TipoVehiculo
    @GetMapping("/desactivar/Tipo-Vehiculo/{id}")
    public String desactivarTipoVehiculo(@PathVariable Long id) {
        TipoVehiculo tipoVehiculo = tipoVehiculoRepository.findById(id).get();
        tipoVehiculo.setIsActive(Boolean.FALSE);
        tipoVehiculoRepository.save(tipoVehiculo);
        return "redirect:/clasificadores";
    }





}
