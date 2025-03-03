package GMC.carwash_system.controller;

import GMC.carwash_system.model.dto.SueldosDTO;
import GMC.carwash_system.model.entidades.Sueldos;
import GMC.carwash_system.repository.entidades.ColaboradorRepository;
import GMC.carwash_system.repository.entidades.SueldosRepository;
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
import java.util.stream.Collectors;

@Controller
@RequestMapping("/app/sueldos")
public class SueldosController {

    @Autowired
    ColaboradorRepository colaboradorRepository;

    @Autowired
    SueldosRepository sueldosRepository;

    @GetMapping("/pagos")
    public ResponseEntity<List<SueldosDTO>> obtenerSueldos(@RequestParam int year, @RequestParam int month) {
        List<Sueldos> listaPagos = sueldosRepository.findByYearAndMonth(year, month);

        List<SueldosDTO> pagosDTO = listaPagos.stream().map(pago ->
                new SueldosDTO(
                        pago.getId(),
                        pago.getColaborador().getNombre(),
                        pago.getColaborador().getSueldo_fijo(),
                        pago.getMonto(),
                        pago.getComentario(),
                        pago.getFormattedFecha(),
                        pago.getFormattedHora(),
                        pago.getTipoOperacion()
                )
        ).collect(Collectors.toList());

        return ResponseEntity.ok(pagosDTO);
    }


    @PostMapping("/realizar-transaccion")
    public ResponseEntity<String> realizarTransaccionDeSueldo(
            @RequestParam("idColaborador") Long idColaborador,
            @RequestParam("monto") BigDecimal montoAPagar,
            @RequestParam("comentario") String comentario,
            @RequestParam("tipoOperacion") int tipoOperacion,
            HttpServletResponse response
    ) throws IOException {
        Sueldos nuevoPago = new Sueldos();
        nuevoPago.setColaborador(colaboradorRepository.findById(idColaborador).orElseThrow(
                () -> new RuntimeException("Colaborador no encontrado")));
        nuevoPago.setFecha(LocalDate.now());
        nuevoPago.setHora(LocalTime.now());
        nuevoPago.setMonto(montoAPagar);
        nuevoPago.setComentario(comentario);
        nuevoPago.setTipoOperacion(tipoOperacion);

        sueldosRepository.save(nuevoPago);

        response.sendRedirect("/colaboradores/pagos");
        return ResponseEntity.ok("Pago realizado correctamente");
    }



    // Eliminar Pago
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminarPago(@PathVariable Long id) {
        Optional<Sueldos> optionalSueldos = sueldosRepository.findById(id);
        if (optionalSueldos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pago no encontrado");
        }
        sueldosRepository.deleteById(id);
        return ResponseEntity.ok("Pago eliminado correctamente");
    }




}
