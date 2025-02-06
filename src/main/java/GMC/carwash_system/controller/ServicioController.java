package GMC.carwash_system.controller;


import GMC.carwash_system.model.clasificadores.TipoProducto;
import GMC.carwash_system.model.clasificadores.TipoServicio;
import GMC.carwash_system.model.entidades.HistorialAlmacen;
import GMC.carwash_system.model.entidades.Producto;
import GMC.carwash_system.repository.clasificadores.TipoProductoRepository;
import GMC.carwash_system.repository.clasificadores.TipoServicioRepository;
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
@RequestMapping("/app/servicio")
public class ServicioController {
    @Autowired
    TipoServicioRepository tipoServicioRepository;
    // TipoServicio
    public Model getListaTipoServicioActivos(Model model) {
        List<TipoServicio> listaTipoServicio = tipoServicioRepository.findByIsActiveTrue();
        model.addAttribute("ListaTipoServicio", listaTipoServicio);
        return model;
    }
    public Model retornaListaTipoServicio_Especial_Basico(Model model) {
        List<TipoServicio> listaTipoServicioBasico = tipoServicioRepository.findByIsEspecialFalseAndIsActiveTrue();
        model.addAttribute("listaTipoServicioBasico", listaTipoServicioBasico);
        List<TipoServicio> listaTipoServicioEspecial = tipoServicioRepository.findByIsEspecialTrueAndIsActiveTrue();
        model.addAttribute("listaTipoServicioEspecial", listaTipoServicioEspecial);
        return model;
    }


        // Crear TipoServicio nuevo
    @PostMapping("/Tipo-Servicio/nuevo")
    public ResponseEntity<String> crearTipoServicio(
            @RequestParam("nombre") String nombre,
            @RequestParam(name = "descripcion", required = false, defaultValue = "") String descripcion,
            @RequestParam(name = "isEspecial", required = false, defaultValue = "") String isEspecial,
            HttpServletResponse response) throws IOException {
        TipoServicio tipoServicio = new TipoServicio();
        tipoServicio.setNombre(nombre);
        tipoServicio.setDescripcion(descripcion);
        if (isEspecial.equals("on")){
            tipoServicio.setIsEspecial(Boolean.TRUE);
        }else{
            tipoServicio.setIsEspecial(Boolean.FALSE);
        }
        tipoServicio.setIsActive(Boolean.TRUE);
        tipoServicioRepository.save(tipoServicio);
        response.sendRedirect("/servicios/lista");
        return ResponseEntity.ok("Clasificación TipoServicio creada correctamente");
    }

    // Actualizar un TipoServicio existente
    @PostMapping("/actualizar/Tipo-Servicio/{id}")
    public String actualizarTipoServicio(
            @PathVariable Long id,
            @RequestParam("nombre") String nombre,
            @RequestParam(name = "descripcion", required = false, defaultValue = "") String descripcion,
            @RequestParam(name = "isEspecial", required = false, defaultValue = "") String isEspecial) {
        TipoServicio tipoServicio = tipoServicioRepository.findById(id).get();
        tipoServicio.setNombre(nombre);
        tipoServicio.setDescripcion(descripcion);
        if (isEspecial.equals("on")){
            tipoServicio.setIsEspecial(Boolean.TRUE);
        }else{
            tipoServicio.setIsEspecial(Boolean.FALSE);
        }
        tipoServicio.setIsActive(Boolean.TRUE);
        tipoServicioRepository.save(tipoServicio);
        return "redirect:/servicios/lista";
    }

    // Desactivar TipoServicio
    @PostMapping("/desactivar/Tipo-Servicio/{id}")
    public String desactivarTipoServicio(@PathVariable Long id) {
        TipoServicio tipoServicio = tipoServicioRepository.findById(id).get();
        tipoServicio.setIsActive(Boolean.FALSE);
        tipoServicioRepository.save(tipoServicio);
        return "redirect:/servicios/lista";
    }
}
