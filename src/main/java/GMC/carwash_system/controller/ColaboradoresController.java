package GMC.carwash_system.controller;

import GMC.carwash_system.model.entidades.Colaborador;
import GMC.carwash_system.repository.entidades.ColaboradorRepository;
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
@RequestMapping("/app/colaboradores")
public class ColaboradoresController {
    @Autowired
    ColaboradorRepository colaboradorRepository;

    public Model retornaListaColaboradores(Model model) {
        List<Colaborador> listaColaboradores = colaboradorRepository.findAll();
        model.addAttribute("listaColaboradores", listaColaboradores);
        return model;
    }


    @PostMapping("/crear")
    public ResponseEntity<String> crearColaborador(
            @RequestParam("nombre")String nombre,
            @RequestParam("telefono")String telefono,
            @RequestParam("identificacion")String identificacion,
            @RequestParam("sueldo_fijo")BigDecimal sueldo_fijo,
            @RequestParam("descripcion")String descripcion,
            HttpServletResponse response
    ) throws IOException {

        Colaborador colaborador = Colaborador.builder()
                        .nombre(nombre)
                        .telefono(telefono)
                        .identificacion(identificacion)
                        .soueldo_fijo(sueldo_fijo)
                        .descripcion(descripcion)
                        .build();
        colaboradorRepository.save(colaborador);

        response.sendRedirect("/colaboradores/lista");
        return ResponseEntity.ok("Colaborador creado correctamente");
    }

    @PostMapping("/editar-{id}")
    public ResponseEntity<String> editarColaborador(
            @PathVariable Long id,
            @RequestParam("nombre")String nombre,
            @RequestParam("telefono")String telefono,
            @RequestParam("identificacion")String identificacion,
            @RequestParam("sueldo_fijo")BigDecimal sueldo_fijo,
            @RequestParam("descripcion")String descripcion,
            HttpServletResponse response
    ) throws IOException {
        // Buscar al colaborador
        Optional<Colaborador> optionalColaborador = colaboradorRepository.findById(id);
        if (optionalColaborador.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Colaborador no encontrado");
        }

        Colaborador colaborador = optionalColaborador.get();
        colaborador.setNombre(nombre);
        colaborador.setTelefono(telefono);
        colaborador.setIdentificacion(identificacion);
        colaborador.setSoueldo_fijo(sueldo_fijo);
        colaborador.setDescripcion(descripcion);
        colaboradorRepository.save(colaborador);

        response.sendRedirect("/colaboradores/lista");
        return ResponseEntity.ok("Colaborador editado correctamente");
    }

    //falta logica de eliminacion o desactivacion

}
