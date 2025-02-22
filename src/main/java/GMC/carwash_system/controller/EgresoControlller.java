package GMC.carwash_system.controller;

import GMC.carwash_system.model.clasificadores.TipoProducto;
import GMC.carwash_system.model.entidades.Egreso;
import GMC.carwash_system.model.entidades.Producto;
import GMC.carwash_system.repository.entidades.EgresoRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@RestController
@RequestMapping("/app/egresos")
public class EgresoControlller {

    @Autowired
    EgresoRepository egresoRepository;

    @PostMapping("/agregar")
    public ResponseEntity<String> agregarEgreso(
            @RequestParam("descripcion")String descripcion,
            @RequestParam("monto") BigDecimal monto,
            HttpServletResponse response
    ) throws IOException {
        Egreso egreso = new Egreso();
        egreso.setDescripcion(descripcion);
        egreso.setMonto(monto);
        egreso.setFecha(LocalDate.now());
        egreso.setHora(LocalTime.now());

        egresoRepository.save(egreso);

        response.sendRedirect("/balance/egresos");
        return ResponseEntity.ok("Egreso agregado correctamente");
    }

    @PostMapping("/editar-{id}")
    public ResponseEntity<String> editarEgreso(
            @PathVariable Long id,
            @RequestParam("descripcion")String descripcion,
            @RequestParam("monto") BigDecimal monto
    ) {
        // Buscar el producto
        Optional<Egreso> optEgreso = egresoRepository.findById(id);
        if (optEgreso.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Egreso no encontrado");
        }

        Egreso egreso = optEgreso.get();
        egreso.setDescripcion(descripcion);
        egreso.setMonto(monto);
        egresoRepository.save(egreso);

        return ResponseEntity.ok("Egreso editado correctamente");
    }

    @PostMapping("/eliminar-{id}")
    public ResponseEntity<String> eliminarEgreso(@PathVariable Long id) {
        Optional<Egreso> optEgreso = egresoRepository.findById(id);
        if (optEgreso.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Egreso no encontrado");
        }
        Egreso egreso = optEgreso.get();
        egresoRepository.delete(egreso);

        return ResponseEntity.ok("egreso eliminado correctamente");
    }


}
