package GMC.carwash_system.controller;


import GMC.carwash_system.model.clasificadores.MetodoPago;
import GMC.carwash_system.model.clasificadores.TipoItem;
import GMC.carwash_system.model.clasificadores.TipoServicio;
import GMC.carwash_system.model.clasificadores.TipoVehiculo;
import GMC.carwash_system.model.dto.DetalleVentaDTO;
import GMC.carwash_system.model.entidades.*;
import GMC.carwash_system.repository.clasificadores.MetodoPagoRepository;
import GMC.carwash_system.repository.clasificadores.TipoItemRepository;
import GMC.carwash_system.repository.clasificadores.TipoServicioRepository;
import GMC.carwash_system.repository.clasificadores.TipoVehiculoRepository;
import GMC.carwash_system.repository.entidades.*;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/app/atencion")
public class AtencionController {

    @Autowired
    TipoServicioRepository tipoServicioRepository;
    @Autowired
    PrecioServicioRepository precioServicioRepository;
    @Autowired
    TipoVehiculoRepository tipoVehiculoRepository;
    @Autowired
    ProductoRepository productoRepository;
    @Autowired
    ClienteRepository clienteRepository;
    @Autowired
    VehiculoRepository vehiculoRepository;
    @Autowired
    DetalleIngresoVehiculoRepository detalleIngresoVehiculoRepository;
    @Autowired
    TipoItemRepository tipoItemRepository;
    @Autowired
    DetalleVentaRepository detalleVentaRepository;
    @Autowired
    ColaboradorRepository colaboradorRepository;
    @Autowired
    MetodoPagoRepository metodoPagoRepository;
    @Autowired
    VentaRepository ventaRepository;
    @Autowired
    DetalleMetodoPagoRepository detalleMetodoPagoRepository;

    public Model retornaListaIngresoClientes(Model model) {
        List<DetalleIngresoVehiculo> listaIngresos = detalleIngresoVehiculoRepository.findByRealizadoFalseOrderByFechaDescHoraDesc();
        for (DetalleIngresoVehiculo ingreso : listaIngresos) {
            if (ingreso.getListaDetalleVentas() != null) {
                List<DetalleVentaDTO> listaDetallesDTO = new ArrayList<>();
                for (DetalleVenta detalleVenta : ingreso.getListaDetalleVentas()) {
                    DetalleVentaDTO detalleVentaDTO = new DetalleVentaDTO(detalleVenta);

                    if (detalleVenta.getTipoItem().getId() == 1) {
                        detalleVentaDTO.setNombreItem(tipoServicioRepository.findById(detalleVenta.getIdItem()).get().getNombre());
                    } else if (detalleVenta.getTipoItem().getId() == 2) {
                        detalleVentaDTO.setNombreItem(productoRepository.findById(detalleVenta.getIdItem()).get().getNombre());
                    } else {
                        detalleVentaDTO.setNombreItem("N/D");
                    }


                    listaDetallesDTO.add(detalleVentaDTO);
                }
                ingreso.setListaDetalleVentasDTO(listaDetallesDTO);
            }
        }
        model.addAttribute("listaIngresos", listaIngresos);
        return model;
    }

    public Model retornaListaRealizados(Model model) {
        List<DetalleIngresoVehiculo> listaIngresosRealizados = detalleIngresoVehiculoRepository.findByRealizadoTrueOrderByFechaDescHoraDesc();
        for (DetalleIngresoVehiculo ingreso : listaIngresosRealizados) {
            if (ingreso.getListaDetalleVentas() != null) {
                List<DetalleVentaDTO> listaDetallesDTO = new ArrayList<>();
                for (DetalleVenta detalleVenta : ingreso.getListaDetalleVentas()) {
                    DetalleVentaDTO detalleVentaDTO = new DetalleVentaDTO(detalleVenta);

                    if (detalleVenta.getTipoItem().getId() == 1) {
                        detalleVentaDTO.setNombreItem(tipoServicioRepository.findById(detalleVenta.getIdItem()).get().getNombre());
                    } else if (detalleVenta.getTipoItem().getId() == 2) {
                        detalleVentaDTO.setNombreItem(productoRepository.findById(detalleVenta.getIdItem()).get().getNombre());
                    } else {
                        detalleVentaDTO.setNombreItem("N/D");
                    }
                    listaDetallesDTO.add(detalleVentaDTO);
                }
                ingreso.setListaDetalleVentasDTO(listaDetallesDTO);
            }
        }
        model.addAttribute("listaIngresosRealizados", listaIngresosRealizados);
        return model;
    }

