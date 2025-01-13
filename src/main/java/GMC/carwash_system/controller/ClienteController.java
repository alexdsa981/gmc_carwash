package GMC.carwash_system.controller;

import GMC.carwash_system.model.dto.ClienteDTO;
import GMC.carwash_system.model.entidades.Cliente;
import GMC.carwash_system.model.entidades.Colaborador;
import GMC.carwash_system.model.entidades.Vehiculo;
import GMC.carwash_system.repository.clasificadores.TipoVehiculoRepository;
import GMC.carwash_system.repository.entidades.ClienteRepository;
import GMC.carwash_system.repository.entidades.VehiculoRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/app/cliente")
public class ClienteController {
    @Autowired
    ClienteRepository clienteRepository;
    @Autowired
    VehiculoRepository vehiculoRepository;
    @Autowired
    TipoVehiculoRepository tipoVehiculoRepository;
    @Autowired
    private ObjectMapper objectMapper;

    public Model retornaListaClientes(Model model) {
        List<Cliente> listaClientes = clienteRepository.findAll();

        // Convertir las listas de placas de los clientes a JSON
        for (Cliente cliente : listaClientes) {
            // Asegurarse de que listaPlacas esté correctamente inicializada
            List<String> listaPlacas = cliente.getListaPlacas(); // Obtener la lista de placas
            try {
                // Convertir la lista de placas a JSON
                String placasJson = objectMapper.writeValueAsString(listaPlacas);
                cliente.setListaPlacasJson(placasJson);  // Establecer el JSON en una nueva propiedad
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

        // Agregar la lista de clientes con los JSON de placas al modelo
        model.addAttribute("listaClientes", listaClientes);
        return model;
    }


    // Crear un nuevo cliente
    @PostMapping("/crear")
    public ResponseEntity<String> crearCliente(
            @RequestParam("nombre") String nombre,
            @RequestParam("telefono") String telefono,
            @RequestParam("identificacion") String identificacion,
            @RequestParam("placas") List<String> placas,
            HttpServletResponse response
    ) throws IOException {
        Cliente cliente = new Cliente();
        cliente.setNombre(nombre);
        cliente.setTelefono(telefono);
        cliente.setIdentificacion(identificacion);

        List<Vehiculo> listaVehiculos = new ArrayList<>();
        for (String placa : placas) {
            Vehiculo vehiculo = new Vehiculo();
            vehiculo.setPlaca(placa);
            vehiculo.setCliente(cliente);
            listaVehiculos.add(vehiculo);
        }

        cliente.setListaVehiculos(listaVehiculos);
        clienteRepository.save(cliente);
        vehiculoRepository.saveAll(listaVehiculos);

        response.sendRedirect("/clientes/lista");
        return ResponseEntity.ok("Cliente creado correctamente");
    }

    @PutMapping("/editar/{id}")
    public ResponseEntity<Map<String, String>> editarCliente(
            @PathVariable("id") Long id,
            @RequestBody ClienteDTO clienteDTO
    ) {
        // Buscar al cliente existente
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        // Actualizar los datos básicos del cliente
        cliente.setNombre(clienteDTO.getNombre());
        cliente.setTelefono(clienteDTO.getTelefono());
        cliente.setIdentificacion(clienteDTO.getIdentificacion());

        // Obtener los vehículos actuales del cliente
        List<Vehiculo> vehiculosActuales = vehiculoRepository.findByCliente(cliente);

        // Crear un mapa de las placas existentes
        Map<String, Vehiculo> vehiculosMap = vehiculosActuales.stream()
                .collect(Collectors.toMap(Vehiculo::getPlaca, vehiculo -> vehiculo));

        // Lista de placas recibidas desde el frontend
        List<String> placasRecibidas = clienteDTO.getPlacas();

        // Crear una lista para los vehículos finales
        List<Vehiculo> vehiculosFinales = new ArrayList<>();

        // Procesar las placas recibidas
        for (String placa : placasRecibidas) {
            if (vehiculosMap.containsKey(placa)) {
                // Si la placa ya existe, la mantenemos
                vehiculosFinales.add(vehiculosMap.get(placa));
            } else {
                // Si la placa no existe, creamos un nuevo vehículo
                Vehiculo nuevoVehiculo = new Vehiculo();
                nuevoVehiculo.setPlaca(placa);
                nuevoVehiculo.setCliente(cliente);
                vehiculosFinales.add(nuevoVehiculo);
            }
        }

        // Identificar las placas que ya no están en la lista recibida y eliminarlas
        List<Vehiculo> vehiculosAEliminar = vehiculosActuales.stream()
                .filter(v -> !placasRecibidas.contains(v.getPlaca()))
                .collect(Collectors.toList());

        if (!vehiculosAEliminar.isEmpty()) {
            vehiculoRepository.deleteAll(vehiculosAEliminar);
        }

        // Guardar los vehículos finales en el repositorio
        vehiculoRepository.saveAll(vehiculosFinales);

        // Actualizar la lista de vehículos del cliente
        cliente.setListaVehiculos(vehiculosFinales);

        // Guardar el cliente actualizado
        clienteRepository.save(cliente);

        // Respuesta exitosa
        Map<String, String> response = new HashMap<>();
        response.put("message", "Cliente actualizado correctamente");
        return ResponseEntity.ok(response);
    }



    // Eliminar un cliente
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminarCliente(@PathVariable Long id) {
        Optional<Cliente> optionalCliente = clienteRepository.findById(id);
        if (optionalCliente.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente no encontrado");
        }

        clienteRepository.deleteById(id);
        return ResponseEntity.ok("Cliente eliminado correctamente");
    }



}
