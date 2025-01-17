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
import org.springframework.beans.factory.FactoryBean;
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

@Controller
@RequestMapping("/app/servicios")
public class ServiciosController {

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

    public Model retornaListaIngresoClientes(Model model) {
        List<DetalleIngresoVehiculo> listaIngresos= detalleIngresoVehiculoRepository.findByRealizadoFalse();
            for (DetalleIngresoVehiculo ingreso : listaIngresos){
                if (ingreso.getListaDetalleVentas() != null) {
                    List<DetalleVentaDTO> listaDetallesDTO = new ArrayList<>();
                    for (DetalleVenta detalleVenta: ingreso.getListaDetalleVentas()){
                        DetalleVentaDTO detalleVentaDTO = new DetalleVentaDTO(detalleVenta);

                        if (detalleVenta.getTipoItem().getId() == 1){
                            detalleVentaDTO.setNombreItem(tipoServicioRepository.findById(detalleVenta.getIdItem()).get().getNombre());
                        } else if (detalleVenta.getTipoItem().getId() == 2){
                            detalleVentaDTO.setNombreItem(productoRepository.findById(detalleVenta.getIdItem()).get().getNombre());
                        }else{
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

    public Model retornaListaTipoServicio(Model model) {
        List<TipoServicio> listaTipoServicio = tipoServicioRepository.findAll();
        model.addAttribute("listaTipoServicio", listaTipoServicio);
        return model;
    }
    public Model retornaListaTipoServicio_Especial_Basico(Model model) {
        List<TipoServicio> listaTipoServicioBasico = tipoServicioRepository.findByIsEspecialFalse();
        model.addAttribute("listaTipoServicioBasico", listaTipoServicioBasico);
        List<TipoServicio> listaTipoServicioEspecial = tipoServicioRepository.findByIsEspecialTrue();
        model.addAttribute("listaTipoServicioEspecial", listaTipoServicioEspecial);
        return model;
    }
    public Model retornaListaPrecioServicio(Model model) {
        List<PrecioServicio> listaPrecioServicio = precioServicioRepository.findAll();
        model.addAttribute("listaPrecioServicio", listaPrecioServicio);
        return model;
    }
    public Model retornaListaTipoVehiculo(Model model) {
        List<TipoVehiculo> listaTipoVehiculo= tipoVehiculoRepository.findAll();
        model.addAttribute("listaTipoVehiculo", listaTipoVehiculo);
        return model;
    }
    public Model retornaListaProducto(Model model) {
        List<Producto> listaProducto= productoRepository.findAll();
        model.addAttribute("listaProducto", listaProducto);
        return model;
    }
    public Model retornaListaTipoItem(Model model) {
        List<TipoItem> listaTipoItem= tipoItemRepository.findAll();
        model.addAttribute("listaTipoItem", listaTipoItem);
        return model;
    }
    public Model retornaListaMetodoPago(Model model) {
        List<MetodoPago> ListaMetodoPago= metodoPagoRepository.findAll();
        model.addAttribute("ListaMetodoPago", ListaMetodoPago);
        return model;
    }


    @PostMapping("/venta")
    public ResponseEntity<?> procesarPago(@RequestBody Map<String, Object> body) {
        try {
            // Extraer los datos del JSON recibido
            BigDecimal total = new BigDecimal(body.get("total").toString());
            Long idMetodoPago = Long.parseLong(body.get("metodoPago").toString());
            String detalleVentaIdsJson = body.get("detalleVentaIds").toString();

            System.out.println("Total: " + total);
            System.out.println("Método de pago: " + idMetodoPago);
            System.out.println("Detalle Venta IDs (JSON): " + detalleVentaIdsJson);

            // Deserializar el JSON de los IDs de detalleVenta como una lista de objetos
            ObjectMapper objectMapper = new ObjectMapper();
            List<Object> detalleVentaIdsObjects = objectMapper.readValue(detalleVentaIdsJson, List.class);

            // Convertir la lista de objetos a una lista de Longs
            List<Long> detalleVentaIds = new ArrayList<>();
            for (Object idObj : detalleVentaIdsObjects) {
                try {
                    // Convertir cada objeto a Long (en caso de que sea Integer o String)
                    detalleVentaIds.add(Long.parseLong(idObj.toString()));
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Formato inválido para el ID de detalleVenta: " + idObj);
                }
            }

            // Verificar que la lista de detalleVentaIds no esté vacía
            if (detalleVentaIds.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Collections.singletonMap("message", "No se proporcionaron IDs de detalle de venta"));
            }

            DetalleVenta primerDetalleVenta = detalleVentaRepository.findById(detalleVentaIds.get(0)).get();
            Cliente cliente = primerDetalleVenta.getDetalleIngresoVehiculo().getCliente();
            DetalleIngresoVehiculo detalleIngresoVehiculo = primerDetalleVenta.getDetalleIngresoVehiculo();
            detalleIngresoVehiculo.setRealizado(Boolean.TRUE);
            detalleIngresoVehiculoRepository.save(detalleIngresoVehiculo);

            // Lógica para procesar el pago
            Venta venta = new Venta();
            venta.setTotal(total);
            venta.setMetodoPago(metodoPagoRepository.findById(idMetodoPago).orElseThrow(() -> new IllegalArgumentException("Método de pago no encontrado")));
            venta.setCliente(cliente);
            venta.setFecha(LocalDate.now());
            venta.setHora(LocalTime.now());
            ventaRepository.save(venta);

            for (Long id : detalleVentaIds){
                DetalleVenta detalleVenta = detalleVentaRepository.findById(id).get();
                detalleVenta.setVenta(venta);
                detalleVentaRepository.save(detalleVenta);
            }

            // Respuesta de éxito
            return ResponseEntity.ok().build();  // Respuesta vacía, solo éxito
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("message", e.getMessage()));
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
            @RequestParam("precio") BigDecimal precioUnitarioPersonalizado,
            @RequestParam("idColaborador") Long idColaborador) {

        DetalleIngresoVehiculo detalleIngresoVehiculo = detalleIngresoVehiculoRepository.findById(idDetalleIngreso).get();
        TipoVehiculo tipoVehiculo = detalleIngresoVehiculo.getVehiculo().getTipo_vehiculo();
        DetalleVenta detalleVenta = new DetalleVenta();
        if (idTipoVenta == 1){ //SERVICIO
            detalleVenta.setCantidad(1);
            if (IDservicioBasico != null){
                detalleVenta.setIdItem(IDservicioBasico);
                detalleVenta.setTipoItem(tipoItemRepository.findById(1L).get());
                //si no se agrega precio, puede usarse uno personalizado
                if (precioUnitarioPersonalizado == null){
                    detalleVenta.setPrecio_unitario(precioServicioRepository.buscarPorTipoServicioYVehiculo(IDservicioBasico, tipoVehiculo.getId()).get().getPrecio());
                    detalleVenta.setSubtotal(detalleVenta.getPrecio_unitario());
                }else{
                    detalleVenta.setPrecio_unitario(precioUnitarioPersonalizado);
                    detalleVenta.setSubtotal(precioUnitarioPersonalizado);
                }
            }
            if (IDservicioEspecial != null){
                detalleVenta.setIdItem(IDservicioEspecial);
                detalleVenta.setTipoItem(tipoItemRepository.findById(1L).get());
                //si no se agrega precio, puede usarse uno personalizado
                if (precioUnitarioPersonalizado == null){
                    detalleVenta.setPrecio_unitario(precioServicioRepository.buscarPorTipoServicioYVehiculo(IDservicioBasico, tipoVehiculo.getId()).get().getPrecio());
                    detalleVenta.setSubtotal(detalleVenta.getPrecio_unitario());
                }else{
                    detalleVenta.setPrecio_unitario(precioUnitarioPersonalizado);
                    detalleVenta.setSubtotal(precioUnitarioPersonalizado);
                }
            }
        }if (idTipoVenta == 2){ //PRODUCTO
            detalleVenta.setColaborador(null);
            if (IDproducto != null){
                detalleVenta.setIdItem(IDproducto);
                detalleVenta.setTipoItem(tipoItemRepository.findById(2L).get());
                detalleVenta.setCantidad(cantidad);
                if (precioUnitarioPersonalizado == null){
                    detalleVenta.setPrecio_unitario(productoRepository.findById(IDproducto).get().getPrecio_venta());
                }else{
                    detalleVenta.setPrecio_unitario(precioUnitarioPersonalizado);
                }
                detalleVenta.setSubtotal(detalleVenta.getPrecio_unitario().multiply(BigDecimal.valueOf(cantidad)));
            }
        }
        detalleVenta.setColaborador(colaboradorRepository.findById(idColaborador).get());
        detalleVenta.setDetalleIngresoVehiculo(detalleIngresoVehiculoRepository.findById(idDetalleIngreso).get());
        detalleVentaRepository.save(detalleVenta);

        // Imprimir los valores enviados
        System.out.println("-------------Recibidos-------------");
        System.out.println("Tipo de Venta: " + idTipoVenta);
        System.out.println("id detalle" + idDetalleIngreso);
        System.out.println("Producto: " + IDproducto);
        System.out.println("cantidad: " + cantidad);
        System.out.println("Servicio Básico: " + IDservicioBasico);
        System.out.println("Servicio Especial: " + IDservicioEspecial);
        System.out.println("precio: " + precioUnitarioPersonalizado);
        System.out.println("Colaborador ID: " + idColaborador);
        System.out.println("-------------Detalle Venta-------------");
        System.out.println("TIPO DE VENTA:" + detalleVenta.getTipoItem().getNombre());
        System.out.println("id detalle ingreso" + idDetalleIngreso);
        System.out.println("ID ITEM:" + detalleVenta.getIdItem());
        System.out.println("CANTIDAD: " + detalleVenta.getCantidad());
        System.out.println("PRECIO UNITARIO:" + detalleVenta.getPrecio_unitario());
        System.out.println("SUBTOTAL: " + detalleVenta.getSubtotal());
        System.out.println("ID COLABORADOR: " + detalleVenta.getColaborador().getId());
        System.out.println("VENTA: " + detalleVenta.getVenta());

        return "redirect:/servicios";
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

        vehiculo.setTipo_vehiculo(tipoVehiculoRepository.findById(idTipoVehiculo).get());
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
            @RequestParam("nombre")String nombre,
            @RequestParam("descripcion")String descripcion,
            HttpServletResponse response
    ) throws IOException {
        TipoServicio tipoServicio = new TipoServicio();
        tipoServicio.setDescripcion(descripcion);
        tipoServicio.setNombre(nombre);
        tipoServicioRepository.save(tipoServicio);
        response.sendRedirect("/servicios/lista");
        return ResponseEntity.ok("Tipo Servicio creado correctamente");
    }

    @PostMapping("/editar-tipo-servicio")
    public ResponseEntity<String> editarTipoServicio(
            @PathVariable Long id,
            @RequestParam("nombre")String nombre,
            @RequestParam("descripcion")String descripcion,
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
            @RequestParam("idTipoServicio")Long idTipoServicio,
            @RequestParam("idTipoVehiculo")Long idTipoVehiculo,
            HttpServletResponse response
    ) throws IOException {

        PrecioServicio nuevoPrecioServicio = new PrecioServicio();
        nuevoPrecioServicio.setPrecio(precio);
        nuevoPrecioServicio.setTipo_servicio(tipoServicioRepository.findById(idTipoServicio).get());
        nuevoPrecioServicio.setTipo_vehiculo(tipoVehiculoRepository.findById(idTipoVehiculo).get());
        precioServicioRepository.save(nuevoPrecioServicio);
        response.sendRedirect("/servicios/lista");
        return ResponseEntity.ok("Tipo Servicio creado correctamente");
    }

    @PostMapping("/editar-precio-servicio")
    public ResponseEntity<String> editarPrecioServicio(
            @PathVariable Long id,
            @RequestParam("precio") BigDecimal precio,
            @RequestParam("idTipoServicio")Long idTipoServicio,
            @RequestParam("idTipoVehiculo")Long idTipoVehiculo,
            HttpServletResponse response
    ) throws IOException {
        // Buscar al colaborador
        Optional<PrecioServicio> optionalPrecioServicio= precioServicioRepository.findById(id);
        if (optionalPrecioServicio.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("PrecioServicio no encontrado");
        }
        PrecioServicio precioServicio = optionalPrecioServicio.get();
        precioServicio.setTipo_servicio(tipoServicioRepository.findById(idTipoServicio).get());
        precioServicio.setTipo_vehiculo(tipoVehiculoRepository.findById(idTipoVehiculo).get());
        precioServicio.setPrecio(precio);
        precioServicioRepository.save(precioServicio);

        response.sendRedirect("/servicios/lista");
        return ResponseEntity.ok("PrecioServicio editado correctamente");
    }

    //
    //Logica de Desactivación o eliminacion de precio servicio
    //
}
