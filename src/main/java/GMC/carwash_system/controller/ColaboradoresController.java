package GMC.carwash_system.controller;

import GMC.carwash_system.model.entidades.Colaborador;
import GMC.carwash_system.repository.entidades.ColaboradorRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
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
            @RequestParam("nombre") String nombre,
            @RequestParam("telefono") String telefono,
            @RequestParam("identificacion") String identificacion,
            @RequestParam("sueldo_fijo") BigDecimal sueldo_fijo,
            @RequestParam("descripcion") String descripcion,
            HttpServletResponse response
    ) throws IOException {
        // Crear un nuevo colaborador
        Colaborador colaborador = new Colaborador();
        colaborador.setNombre(nombre);
        colaborador.setTelefono(telefono);
        colaborador.setIdentificacion(identificacion);
        colaborador.setSueldo_fijo(sueldo_fijo);
        colaborador.setDescripcion(descripcion);

        // Guardar el colaborador en la base de datos
        colaboradorRepository.save(colaborador);

        // Crear un mapa con el mensaje en formato JSON

        response.sendRedirect("/colaboradores/lista");
        return ResponseEntity.ok("Colaborador creado correctamente");
    }



    @PutMapping("/editar/{id}")
    public ResponseEntity<Map<String, String>> editarColaborador(
            @PathVariable("id") Long id,
            @RequestParam("nombre") String nombre,
            @RequestParam("telefono") String telefono,
            @RequestParam("identificacion") String identificacion,
            @RequestParam("sueldo_fijo") BigDecimal sueldo_fijo,
            @RequestParam("descripcion") String descripcion) {
        // Buscar y actualizar colaborador
        Colaborador colaborador = colaboradorRepository.findById(id).orElseThrow(() -> new RuntimeException("Colaborador no encontrado"));
        colaborador.setNombre(nombre);
        colaborador.setTelefono(telefono);
        colaborador.setIdentificacion(identificacion);
        colaborador.setSueldo_fijo(sueldo_fijo);
        colaborador.setDescripcion(descripcion);

        // Guardar el colaborador actualizado
        colaboradorRepository.save(colaborador);

        // Crear un mapa con el mensaje en formato JSON
        Map<String, String> response = new HashMap<>();
        response.put("message", "Colaborador actualizado correctamente");

        return ResponseEntity.ok(response);
    }




    // Eliminar colaborador
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminarColaborador(@PathVariable Long id) {
        Optional<Colaborador> optionalColaborador = colaboradorRepository.findById(id);
        if (optionalColaborador.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Colaborador no encontrado");
        }

        colaboradorRepository.deleteById(id);
        return ResponseEntity.ok("Colaborador eliminado correctamente");
    }




}
