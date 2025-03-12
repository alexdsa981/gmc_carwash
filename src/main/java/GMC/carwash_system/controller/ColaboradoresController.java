package GMC.carwash_system.controller;

import GMC.carwash_system.model.entidades.Colaborador;
import GMC.carwash_system.repository.entidades.ColaboradorRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/app/colaboradores")
public class ColaboradoresController {
    @Autowired
    ColaboradorRepository colaboradorRepository;

    public Model retornaListaColaboradoresActivos(Model model) {
        List<Colaborador> listaColaboradores = colaboradorRepository.findByIsActiveTrue();
        model.addAttribute("listaColaboradores", listaColaboradores);
        return model;
    }

    @GetMapping("/foto/{id}")
    public ResponseEntity<byte[]> obtenerFoto(@PathVariable Long id) {
        Colaborador colaborador = colaboradorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Colaborador no encontrado"));

        if (colaborador.getFoto() == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG) // Cambia según el tipo de imagen guardado
                .body(colaborador.getFoto());
    }


    @PostMapping("/crear")
    public ResponseEntity<String> crearColaborador(
            @RequestParam("nombre") String nombre,
            @RequestParam("telefono") String telefono,
            @RequestParam("identificacion") String identificacion,
            @RequestParam("sueldo_fijo") BigDecimal sueldo_fijo,
            @RequestParam("descripcion") String descripcion,
            @RequestParam(value = "foto", required = false) MultipartFile foto, // Recibe la imagen
            HttpServletResponse response
    ) throws IOException {
        // Crear un nuevo colaborador
        Colaborador colaborador = new Colaborador();
        colaborador.setNombre(nombre);
        colaborador.setTelefono(telefono);
        colaborador.setIdentificacion(identificacion);
        colaborador.setSueldo_fijo(sueldo_fijo);
        colaborador.setDescripcion(descripcion);
        colaborador.setIsActive(Boolean.TRUE);

        // Si se adjunta una imagen, la convertimos a byte[]
        if (foto != null && !foto.isEmpty()) {
            colaborador.setFoto(foto.getBytes());
        }

        // Guardar en la base de datos
        colaboradorRepository.save(colaborador);

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
            @RequestParam("descripcion") String descripcion,
            @RequestParam(value = "foto", required = false) MultipartFile foto // Recibe la imagen opcionalmente
    ) throws IOException {
        // Buscar colaborador existente
        Colaborador colaborador = colaboradorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Colaborador no encontrado"));

        // Actualizar datos
        colaborador.setNombre(nombre);
        colaborador.setTelefono(telefono);
        colaborador.setIdentificacion(identificacion);
        colaborador.setSueldo_fijo(sueldo_fijo);
        colaborador.setDescripcion(descripcion);

        // Si se adjunta una imagen, actualizarla
        if (foto != null && !foto.isEmpty()) {
            colaborador.setFoto(foto.getBytes());
        }

        // Guardar en la base de datos
        colaboradorRepository.save(colaborador);

        // Respuesta en JSON
        Map<String, String> response = new HashMap<>();
        response.put("message", "Colaborador actualizado correctamente");

        return ResponseEntity.ok(response);
    }



    // Eliminar colaborador
    @PostMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminarColaborador(@PathVariable Long id) {
        Optional<Colaborador> optionalColaborador = colaboradorRepository.findById(id);
        if (optionalColaborador.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Colaborador no encontrado");
        }
        Colaborador colaborador = optionalColaborador.get();
        colaborador.setIsActive(Boolean.FALSE);
        colaboradorRepository.save(colaborador);
        return ResponseEntity.ok("Colaborador eliminado correctamente");
    }




}
