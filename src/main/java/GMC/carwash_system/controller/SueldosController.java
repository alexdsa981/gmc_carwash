package GMC.carwash_system.controller;

import GMC.carwash_system.model.clasificadores.ConceptoPago;
import GMC.carwash_system.model.entidades.Colaborador;
import GMC.carwash_system.model.entidades.Sueldos;
import GMC.carwash_system.repository.clasificadores.ConceptoPagoRepository;
import GMC.carwash_system.repository.entidades.ColaboradorRepository;
import GMC.carwash_system.repository.entidades.SueldosRepository;
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
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/app/pagos")
public class SueldosController {

    @Autowired
    ColaboradorRepository colaboradorRepository;
    @Autowired
    ConceptoPagoRepository conceptoPagoRepository;
    @Autowired
    SueldosRepository sueldosRepository;

    public Model retornaListaConceptoPagos(Model model) {
        List<ConceptoPago> listaConceptoPago = conceptoPagoRepository.findAll();
        model.addAttribute("listaConceptoPago", listaConceptoPago);
        return model;
    }
    public Model retornaListaPagos(Model model) {
        List<Sueldos> listaPagos = sueldosRepository.findAll();
        model.addAttribute("listaPagos", listaPagos);
        return model;
    }

    @PostMapping("/crear-concepto")
    public ResponseEntity<String> crearConceptoPago(
            @RequestParam("nombre")String nombre,
            @RequestParam("dentro_sueldo")Boolean dentroSueldo,
            HttpServletResponse response
    ) throws IOException {

        ConceptoPago nuevoConceptoPago = new ConceptoPago();
        nuevoConceptoPago.setNombre(nombre);
        nuevoConceptoPago.setDentro_sueldo(dentroSueldo);
        conceptoPagoRepository.save(nuevoConceptoPago);

        response.sendRedirect("/colaboradores/lista");
        return ResponseEntity.ok("Concpeto pago creado correctamente");
    }

    @PostMapping("/editar-concepto")
    public ResponseEntity<String> editarConceptoPago(
            @PathVariable Long id,
            @RequestParam("nombre")String nombre,
            @RequestParam("dentro_sueldo")Boolean dentroSueldo,
            HttpServletResponse response
    ) throws IOException {
        // Buscar al colaborador
        Optional<ConceptoPago> optionalConcepto = conceptoPagoRepository.findById(id);
        if (optionalConcepto.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Concepto Pago no encontrado");
        }

        ConceptoPago conceptoPago = optionalConcepto.get();
        conceptoPago.setNombre(nombre);
        conceptoPago.setDentro_sueldo(dentroSueldo);
        conceptoPagoRepository.save(conceptoPago);

        response.sendRedirect("/colaboradores/lista");
        return ResponseEntity.ok("Concepto Pago editado correctamente");
    }

    //
    //Desactivacion o eliminacion concepto pago
    //


    @PostMapping("/realizar-transaccion")
    public ResponseEntity<String> realizarTransaccionDeSueldo(
            @RequestParam("idColaborador")Long idColaborador,
            @RequestParam("idConceptoPago")Long idConceptoPago,
            @RequestParam("monto") BigDecimal montoAPagar,
            @RequestParam("comentario")String comentario,
            HttpServletResponse response
    ) throws IOException {
        Sueldos nuevoPago = new Sueldos();
        nuevoPago.setColaborador(colaboradorRepository.findById(idColaborador).get());
        nuevoPago.setFecha(LocalDate.now());
        nuevoPago.setHora(LocalTime.now());
        nuevoPago.setConceptoPago(conceptoPagoRepository.findById(idConceptoPago).get());
        nuevoPago.setMonto(montoAPagar);
        nuevoPago.setComentario(comentario);

        sueldosRepository.save(nuevoPago);

        response.sendRedirect("colaboradores/lista");
        return ResponseEntity.ok("Pago realizado correctamente");
    }

}
