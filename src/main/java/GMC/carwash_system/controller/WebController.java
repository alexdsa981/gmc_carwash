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
    @Autowired
    ServicioController servicioController;
    @Autowired
    EgresoControlller egresoControlller;

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
    public String paginaAtencion(Model model) {
        atencionController.retornaListaPrecioServicio(model);
        atencionController.retornaListaIngresoClientes(model);
        atencionController.retornaListaTipoItem(model);
        atencionController.retornaListaRealizados(model);


        clasificadoresController.getListaMetodoPagoActivos(model);
        clasificadoresController.getListaTipoVehiculoActivos(model);
        colaboradoresController.retornaListaColaboradoresActivos(model);
        inventarioController.retornalistaProductosActivos(model);
        servicioController.getListaTipoServicioActivos(model);
        servicioController.retornaListaTipoServicio_Especial_Basico(model);
        return "atencion/atencion";
    }

    @GetMapping("/atencion/ventas")
    public String paginaVentasRealizadas(Model model) {
        atencionController.retornaListaRealizados(model);
        return "atencion/ventas";
    }
    @GetMapping("/atencion/otros")
    public String paginaAtencionOtros(Model model) {
        clasificadoresController.getListaTipoVehiculoActivos(model);
        clasificadoresController.getListaMetodoPagoActivos(model);
        return "atencion/otros";
    }

    //PAGINAS DE COLABORADORES
    @GetMapping("/colaboradores/lista")
    public String paginaListaColaboradores(Model model) {
        colaboradoresController.retornaListaColaboradoresActivos(model);
        return "colaboradores/lista";
    }
    @GetMapping("/colaboradores/pagos")
    public String paginaListaPagosColaboradores(Model model) {
        colaboradoresController.retornaListaColaboradoresActivos(model);
        return "colaboradores/pagos";
    }

    //PAGINAS DE CLIENTES
    @GetMapping("/clientes/lista")
    public String paginaListaClientes(Model model) {
        clasificadoresController.getListaTipoVehiculoActivos(model);
        clienteController.retornaListaClientes(model);
        return "clientes/lista";
    }


    //PAGINAS DE INVENTARIO
    @GetMapping("/inventario/lista")
    public String paginaListaInventario(Model model) {
        inventarioController.retornalistaProductosActivos(model);
        inventarioController.retornalistaTipoProductosActivos(model);
        return "inventario/lista";
    }

    @GetMapping("/inventario/historial")
    public String paginaListaHistorialInventario(Model model) {
        inventarioController.retornaHistorialAlmacen(model);
        inventarioController.retornalistaProductosActivos(model);
        inventarioController.retornalistaTipoProductosActivos(model);
        return "inventario/historial";
    }
    @GetMapping("/servicios/lista")
    public String paginaListaServicios(Model model) {
        servicioController.getListaTipoServicioActivos(model);
        return "servicios/lista";
    }

    //PAGINAS DE CUADRE DE CAJA
    @GetMapping("/caja/cuadre")
    public String paginaCuadreDeCaja(Model model) {
        return "caja/cuadre";
    }



    //PAGINAS DE BALANCE
    @GetMapping("/balance")
    public String paginaBalance() { return "balance/balance"; }

    @GetMapping("/balance/ingresos")
    public String paginaIngresos() { return "balance/ingresos"; }

    @GetMapping("/balance/egresos")
    public String paginaEgresos() {
        return "balance/egresos";
    }


}

