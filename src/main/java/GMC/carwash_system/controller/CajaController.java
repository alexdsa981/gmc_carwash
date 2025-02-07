package GMC.carwash_system.controller;

import GMC.carwash_system.model.dto.caja.*;
import GMC.carwash_system.repository.entidades.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

@Controller
@RequestMapping("/app/caja")
public class CajaController {
    @Autowired
    VentaRepository ventaRepository;


    @GetMapping("/cuadre-caja")
    public ResponseEntity<List<CuadreCajaDTO>> obtenerCuadresCaja() {
        // Obtener la información por día para cada tipo de datos
        List<Object[]> resultadosServicios = ventaRepository.obtenerServiciosPorDia();
        List<Object[]> resultadosProductos = ventaRepository.obtenerProductosPorDia();
        List<Object[]> resultadosTiposVehiculos = ventaRepository.obtenerTiposVehiculosPorDia();
        List<Object[]> resultadosMetodosPago = ventaRepository.obtenerMetodosPagoPorDia();

        // Crear un mapa para almacenar los cuadres de caja por día
        Map<String, CuadreCajaDTO> cuadresPorDia = new HashMap<>();

        // Crear un formateador de fecha
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // Formato de fecha que deseas

        // Procesar los servicios
        for (Object[] row : resultadosServicios) {
            String fecha = "";
            if (row[3] instanceof java.sql.Date) {
                fecha = sdf.format((java.sql.Date) row[3]); // Convertir la fecha a String usando el formato
            } else if (row[3] instanceof String) {
                fecha = (String) row[3]; // Si ya es String, lo dejamos como está
            }
            CuadreCajaDTO cuadre = cuadresPorDia.computeIfAbsent(fecha, k -> new CuadreCajaDTO());
            cuadre.setFecha(fecha);  // Asignar la fecha al DTO

            // Agregar los servicios a este cuadre de caja
            ServicioDTO servicio = new ServicioDTO(
                    (String) row[0],  // nombreServicio
                    ((Number) row[1]).intValue(), // totalCantidad
                    ((Number) row[2]).doubleValue() // totalRecaudado
            );
            cuadre.getServicios().add(servicio);
        }

        // Procesar los productos
        for (Object[] row : resultadosProductos) {
            String fecha = "";
            if (row[3] instanceof java.sql.Date) {
                fecha = sdf.format((java.sql.Date) row[3]); // Convertir la fecha a String usando el formato
            } else if (row[3] instanceof String) {
                fecha = (String) row[3]; // Si ya es String, lo dejamos como está
            }
            CuadreCajaDTO cuadre = cuadresPorDia.computeIfAbsent(fecha, k -> new CuadreCajaDTO());
            cuadre.setFecha(fecha);  // Asignar la fecha al DTO

            // Agregar los productos a este cuadre de caja
            ProductoDTO producto = new ProductoDTO(
                    (String) row[0],  // nombreProducto
                    ((Number) row[1]).intValue(), // totalCantidad
                    ((Number) row[2]).doubleValue() // totalRecaudado
            );
            cuadre.getProductos().add(producto);
        }





        // Procesar los metodoPago
        for (Object[] row : resultadosMetodosPago) {
            String fecha = "";
            if (row[2] instanceof java.sql.Date) {
                fecha = sdf.format((java.sql.Date) row[2]); // Convertir la fecha a String usando el formato
            } else if (row[2] instanceof String) {
                fecha = (String) row[2]; // Si ya es String, lo dejamos como está
            }
            CuadreCajaDTO cuadre = cuadresPorDia.computeIfAbsent(fecha, k -> new CuadreCajaDTO());
            cuadre.setFecha(fecha);  // Asignar la fecha al DTO

            // Agregar los servicios a este cuadre de caja
            MetodoPagoDTO metodoPago = new MetodoPagoDTO(
                    (String) row[0],  // nombreMetodoPago
                    ((Number) row[1]).doubleValue() // total
            );
            cuadre.getMetodosPago().add(metodoPago);
        }


        // Procesar los tipos de vehículos
        for (Object[] row : resultadosTiposVehiculos) {
            String fecha = "";
            if (row[2] instanceof java.sql.Date) {
                fecha = sdf.format((java.sql.Date) row[2]); // Convertir la fecha a String usando el formato
            } else if (row[2] instanceof String) {
                fecha = (String) row[2]; // Si ya es String, lo dejamos como está
            }
            CuadreCajaDTO cuadre = cuadresPorDia.computeIfAbsent(fecha, k -> new CuadreCajaDTO());
            cuadre.setFecha(fecha);  // Asignar la fecha al DTO

            // Verificar y asignar un valor por defecto en caso de que tipoVehiculo sea null o vacío
            String tipoVehiculoStr = (String) row[0];
            if (tipoVehiculoStr == null || tipoVehiculoStr.trim().isEmpty()) {
                tipoVehiculoStr = "No especificado"; // Valor por defecto si tipoVehiculo es null o vacío
            }

            // Agregar los tipos de vehículos a este cuadre de caja
            TipoVehiculoDTO tipoVehiculo = new TipoVehiculoDTO(
                    tipoVehiculoStr, // Asignar tipoVehiculo con el valor verificado
                    ((Number) row[1]).intValue() // cantidad
            );
            cuadre.getTiposVehiculos().add(tipoVehiculo);
        }


        // Convertir el mapa a una lista
        List<CuadreCajaDTO> cuadres = new ArrayList<>(cuadresPorDia.values());

        // Calcular el total de vehículos y el total de dinero por cada cuadre
        for (CuadreCajaDTO cuadre : cuadres) {
            // Calcular totalVehiculos
            int totalVehiculos = cuadre.getTiposVehiculos().stream()
                    .mapToInt(TipoVehiculoDTO::getCantidad)
                    .sum();
            cuadre.setTotalVehiculos(totalVehiculos);

            // Calcular totalDinero
            double totalDinero = cuadre.getServicios().stream()
                    .mapToDouble(ServicioDTO::getRecaudado)
                    .sum() + cuadre.getProductos().stream()
                    .mapToDouble(ProductoDTO::getRecaudado)
                    .sum();
            cuadre.setTotalDinero(totalDinero);
        }

        Collections.reverse(cuadres);
        return ResponseEntity.ok(cuadres); // Devolver la lista de cuadres
    }

//    @GetMapping("/metodos-pago")
//    public ResponseEntity<List<MetodoPagoDTO>> obtenerMetodosPago() {
//        List<Object[]> resultados = ventaRepository.obtenerMetodosPagoRaw();
//
//        List<MetodoPagoDTO> metodos = resultados.stream()
//                .map(obj -> new MetodoPagoDTO(
//                        (String) obj[0],          // nombre
//                        ((Number) obj[1]).doubleValue() // total
//                ))
//                .toList();
//
//        return ResponseEntity.ok(metodos);
//    }
//
//

//    @GetMapping("/servicios")
//    public ResponseEntity<List<ServicioDTO>> obtenerServicios() {
//        List<Object[]> resultados = ventaRepository.obtenerServiciosRaw();
//
//        List<ServicioDTO> servicios = resultados.stream()
//                .map(obj -> new ServicioDTO(
//                        (String) obj[0],          // nombreServicio
//                        ((Number) obj[1]).intValue(), // totalCantidad
//                        ((Number) obj[2]).doubleValue() // totalRecaudado
//                ))
//                .toList();
//
//        return ResponseEntity.ok(servicios);
//    }
//
//    @GetMapping("/productos")
//    public ResponseEntity<List<ProductoDTO>> obtenerProductos() {
//        List<Object[]> resultados = ventaRepository.obtenerProductosRaw();
//
//        List<ProductoDTO> productos = resultados.stream()
//                .map(obj -> new ProductoDTO(
//                        (String) obj[0],          // nombreProducto
//                        ((Number) obj[1]).intValue(), // totalCantidad
//                        ((Number) obj[2]).doubleValue() // totalRecaudado bn
//                ))
//                .toList();
//
//        return ResponseEntity.ok(productos);
//    }
//
//    @GetMapping("/tipos-vehiculos")
//    public ResponseEntity<List<TipoVehiculoDTO>> obtenerTiposVehiculos() {
//        List<Object[]> resultados = ventaRepository.obtenerTiposVehiculosRaw();
//
//        List<TipoVehiculoDTO> tiposVehiculos = resultados.stream()
//                .map(obj -> new TipoVehiculoDTO(
//                        (String) obj[0],          // tipoVehiculo
//                        ((Number) obj[1]).intValue() // cantidad
//                ))
//                .toList();
//
//        return ResponseEntity.ok(tiposVehiculos);
//    }


}
