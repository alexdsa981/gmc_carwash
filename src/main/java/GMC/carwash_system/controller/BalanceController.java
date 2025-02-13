package GMC.carwash_system.controller;

import GMC.carwash_system.model.dto.balance.IngresosDTO;
import GMC.carwash_system.repository.entidades.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/app/balance")
public class BalanceController {
    @Autowired
    VentaRepository ventaRepository;

    @GetMapping("/ingresos")
    public ResponseEntity<List<IngresosDTO>> obtenerIngresos(@RequestParam int year, @RequestParam int month) {

        List<Object[]> ingresosData = ventaRepository.obtenerIngresos(year, month);
        List<Object[]> serviciosData = ventaRepository.obtenerServiciosEspeciales(year, month);

        // Mapear servicios especiales por fecha
        Map<String, List<String>> serviciosPorFecha = serviciosData.stream()
                .collect(Collectors.groupingBy(
                        obj -> (String) obj[0], // Fecha
                        Collectors.mapping(obj -> (String) obj[1], Collectors.toList()) // Lista de servicios
                ));

        // Convertir ingresos en DTO
        List<IngresosDTO> ingresos = ingresosData.stream().map(obj ->
                new IngresosDTO(
                        (String) obj[0],     // fecha
                        (String) obj[1],     // subDia
                        (Integer) obj[2],    // dia (Se corrige el casteo)
                        (BigDecimal) obj[3], // subtotal
                        (Integer) obj[4],    // cantidad
                        (Integer) obj[5],    // autos
                        (Integer) obj[6],    // motosL
                        (Integer) obj[7],    // motosT
                        serviciosPorFecha.getOrDefault((String) obj[0], new ArrayList<>()) // Lista de servicios especiales
                )
        ).toList();

        return ResponseEntity.ok(ingresos);
    }

}
