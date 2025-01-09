package GMC.carwash_system.controller;

import GMC.carwash_system.repository.clasificadores.TipoVehiculoRepository;
import GMC.carwash_system.repository.entidades.ClienteRepository;
import GMC.carwash_system.repository.entidades.VehiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/app/cliente")
public class ClienteController {
    @Autowired
    ClienteRepository clienteRepository;
    @Autowired
    VehiculoRepository vehiculoRepository;
    @Autowired
    TipoVehiculoRepository tipoVehiculoRepository;

}