    public Model retornaListaPrecioServicio(Model model) {
        List<PrecioServicio> listaPrecioServicio = precioServicioRepository.findAll();
        model.addAttribute("listaPrecioServicio", listaPrecioServicio);
        return model;
    }


    public Model retornaListaTipoItem(Model model) {
        List<TipoItem> listaTipoItem = tipoItemRepository.findAll();
        model.addAttribute("listaTipoItem", listaTipoItem);
        return model;
    }


    @PostMapping("/venta")
    public ResponseEntity<?> procesarPago(@RequestBody Map<String, Object> body) {
        try {
            BigDecimal total = new BigDecimal(body.get("total").toString());
            List<Map<String, Object>> metodosPagoList = (List<Map<String, Object>>) body.get("metodosPago");
            List<Long> detalleVentaIds = ((List<?>) body.get("detalleVentaIds"))
                    .stream()
                    .map(id -> Long.parseLong(id.toString()))
                    .collect(Collectors.toList());

            // Verificar que los métodos de pago sumen el total
            BigDecimal sumaPagos = BigDecimal.ZERO;
            List<DetalleMetodoPago> detalleMetodoPagos = new ArrayList<>();
            for (Map<String, Object> metodoPagoMap : metodosPagoList) {
                Long idMetodoPago = Long.parseLong(metodoPagoMap.get("idMetodoPago").toString());
                BigDecimal monto = new BigDecimal(metodoPagoMap.get("monto").toString());
                sumaPagos = sumaPagos.add(monto);

                DetalleMetodoPago detalleMetodoPago = new DetalleMetodoPago();
                detalleMetodoPago.setMetodoPago(metodoPagoRepository.findById(idMetodoPago)
                        .orElseThrow(() -> new IllegalArgumentException("Método de pago no encontrado")));
                detalleMetodoPago.setMonto(monto);
                detalleMetodoPagos.add(detalleMetodoPago);
            }

            if (sumaPagos.compareTo(total) != 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Collections.singletonMap("message", "Los métodos de pago no suman el total."));
            }

            // Obtener cliente y marcar el ingreso como realizado
            DetalleVenta primerDetalleVenta = detalleVentaRepository.findById(detalleVentaIds.get(0)).get();
            Cliente cliente = primerDetalleVenta.getDetalleIngresoVehiculo().getCliente();
            DetalleIngresoVehiculo detalleIngresoVehiculo = primerDetalleVenta.getDetalleIngresoVehiculo();
            detalleIngresoVehiculo.setRealizado(true);
            detalleIngresoVehiculoRepository.save(detalleIngresoVehiculo);

            // Crear la venta
            Venta venta = new Venta();
            venta.setTotal(total);
            venta.setCliente(cliente);
            venta.setFecha(LocalDate.now());
            venta.setHora(LocalTime.now());
            ventaRepository.save(venta);

            // Asociar los métodos de pago a la venta
            for (DetalleMetodoPago detalleMetodoPago : detalleMetodoPagos) {
                detalleMetodoPago.setVenta(venta);
                detalleMetodoPagoRepository.save(detalleMetodoPago);
            }

            // Asociar los detalles de venta a la venta
            for (Long id : detalleVentaIds) {
                DetalleVenta detalleVenta = detalleVentaRepository.findById(id).get();
                detalleVenta.setVenta(venta);
                detalleVentaRepository.save(detalleVenta);
            }

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("message", e.getMessage()));
        }
    }


    @GetMapping("/ingreso/{idIngreso}")
    @ResponseBody
    public List<DetalleVentaDTO> obtenerServicios(@PathVariable Long idIngreso) {
        List<DetalleVenta> listaDetallesVenta = detalleVentaRepository.findByDetalleIngresoVehiculoId(idIngreso);
        List<DetalleVentaDTO> listaDetallesDTO = new ArrayList<>();

        for (DetalleVenta detalleVenta : listaDetallesVenta) {
            DetalleVentaDTO detalleVentaDTO = new DetalleVentaDTO(detalleVenta);
            if (detalleVenta.getTipoItem().getId() == 1) {
                detalleVentaDTO.setNombreItem(tipoServicioRepository.findById(detalleVenta.getIdItem()).get().getNombre());
            } else if (detalleVenta.getTipoItem().getId() == 2) {
                detalleVentaDTO.setNombreItem(productoRepository.findById(detalleVenta.getIdItem()).get().getNombre());
            } else {
                detalleVentaDTO.setNombreItem("N/D");
            }
            listaDetallesDTO.add(detalleVentaDTO);
        }
        return listaDetallesDTO;
    }




    @PostMapping("/agregar-detalle-venta")
    public String addServicio(
            @RequestParam("tipoVenta") Long idTipoVenta,
            @RequestParam("idDetalle") Long idDetalleIngreso,
            @RequestParam(value = "producto", required = false) Long IDproducto,
            @RequestParam(value = "cantidad", required = false) Integer cantidad,
            @RequestParam(value = "servicioBasico", required = false) Long IDservicioBasico,
            @RequestParam(value = "servicioEspecial", required = false) Long IDservicioEspecial,
            @RequestParam("precio") BigDecimal precioUnitarioPersonalizado) {

        DetalleIngresoVehiculo detalleIngresoVehiculo = detalleIngresoVehiculoRepository.findById(idDetalleIngreso).get();
        TipoVehiculo tipoVehiculo = detalleIngresoVehiculo.getVehiculo().getTipo_vehiculo();
        DetalleVenta detalleVenta = new DetalleVenta();
        if (idTipoVenta == 1) { //SERVICIO
            detalleVenta.setCantidad(1);
            if (IDservicioBasico != null) {
                detalleVenta.setIdItem(IDservicioBasico);
                detalleVenta.setTipoItem(tipoItemRepository.findById(1L).get());
                //si no se agrega precio, puede usarse uno personalizado
                if (precioUnitarioPersonalizado == null) {
                    detalleVenta.setPrecio_unitario(precioServicioRepository.buscarPorTipoServicioYVehiculo(IDservicioBasico, tipoVehiculo.getId()).get().getPrecio());
                    detalleVenta.setSubtotal(detalleVenta.getPrecio_unitario());
                } else {
                    detalleVenta.setPrecio_unitario(precioUnitarioPersonalizado);
                    detalleVenta.setSubtotal(precioUnitarioPersonalizado);
                }
            }
            if (IDservicioEspecial != null) {
                detalleVenta.setIdItem(IDservicioEspecial);
                detalleVenta.setTipoItem(tipoItemRepository.findById(1L).get());
                //si no se agrega precio, puede usarse uno personalizado
                if (precioUnitarioPersonalizado == null) {
                    detalleVenta.setPrecio_unitario(precioServicioRepository.buscarPorTipoServicioYVehiculo(IDservicioBasico, tipoVehiculo.getId()).get().getPrecio());
                    detalleVenta.setSubtotal(detalleVenta.getPrecio_unitario());
                } else {
                    detalleVenta.setPrecio_unitario(precioUnitarioPersonalizado);
                    detalleVenta.setSubtotal(precioUnitarioPersonalizado);
                }
            }
        }
        if (idTipoVenta == 2) { //PRODUCTO
            if (IDproducto != null) {
                detalleVenta.setIdItem(IDproducto);
                detalleVenta.setTipoItem(tipoItemRepository.findById(2L).get());
                detalleVenta.setCantidad(cantidad);
                if (precioUnitarioPersonalizado == null) {
                    detalleVenta.setPrecio_unitario(productoRepository.findById(IDproducto).get().getPrecio_venta());
                } else {
                    detalleVenta.setPrecio_unitario(precioUnitarioPersonalizado);
                }
                detalleVenta.setSubtotal(detalleVenta.getPrecio_unitario().multiply(BigDecimal.valueOf(cantidad)));
            }
        }
        detalleVenta.setDetalleIngresoVehiculo(detalleIngresoVehiculoRepository.findById(idDetalleIngreso).get());
        detalleVentaRepository.save(detalleVenta);

        return "redirect:/atencion";
    }

    @PostMapping("/editar-detalleventa/producto")
    public ResponseEntity<String> editarProducto(
            @RequestParam("idDetalle") Long idDetalle,
            @RequestParam("precio") BigDecimal precio,
            @RequestParam("producto") Long productoId,
            @RequestParam("cantidad") Integer cantidad){
        try {
            DetalleVenta detalle = detalleVentaRepository.findById(idDetalle).get();
            detalle.setIdItem(productoId);
            detalle.setCantidad(cantidad);
            detalle.setPrecio_unitario(precio);
            detalle.setSubtotal(detalle.getPrecio_unitario().multiply(BigDecimal.valueOf(cantidad)));
            detalleVentaRepository.save(detalle);
            return ResponseEntity.ok("Producto actualizado con éxito");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar el producto");
        }
    }



    @PostMapping("/editar-detalleventa/servicio")
    @ResponseBody
    public ResponseEntity<String> editarDetalleVentaServicio(
            @RequestParam Long idDetalle,
            @RequestParam Long servicio,
            @RequestParam BigDecimal precio
    ) {
        try {
            DetalleVenta detalleVenta = detalleVentaRepository.findById(idDetalle)
                    .orElseThrow(() -> new IllegalArgumentException("Detalle no encontrado"));

            detalleVenta.setIdItem(servicio);
            detalleVenta.setPrecio_unitario(precio);
            detalleVenta.setSubtotal(precio);
            detalleVentaRepository.save(detalleVenta);

            return ResponseEntity.ok("Servicio actualizado con éxito");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar el servicio");
        }
    }

    @DeleteMapping("/eliminar-detalleventa/{idDetalle}")
    @ResponseBody
    public ResponseEntity<String> eliminarDetalleVenta(@PathVariable("idDetalle") Long idDetalle) {
        try {
            // Verificar si el detalle existe
            Optional<DetalleVenta> detalleVentaOptional = detalleVentaRepository.findById(idDetalle);

            if (!detalleVentaOptional.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Detalle no encontrado.");
            }

            detalleVentaRepository.deleteById(idDetalle);

            return ResponseEntity.ok("Detalle eliminado exitosamente.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar el detalle.");
        }
    }





    @PostMapping("/agregar-ingreso/{placa}")
    public ResponseEntity<String> agregarIngreso(
            @PathVariable("placa") String placa,
            @RequestParam(value = "nombre", required = false) String nombre,
            @RequestParam(value = "telefono", required = false) String telefono,
            @RequestParam(value = "identificacion", required = false) String identificacion,
            @RequestParam(value = "marca", required = false) String marca,
            @RequestParam(value = "modelo", required = false) String modelo,
            @RequestParam(value = "idTipoVehiculo", required = false) Long idTipoVehiculo
    ) {
        // Buscar o crear el vehículo
        Vehiculo vehiculo = vehiculoRepository.findByPlaca(placa).orElse(new Vehiculo());

        // Buscar o crear el cliente
        Cliente cliente = vehiculo.getCliente() != null ? vehiculo.getCliente() : new Cliente();

        // Actualizar los datos del cliente
        cliente.setNombre(nombre);
        cliente.setTelefono(telefono);
        cliente.setIdentificacion(identificacion);
        cliente.setIsActive(Boolean.TRUE);
        cliente.setIsActive(Boolean.TRUE);
        clienteRepository.save(cliente);

        // Actualizar los datos del vehículo
        vehiculo.setPlaca(placa);
        vehiculo.setMarca(marca);
        vehiculo.setModelo(modelo);

        if (idTipoVehiculo != null) {
            tipoVehiculoRepository.findById(idTipoVehiculo)
                    .ifPresentOrElse(
                            vehiculo::setTipo_vehiculo,
                            () -> {
                                throw new IllegalArgumentException("El tipo de vehículo no existe");
                            }
                    );
        }else{
            vehiculo.setTipo_vehiculo(tipoVehiculoRepository.findById(9L).get());
        }


        vehiculo.setCliente(cliente);
        vehiculoRepository.save(vehiculo);

        // Crear y guardar el detalle de atención
        DetalleIngresoVehiculo detalleIngresoVehiculo = new DetalleIngresoVehiculo();
        detalleIngresoVehiculo.setCliente(cliente);
        detalleIngresoVehiculo.setRealizado(Boolean.FALSE);
        detalleIngresoVehiculo.setVehiculo(vehiculo);
        detalleIngresoVehiculo.setFecha(LocalDate.now());
        detalleIngresoVehiculo.setHora(LocalTime.now());
        detalleIngresoVehiculoRepository.save(detalleIngresoVehiculo);

        return ResponseEntity.ok("Ingreso agregado correctamente");
    }


    @PostMapping("/editar-detalle-servicio/{id}")
    public ResponseEntity<String> editarDetalleIngreso(
            @PathVariable("id") Long idDetalle,
            @RequestParam(value = "placa", required = false) String placa,
            @RequestParam(value = "nombre", required = false) String nombre,
            @RequestParam(value = "telefono", required = false) String telefono,
            @RequestParam(value = "identificacion", required = false) String identificacion,
            @RequestParam(value = "marca", required = false) String marca,
            @RequestParam(value = "modelo", required = false) String modelo,
            @RequestParam(value = "idTipoVehiculo", required = false) Long idTipoVehiculo
    ) {
        DetalleIngresoVehiculo detalleIngresoVehiculo = detalleIngresoVehiculoRepository.findById(idDetalle).get();
        Cliente cliente = detalleIngresoVehiculo.getCliente();
        Vehiculo vehiculo = detalleIngresoVehiculo.getVehiculo();

        cliente.setNombre(nombre);
        cliente.setTelefono(telefono);
        cliente.setIdentificacion(identificacion);

        if (idTipoVehiculo != null) {
            vehiculo.setTipo_vehiculo(tipoVehiculoRepository.findById(idTipoVehiculo).get());
        }
        vehiculo.setMarca(marca);
        vehiculo.setModelo(modelo);
        vehiculo.setPlaca(placa);

        clienteRepository.save(cliente);
        vehiculoRepository.save(vehiculo);

        return ResponseEntity.ok("Datos recibidos correctamente");
    }


    @PostMapping("/eliminar-ingreso/{id}")
    public ResponseEntity<String> eliminarIngreso(@PathVariable("id") Long id) {

        if (detalleIngresoVehiculoRepository.existsById(id)) {
            detalleIngresoVehiculoRepository.deleteById(id);
            return ResponseEntity.ok("Ingreso eliminado correctamente");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Detalle de Ingreso no encontrado");
        }
    }


    @PostMapping("/crear-tipo-servicio")
    public ResponseEntity<String> crearTipoServicio(
            @RequestParam("nombre") String nombre,
            @RequestParam("descripcion") String descripcion,
            HttpServletResponse response
    ) throws IOException {
        TipoServicio tipoServicio = new TipoServicio();
        tipoServicio.setDescripcion(descripcion);
        tipoServicio.setNombre(nombre);
        tipoServicioRepository.save(tipoServicio);
        response.sendRedirect("/atencion/lista");
        return ResponseEntity.ok("Tipo Servicio creado correctamente");
    }

    @PostMapping("/editar-tipo-servicio")
    public ResponseEntity<String> editarTipoServicio(
            @PathVariable Long id,
            @RequestParam("nombre") String nombre,
            @RequestParam("descripcion") String descripcion,
            HttpServletResponse response
    ) throws IOException {
        // Buscar al colaborador
        Optional<TipoServicio> optionalTipoServicio = tipoServicioRepository.findById(id);
        if (optionalTipoServicio.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Concepto Pago no encontrado");
        }
        TipoServicio tipoServicio = optionalTipoServicio.get();
        tipoServicio.setNombre(nombre);
        tipoServicio.setDescripcion(descripcion);
        tipoServicioRepository.save(tipoServicio);

        response.sendRedirect("/colaboradores/lista");
        return ResponseEntity.ok("Tipo Servicio editado correctamente");
    }

    //
    //Logica de Desactivación o eliminacion de tipo servicio
    //

    @PostMapping("/crear-precio-servicio")
    public ResponseEntity<String> crearPrecioServicio(
            @RequestParam("precio") BigDecimal precio,
            @RequestParam("idTipoServicio") Long idTipoServicio,
            @RequestParam("idTipoVehiculo") Long idTipoVehiculo,
            HttpServletResponse response
    ) throws IOException {

        PrecioServicio nuevoPrecioServicio = new PrecioServicio();
        nuevoPrecioServicio.setPrecio(precio);
        nuevoPrecioServicio.setTipo_servicio(tipoServicioRepository.findById(idTipoServicio).get());
        nuevoPrecioServicio.setTipo_vehiculo(tipoVehiculoRepository.findById(idTipoVehiculo).get());
        precioServicioRepository.save(nuevoPrecioServicio);
        response.sendRedirect("/atencion/lista");
        return ResponseEntity.ok("Tipo Servicio creado correctamente");
    }

    @PostMapping("/editar-precio-servicio")
    public ResponseEntity<String> editarPrecioServicio(
            @PathVariable Long id,
            @RequestParam("precio") BigDecimal precio,
            @RequestParam("idTipoServicio") Long idTipoServicio,
            @RequestParam("idTipoVehiculo") Long idTipoVehiculo,
            HttpServletResponse response
    ) throws IOException {
        // Buscar al colaborador
        Optional<PrecioServicio> optionalPrecioServicio = precioServicioRepository.findById(id);
        if (optionalPrecioServicio.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("PrecioServicio no encontrado");
        }
        PrecioServicio precioServicio = optionalPrecioServicio.get();
        precioServicio.setTipo_servicio(tipoServicioRepository.findById(idTipoServicio).get());
        precioServicio.setTipo_vehiculo(tipoVehiculoRepository.findById(idTipoVehiculo).get());
        precioServicio.setPrecio(precio);
        precioServicioRepository.save(precioServicio);

        response.sendRedirect("/atencion/lista");
        return ResponseEntity.ok("PrecioServicio editado correctamente");
    }

    //
    //Logica de Desactivación o eliminacion de precio servicio
    //
}
