package GMC.carwash_system.controller;


import GMC.carwash_system.model.clasificadores.ConceptoPago;
import GMC.carwash_system.model.clasificadores.TipoItem;
import GMC.carwash_system.model.clasificadores.TipoServicio;
import GMC.carwash_system.model.clasificadores.TipoVehiculo;
import GMC.carwash_system.model.entidades.*;
import GMC.carwash_system.repository.clasificadores.TipoItemRepository;
import GMC.carwash_system.repository.clasificadores.TipoServicioRepository;
import GMC.carwash_system.repository.clasificadores.TipoVehiculoRepository;
import GMC.carwash_system.repository.entidades.*;
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
    DetalleAtencionRepository detalleAtencionRepository;
    @Autowired
    TipoItemRepository tipoItemRepository;
    @Autowired
    DetalleVentaRepository detalleVentaRepository;
    @Autowired
    ColaboradorRepository colaboradorRepository;

    public Model retornaListaIngresoClientes(Model model) {
        List<DetalleAtencion> listaIngresos= detalleAtencionRepository.findAll();
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

    @PostMapping("/agregar-detalle-venta")
    public String addServicio(
            @RequestParam("tipoVenta") Long idTipoVenta,
            @RequestParam("idDetalle") Long idDetalleServicio,
            @RequestParam(value = "producto", required = false) Long IDproducto,
            @RequestParam(value = "cantidad", required = false) Integer cantidad,
            @RequestParam(value = "servicioBasico", required = false) Long IDservicioBasico,
            @RequestParam(value = "servicioEspecial", required = false) Long IDservicioEspecial,
            @RequestParam("precio") BigDecimal precioUnitarioPersonalizado,
            @RequestParam("idColaborador") Long idColaborador) {

        DetalleAtencion detalleAtencion = detalleAtencionRepository.findById(idDetalleServicio).get();
        TipoVehiculo tipoVehiculo = detalleAtencion.getVehiculo().getTipo_vehiculo();
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
//
//        detalleVentaRepository.save(detalleVenta);

        // Imprimir los valores enviados
        System.out.println("-------------Recibidos-------------");
        System.out.println("Tipo de Venta: " + idTipoVenta);
        System.out.println("id detalle" + idDetalleServicio);
        System.out.println("Producto: " + IDproducto);
        System.out.println("cantidad: " + cantidad);
        System.out.println("Servicio Básico: " + IDservicioBasico);
        System.out.println("Servicio Especial: " + IDservicioEspecial);
        System.out.println("precio: " + precioUnitarioPersonalizado);
        System.out.println("Colaborador ID: " + idColaborador);
        System.out.println("-------------Detalle Venta-------------");
        System.out.println("TIPO DE VENTA:" + detalleVenta.getTipoItem().getNombre());
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
        DetalleAtencion detalleAtencion = new DetalleAtencion();
        detalleAtencion.setCliente(cliente);
        detalleAtencion.setVehiculo(vehiculo);
        detalleAtencion.setFecha(LocalDate.now());
        detalleAtencion.setHora(LocalTime.now());
        detalleAtencionRepository.save(detalleAtencion);

        return ResponseEntity.ok("Ingreso agregado correctamente");
    }



    @PostMapping("/editar-detalle-servicio/{id}")
    public ResponseEntity<String> editarDetalleAtencion(
            @PathVariable("id") Long idDetalle,
            @RequestParam(value = "placa", required = false) String placa,
            @RequestParam(value = "nombre", required = false) String nombre,
            @RequestParam(value = "telefono", required = false) String telefono,
            @RequestParam(value = "identificacion", required = false) String identificacion,
            @RequestParam(value = "marca", required = false) String marca,
            @RequestParam(value = "modelo", required = false) String modelo,
            @RequestParam(value = "idTipoVehiculo", required = false) Long idTipoVehiculo
    ) {
        DetalleAtencion detalleAtencion = detalleAtencionRepository.findById(idDetalle).get();
        Cliente cliente = detalleAtencion.getCliente();
        Vehiculo vehiculo = detalleAtencion.getVehiculo();

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
        if (detalleAtencionRepository.existsById(id)) {
            detalleAtencionRepository.deleteById(id);
            return ResponseEntity.ok("Ingreso eliminado correctamente");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Detalle de atención no encontrado");
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
