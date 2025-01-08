package GMC.carwash_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @Autowired
    ColaboradoresController colaboradoresController;

    @GetMapping("/")
    public String redirectToInicio() {
        return "redirect:/inicio";
    }

    @GetMapping("/inicio")
    public String paginaInicio() {
        return "inicio";
    }

    @GetMapping("/colaboradores/lista")
    public String paginaListaColaboradores() {

        return "colaboradores/colaboradoresLista";
    }

    @GetMapping("/inventario/lista")
    public String paginaListaInventario() {

        return "inventario/inventarioLista";
    }

}
