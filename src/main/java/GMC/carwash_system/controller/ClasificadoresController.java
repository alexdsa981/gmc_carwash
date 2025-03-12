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


    // TipoVehiculo
    public Model getListaTipoVehiculoActivos(Model model) {
        List<TipoVehiculo> listaTipoVehiculo = tipoVehiculoRepository.findByIsActiveTrue();
        model.addAttribute("listaTipoVehiculo", listaTipoVehiculo);
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
        response.sendRedirect("/atencion/otros");
        return ResponseEntity.ok("Clasificación MetodoPago creada correctamente");
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
        response.sendRedirect("/atencion/otros");
        return ResponseEntity.ok("Clasificación TipoVehiculo creada correctamente");
    }


    // Actualizar un MetodoPago existente
    @PostMapping("/actualizar/Tipo-Vehiculo/{id}")
    public String actualizarTipoVehiculo(@PathVariable Long id,
                                       @RequestParam("nombre") String nombre) {
        TipoVehiculo tipoVehiculo = tipoVehiculoRepository.findById(id).get();
        tipoVehiculo.setNombre(nombre);
        tipoVehiculo.setIsActive(Boolean.TRUE);
        tipoVehiculoRepository.save(tipoVehiculo);
        return "redirect:/atencion/otros";
    }


    // Actualizar un MetodoPago existente
    @PostMapping("/actualizar/Metodo-Pago/{id}")
    public String actualizarMetodoPago(@PathVariable Long id,
                                       @RequestParam("nombre") String nombre) {
        MetodoPago metodoPago = metodoPagoRepository.findById(id).get();
        metodoPago.setNombre(nombre);
        metodoPago.setIsActive(Boolean.TRUE);
        metodoPagoRepository.save(metodoPago);
        return "redirect:/atencion/otros";
    }



    // Desactivar MetodoPago
    @GetMapping("/desactivar/Metodo-Pago/{id}")
    public String desactivarMetodoPago(@PathVariable Long id) {
        MetodoPago metodoPago = metodoPagoRepository.findById(id).get();
        metodoPago.setIsActive(Boolean.FALSE);
        metodoPagoRepository.save(metodoPago);
        return "redirect:/atencion/otros";
    }


    // Desactivar TipoVehiculo
    @GetMapping("/desactivar/Tipo-Vehiculo/{id}")
    public String desactivarTipoVehiculo(@PathVariable Long id) {
        TipoVehiculo tipoVehiculo = tipoVehiculoRepository.findById(id).get();
        tipoVehiculo.setIsActive(Boolean.FALSE);
        tipoVehiculoRepository.save(tipoVehiculo);
        return "redirect:/atencion/otros";
    }





}
