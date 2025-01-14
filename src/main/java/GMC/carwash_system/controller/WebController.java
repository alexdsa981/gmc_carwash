package GMC.carwash_system.controller;

import ch.qos.logback.core.net.server.Client;
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
    ServiciosController serviciosController;

    @GetMapping("/")
    public String redirectToInicio() {
        return "redirect:/inicio";
    }

    @GetMapping("/inicio")
    public String paginaInicio() {
        return "inicio";
    }

    //PAGINAS DE SERVICIOS
    @GetMapping("/servicios")
    public String paginaServicios(Model model) {
        serviciosController.retornaListaTipoVehiculo(model);
        serviciosController.retornaListaPrecioServicio(model);
        serviciosController.retornaListaTipoServicio(model);
        serviciosController.retornaListaProducto(model);
        serviciosController.retornaListaIngresoClientes(model);
        return "servicios/servicios";
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
        clienteController.retornaListaClientes(model);
        return "clientes/lista";
    }





    @GetMapping("/clientes/ex")
    public String paginaClientes() {

        return "ex/cliente";
    }
    @GetMapping("/servicios/ex")
    public String paginaServiciosOriginal(Model model) {

        return "ex/servicios";
    }

}
