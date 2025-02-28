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
    MetodoPagoRepository metodoPagoRepository;
    @Autowired
    TipoProductoRepository productoRepository;

    @Autowired
    TipoVehiculoRepository tipoVehiculoRepository;

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




    // TipoVehiculo
    public Model getListaTipoVehiculoActivos(Model model) {
        List<TipoVehiculo> listaTipoVehiculo = tipoVehiculoRepository.findByIsActiveTrue();
        model.addAttribute("ListaTipoVehiculo", listaTipoVehiculo);
        return model;
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


    // Desactivar TipoVehiculo
    @GetMapping("/desactivar/Tipo-Vehiculo/{id}")
    public String desactivarTipoVehiculo(@PathVariable Long id) {
        TipoVehiculo tipoVehiculo = tipoVehiculoRepository.findById(id).get();
        tipoVehiculo.setIsActive(Boolean.FALSE);
        tipoVehiculoRepository.save(tipoVehiculo);
        return "redirect:/clasificadores";
    }





}
