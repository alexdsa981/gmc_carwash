package GMC.carwash_system.controller;

import GMC.carwash_system.model.dto.caja.CuadreCajaDTO;
import GMC.carwash_system.model.dto.caja.ProductoDTO;
import GMC.carwash_system.model.dto.caja.ServicioDTO;
import GMC.carwash_system.model.dto.caja.TipoVehiculoDTO;
import GMC.carwash_system.repository.entidades.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/app/caja")
public class CajaController {
    @Autowired
    VentaRepository ventaRepository;





    @GetMapping("/cuadre-caja")
    public ResponseEntity<CuadreCajaDTO> obtenerCuadreCaja() {
        // Obtener la información de los servicios
        List<Object[]> resultadosServicios = ventaRepository.obtenerServiciosRaw();
        List<ServicioDTO> servicios = resultadosServicios.stream()
                .map(obj -> new ServicioDTO(
                        (String) obj[0],          // nombreServicio
                        ((Number) obj[1]).intValue(), // totalCantidad
                        ((Number) obj[2]).doubleValue() // totalRecaudado
                ))
                .toList();

        // Obtener la información de los productos
        List<Object[]> resultadosProductos = ventaRepository.obtenerProductosRaw();
        List<ProductoDTO> productos = resultadosProductos.stream()
                .map(obj -> new ProductoDTO(
                        (String) obj[0],          // nombreProducto
                        ((Number) obj[1]).intValue(), // totalCantidad
                        ((Number) obj[2]).doubleValue() // totalRecaudado
                ))
                .toList();

        // Obtener la información de los tipos de vehículos
        List<Object[]> resultadosTiposVehiculos = ventaRepository.obtenerTiposVehiculosRaw();
        List<TipoVehiculoDTO> tiposVehiculos = resultadosTiposVehiculos.stream()
                .map(obj -> new TipoVehiculoDTO(
                        (String) obj[0],          // tipoVehiculo
                        ((Number) obj[1]).intValue() // cantidad
                ))
                .toList();

        // Calcular el total de vehículos (puedes modificar esto según la lógica que necesites)
        Integer totalVehiculos = tiposVehiculos.stream()
                .mapToInt(TipoVehiculoDTO::getCantidad)
                .sum();

        // Calcular el total de dinero (sumar todos los recaudos de servicios y productos)
        Double totalDinero = servicios.stream()
                .mapToDouble(ServicioDTO::getRecaudado)
                .sum() + productos.stream()
                .mapToDouble(ProductoDTO::getRecaudado)
                .sum();

        // Crear el objeto CuadreCajaDTO con los datos obtenidos
        CuadreCajaDTO cuadreCajaDTO = new CuadreCajaDTO();
        cuadreCajaDTO.setTotalVehiculos(totalVehiculos);
        cuadreCajaDTO.setTiposVehiculos(tiposVehiculos);
        cuadreCajaDTO.setProductos(productos);
        cuadreCajaDTO.setServicios(servicios);
        cuadreCajaDTO.setTotalDinero(totalDinero);

        return ResponseEntity.ok(cuadreCajaDTO);
    }









    @GetMapping("/servicios")
    public ResponseEntity<List<ServicioDTO>> obtenerServicios() {
        List<Object[]> resultados = ventaRepository.obtenerServiciosRaw();

        List<ServicioDTO> servicios = resultados.stream()
                .map(obj -> new ServicioDTO(
                        (String) obj[0],          // nombreServicio
                        ((Number) obj[1]).intValue(), // totalCantidad
                        ((Number) obj[2]).doubleValue() // totalRecaudado
                ))
                .toList();

        return ResponseEntity.ok(servicios);
    }

    @GetMapping("/productos")
    public ResponseEntity<List<ProductoDTO>> obtenerProductos() {
        List<Object[]> resultados = ventaRepository.obtenerProductosRaw();

        List<ProductoDTO> productos = resultados.stream()
                .map(obj -> new ProductoDTO(
                        (String) obj[0],          // nombreProducto
                        ((Number) obj[1]).intValue(), // totalCantidad
                        ((Number) obj[2]).doubleValue() // totalRecaudado bn
                ))
                .toList();

        return ResponseEntity.ok(productos);
    }

    @GetMapping("/tipos-vehiculos")
    public ResponseEntity<List<TipoVehiculoDTO>> obtenerTiposVehiculos() {
        List<Object[]> resultados = ventaRepository.obtenerTiposVehiculosRaw();

        List<TipoVehiculoDTO> tiposVehiculos = resultados.stream()
                .map(obj -> new TipoVehiculoDTO(
                        (String) obj[0],          // tipoVehiculo
                        ((Number) obj[1]).intValue() // cantidad
                ))
                .toList();

        return ResponseEntity.ok(tiposVehiculos);
    }


}
