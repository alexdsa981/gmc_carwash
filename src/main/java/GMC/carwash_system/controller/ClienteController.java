package GMC.carwash_system.controller;

import GMC.carwash_system.model.clasificadores.TipoVehiculo;
import GMC.carwash_system.model.dto.ClienteVehiculosDTO;
import GMC.carwash_system.model.entidades.Cliente;
import GMC.carwash_system.model.entidades.Vehiculo;
import GMC.carwash_system.repository.clasificadores.TipoVehiculoRepository;
import GMC.carwash_system.repository.entidades.ClienteRepository;
import GMC.carwash_system.repository.entidades.VehiculoRepository;
import GMC.carwash_system.repository.entidades.VentaRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    VentaRepository ventaRepository;
    @Autowired
    private ObjectMapper objectMapper;

    public Model retornaListaClientes(Model model) {
        List<Cliente> listaClientes = clienteRepository.findByIsActiveTrue();
        List<ClienteVehiculosDTO> listaClientesDTO = new ArrayList<>();
        // Convertir las listas de placas de los clientes a JSON
        for (Cliente cliente : listaClientes) {
            ClienteVehiculosDTO clienteVehiculosDTO = new ClienteVehiculosDTO(cliente);
            listaClientesDTO.add(clienteVehiculosDTO);

             //Asegurarse de que listaPlacas esté correctamente inicializada
            List<String> listaPlacas = clienteVehiculosDTO.getListaPlacas(); // Obtener la lista de placas
            try {
                // Convertir la lista de placas a JSON
                String placasJson = objectMapper.writeValueAsString(listaPlacas);
                clienteVehiculosDTO.setListaPlacasJson(placasJson);  // Establecer el JSON en una nueva propiedad
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

        // Agregar la lista de clientes con los JSON de placas al modelo
        model.addAttribute("listaClientes", listaClientesDTO);
        return model;
    }

    @GetMapping("/clientes")
    @ResponseBody
    public List<ClienteVehiculosDTO> obtenerClientesFiltrados(@RequestParam int mes, @RequestParam int anio) {
        List<Cliente> listaClientes = clienteRepository.findByIsActiveTrue();
        List<ClienteVehiculosDTO> listaClientesDTO = new ArrayList<>();

        for (Cliente cliente : listaClientes) {
            ClienteVehiculosDTO clienteVehiculosDTO = new ClienteVehiculosDTO(cliente);


            //usar ventas
            Integer visitas = ventaRepository.contarVisitasPorClienteYMes(clienteVehiculosDTO.getId(), mes, anio);
            clienteVehiculosDTO.setVisitas(visitas != null ? visitas : 0);

            try {
                String placasJson = objectMapper.writeValueAsString(clienteVehiculosDTO.getListaPlacas());
                clienteVehiculosDTO.setListaPlacasJson(placasJson);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            listaClientesDTO.add(clienteVehiculosDTO);
        }

        return listaClientesDTO;
    }






    @PostMapping("/crear")
    public ResponseEntity<String> crearCliente(
            @RequestParam("nombre") String nombre,
            @RequestParam("telefono") String telefono,
            @RequestParam("identificacion") String identificacion,
            @RequestParam("placas") List<String> placas
    ) {
        try {
            Cliente cliente = new Cliente();
            cliente.setNombre(nombre);
            cliente.setTelefono(telefono);
            cliente.setIdentificacion(identificacion);
            cliente.setIsActive(Boolean.TRUE);

            List<Vehiculo> listaVehiculos = new ArrayList<>();
            for (String placa : placas) {
                if (vehiculoRepository.existsByPlaca(placa)) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("La placa " + placa + " ya está registrada.");
                }
                Vehiculo vehiculo = new Vehiculo();
                vehiculo.setPlaca(placa);
                vehiculo.setCliente(cliente);
                listaVehiculos.add(vehiculo);
            }

            cliente.setListaVehiculos(listaVehiculos);
            clienteRepository.save(cliente);
            vehiculoRepository.saveAll(listaVehiculos);

            return ResponseEntity.ok("Cliente creado correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al crear el cliente: " + e.getMessage());
        }
    }


    @PutMapping("/editar/{id}")
    public ResponseEntity<Map<String, String>> editarCliente(
            @PathVariable("id") Long id,
            @RequestBody ClienteVehiculosDTO clienteVehiculosDTO
    ) {
        System.out.println(id);
        System.out.println(clienteVehiculosDTO.getNombre());
        System.out.println(clienteVehiculosDTO.getIdentificacion());
        System.out.println(clienteVehiculosDTO.getTelefono());
        System.out.println(clienteVehiculosDTO.getListaVehiculos());
        System.out.println(clienteVehiculosDTO.getListaPlacas());
        System.out.println(clienteVehiculosDTO.getListaPlacasJson());

        // Buscar al cliente existente
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        // Actualizar los datos básicos del cliente
        cliente.setNombre(clienteVehiculosDTO.getNombre());
        cliente.setTelefono(clienteVehiculosDTO.getTelefono());
        cliente.setIdentificacion(clienteVehiculosDTO.getIdentificacion());

        // Obtener los vehículos actuales del cliente
        List<Vehiculo> vehiculosActuales = vehiculoRepository.findByCliente(cliente);

        // Crear un mapa de las placas existentes
        Map<String, Vehiculo> vehiculosMap = vehiculosActuales.stream()
                .collect(Collectors.toMap(Vehiculo::getPlaca, vehiculo -> vehiculo));

        // Lista de placas recibidas desde el frontend
//        List<String> placasRecibidas = new ArrayList<>();
//        List<Vehiculo> vehiculos = clienteVehiculosDTO.getListaVehiculos();
//        for (Vehiculo vehiculo : vehiculos){
//            placasRecibidas.add(vehiculo.getPlaca());
//        }

        List<String> placasRecibidas = clienteVehiculosDTO.getListaPlacas();

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
    @PutMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminarCliente(@PathVariable Long id) {
        Optional<Cliente> optionalCliente = clienteRepository.findById(id);
        if (optionalCliente.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente no encontrado");
        }
        Cliente cliente = optionalCliente.get();
        cliente.setIsActive(Boolean.FALSE);
        clienteRepository.save(cliente);
        return ResponseEntity.ok("Cliente eliminado correctamente");
    }


    @GetMapping("/buscar-cliente-por-placa/{placa}")
    public ResponseEntity<ClienteVehiculosDTO> buscarClientePorPlaca(@PathVariable String placa) {
        Optional<Vehiculo> vehiculo = vehiculoRepository.findByPlaca(placa);
        if (vehiculo.isPresent()) {
            ClienteVehiculosDTO clienteVehiculosDTO = new ClienteVehiculosDTO(vehiculo.get().getCliente());
            return ResponseEntity.ok(clienteVehiculosDTO);
        }
        return ResponseEntity.notFound().build();
    }


    @GetMapping("/buscar-vehiculo-por-placa/{placa}")
    public ResponseEntity<Vehiculo> buscarVehiculoPorPlaca(@PathVariable String placa) {
        Optional<Vehiculo> vehiculo = vehiculoRepository.findByPlaca(placa);
        if (vehiculo.isPresent()) {
            Vehiculo vehiculoEncontrado = vehiculo.get();
            return ResponseEntity.ok(vehiculoEncontrado);
        }
        return ResponseEntity.notFound().build();
    }


    @PostMapping("/vehiculo/editar/{id}")
    public ResponseEntity<String> editarVehiculo(
            @PathVariable Long id,
            @RequestParam String placa,
            @RequestParam(required = false) String marca,
            @RequestParam(required = false) String modelo,
            @RequestParam(required = false) Long tipoVehiculoId) {

        try {
            // Buscar el vehículo por su ID
            Vehiculo vehiculo = vehiculoRepository.findById(id).orElse(null);

            if (vehiculo == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vehículo no encontrado.");
            }

            // Actualizar los datos del vehículo
            vehiculo.setPlaca(placa);

            // Manejar valores nulos o vacíos para marca y modelo
            vehiculo.setMarca(marca != null && !marca.trim().isEmpty() ? marca : null);
            vehiculo.setModelo(modelo != null && !modelo.trim().isEmpty() ? modelo : null);

            // Manejar el tipo de vehículo
            if (tipoVehiculoId != null) {
                TipoVehiculo tipoVehiculo = tipoVehiculoRepository.findById(tipoVehiculoId).orElse(null);
                if (tipoVehiculo != null) {
                    vehiculo.setTipo_vehiculo(tipoVehiculo);
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tipo de vehículo inválido.");
                }
            } else {
                vehiculo.setTipo_vehiculo(null); // Permitir valores nulos para el tipo de vehículo
            }

            // Guardar los cambios
            vehiculoRepository.save(vehiculo);

            return ResponseEntity.ok("Vehículo actualizado correctamente.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar el vehículo.");
        }
    }





}
