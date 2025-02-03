package GMC.carwash_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @Autowired
    ColaboradoresController colaboradoresController;
    @Autowired
    SueldosController sueldosController;
    @Autowired
    ClienteController clienteController;
    @Autowired
    AtencionController atencionController;
    @Autowired
    InventarioController inventarioController;
    @Autowired
    ClasificadoresController clasificadoresController;

    @GetMapping("/")
    public String redirectToInicio() {
        return "redirect:/inicio";
    }

    @GetMapping("/inicio")
    public String paginaInicio() {
        return "inicio";
    }

    //PAGINAS DE SERVICIOS
    @GetMapping("/atencion")
    public String paginaServicios(Model model) {
        atencionController.retornaListaTipoVehiculo(model);
        atencionController.retornaListaPrecioServicio(model);
        atencionController.retornaListaTipoServicio(model);
        inventarioController.retornalistaProductos(model);
        atencionController.retornaListaIngresoClientes(model);
        atencionController.retornaListaTipoItem(model);
        colaboradoresController.retornaListaColaboradores(model);
        atencionController.retornaListaTipoServicio_Especial_Basico(model);
        atencionController.retornaListaMetodoPago(model);
        atencionController.retornaListaRealizados(model);
        return "atencion/atencion";
    }

    //PAGINAS DE COLABORADORES
    @GetMapping("/colaboradores/lista")
    public String paginaListaColaboradores(Model model) {
        colaboradoresController.retornaListaColaboradores(model);
        return "colaboradores/lista";
    }
    @GetMapping("/colaboradores/pagos")
    public String paginaListaPagosColaboradores(Model model) {
        colaboradoresController.retornaListaColaboradores(model);
        sueldosController.retornaListaPagos(model);
        sueldosController.retornaListaConceptoPagos(model);
        return "colaboradores/pagos";
    }

    //PAGINAS DE CLIENTES
    @GetMapping("/clientes/lista")
    public String paginaListaClientes(Model model) {
        atencionController.retornaListaTipoVehiculo(model);
        clienteController.retornaListaClientes(model);
        return "clientes/lista";
    }


    //PAGINAS DE INVENTARIO
    @GetMapping("/inventario/lista")
    public String paginaListaInventario(Model model) {
        inventarioController.retornalistaProductos(model);
        inventarioController.retornalistaTipoProductos(model);
        return "inventario/lista";
    }

    @GetMapping("/inventario/historial")
    public String paginaListaHistorialInventario(Model model) {
        inventarioController.retornaHistorialAlmacen(model);
        inventarioController.retornalistaProductos(model);
        inventarioController.retornalistaTipoProductos(model);
        return "inventario/historial";
    }

    //PAGINAS DE Cuadre
    @GetMapping("/caja/cuadre")
    public String paginaCuadreDeCaja() {

        return "caja/cuadre";
    }


    //PAGINAS DE Clasificadores
    @GetMapping("/clasificadores/lista")
    public String paginaClasificadores(Model model) {
        clasificadoresController.getListaConceptoPagoActivos(model);
        clasificadoresController.getListaMetodoPagoActivos(model);
        clasificadoresController.getListaTipoProductoActivos(model);
        clasificadoresController.getListaTipoVehiculoActivos(model);
        clasificadoresController.getListaTipoTransaccionActivos(model);
        clasificadoresController.getListaTipoServicioActivos(model);
        return "clasificadores/lista";
    }


}

