$(document).ready(function () {
    if (!$.fn.DataTable.isDataTable("#clientTable")) {
        $('#clientTable').DataTable({
            language: { url: 'https://cdn.datatables.net/plug-ins/1.13.6/i18n/es-ES.json' },
            dom: '<"d-flex justify-content-between align-items-center"<"left-section d-flex align-items-center"l><"right-section"f>>tip',
            initComplete: function () {
                // Envolver la opción de registros en un contenedor para alinearlo con el botón
                $(".left-section").wrap('<div class="d-flex align-items-center"></div>');

                // Agregar botón a la izquierda, antes del selector de registros
                $(".left-section").prepend(`
                    <a href="/inicio" class="btn btn-outline-primary me-3">
                        <i class="bi bi-arrow-left me-2"></i> Volver a Inicio
                    </a>
                `);

                // Envolver la barra de búsqueda en un contenedor
                $(".right-section").wrap('<div class="d-flex align-items-center"></div>');

                // Agregar filtros al lado de la barra de búsqueda
                $(".right-section").after(`
                    <div class="d-flex align-items-center ms-3">
                        <label for="mesFiltro" class="me-2">Mes:</label>
                        <select id="mesFiltro" class="form-select me-2 w-auto">
                            <option value="1">Enero</option>
                            <option value="2">Febrero</option>
                            <option value="3">Marzo</option>
                            <option value="4">Abril</option>
                            <option value="5">Mayo</option>
                            <option value="6">Junio</option>
                            <option value="7">Julio</option>
                            <option value="8">Agosto</option>
                            <option value="9">Septiembre</option>
                            <option value="10">Octubre</option>
                            <option value="11">Noviembre</option>
                            <option value="12">Diciembre</option>
                        </select>

                        <label for="anioFiltro" class="me-2">Año:</label>
                        <select id="anioFiltro" class="form-select me-2 w-auto"></select>

                        <button class="btn btn-primary" onclick="filtrarClientes()">Filtrar</button>
                    </div>
                `);

                // Esperar a que el DOM se actualice
                setTimeout(() => inicializarFiltros(), 100);
            }
        });
    }
});

function inicializarFiltros() {
    let mesActual = new Date().getMonth() + 1;
    let anioActual = new Date().getFullYear();

    let mesFiltro = document.getElementById("mesFiltro");
    let anioFiltro = document.getElementById("anioFiltro");

    if (mesFiltro && anioFiltro) {
        mesFiltro.value = mesActual;

        for (let i = anioActual; i >= anioActual - 5; i--) {
            let option = document.createElement("option");
            option.value = i;
            option.text = i;
            anioFiltro.appendChild(option);
        }

        anioFiltro.value = anioActual;
        filtrarClientes(); // Cargar visitas al inicio
    } else {
        console.error("Los elementos de filtro no se encontraron en el DOM.");
    }
}

function filtrarClientes() {
    let mesFiltro = document.getElementById("mesFiltro");
    let anioFiltro = document.getElementById("anioFiltro");

    if (!mesFiltro || !anioFiltro) {
        console.error("No se encontraron los elementos de filtro.");
        return;
    }

    let mes = mesFiltro.value;
    let anio = anioFiltro.value;
    const meses = ["Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio",
                   "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"];
    let nombreMes = meses[mes - 1];

    fetch(`/app/cliente/clientes?mes=${mes}&anio=${anio}`)
        .then(response => response.json())
        .then(data => {
            let tabla = $('#clientTable').DataTable();
            let visitasMap = new Map();

            data.forEach(cliente => {
                visitasMap.set(cliente.id, cliente.visitas || 0);
            });

            tabla.rows().every(function () {
                let row = $(this.node());
                let idCliente = row.attr("data-id");
                let nuevaVisita = visitasMap.get(parseInt(idCliente)) || 0;
                let textoVisitas = `${nombreMes}: ${nuevaVisita}`;

                let celdaVisitas = row.find(".visitas");
                celdaVisitas.text(textoVisitas);

                let index = this.index();
                tabla.cell(index, 4).data(textoVisitas);
            });

            tabla.draw(false);
        })
        .catch(error => console.error("Error:", error));
}




//EDITAR VEHICULO
document.addEventListener('DOMContentLoaded', function () {
    // Seleccionamos los botones de editar y el modal
    const editVehiculoBtns = document.querySelectorAll('.edit-vehiculo');
    const saveVehiculoBtn = document.getElementById('saveVehiculoBtn');
    const editVehiculoForm = document.getElementById('editVehiculoForm');
    const editVehiculoId = document.getElementById('editVehiculoId');
    const editPlaca = document.getElementById('editPlaca');
    const editMarca = document.getElementById('editMarca');
    const editModelo = document.getElementById('editModelo');
    const editTipo = document.getElementById('editTipo');

    // Llenar el modal con los datos del vehículo al hacer clic en el botón de editar
    editVehiculoBtns.forEach(button => {
        button.addEventListener('click', function () {
            // Obtener los datos del botón
            const vehiculoId = this.getAttribute('data-id');
            const placa = this.getAttribute('data-placa');
            const marca = this.getAttribute('data-marca');
            const modelo = this.getAttribute('data-modelo');
            const tipo = this.getAttribute('data-id-tipo');

            // Rellenar el modal con estos datos
            editVehiculoId.value = vehiculoId;
            editPlaca.value = placa;
            editMarca.value = marca;
            editModelo.value = modelo;
            editTipo.value = tipo;

            // Abrir el modal
            const myModal = new bootstrap.Modal(document.getElementById('editVehiculoModal'));
            myModal.show();
        });
    });

    // Funcionalidad para guardar los cambios en el vehículo
    saveVehiculoBtn.addEventListener('click', function () {
        const vehiculoData = new FormData(editVehiculoForm);

        fetch(`/app/cliente/vehiculo/editar/${editVehiculoId.value}`, {
            method: 'POST',
            body: vehiculoData
        })
        .then(response => {
            if (response.ok) {
                Swal.fire({
                    icon: 'success',
                    title: 'Vehículo Actualizado',
                    text: 'El vehículo se actualizó correctamente.',
                    confirmButtonText: 'Aceptar'
                }).then(() => {
                    location.reload(); // Recargar la página después de la actualización
                });
            } else {
                throw new Error('Error al actualizar el vehículo');
            }
        })
        .catch(error => {
            console.error(error);
            Swal.fire({
                icon: 'error',
                title: 'Error',
                text: 'Hubo un problema al actualizar el vehículo. Inténtalo de nuevo.',
                confirmButtonText: 'Aceptar'
            });
        });
    });
});




//CREAR CLIENTE
// Añadir nuevos campos para placas
let placaCounter = 1;
document.getElementById('addPlacaButton').addEventListener('click', function () {
    placaCounter++;
    const container = document.getElementById('vehiculosContainer');
    const newPlacaInput = document.createElement('div');
    newPlacaInput.classList.add('mb-3');
    newPlacaInput.innerHTML = `
    <label for="clientPlaca${placaCounter}" class="form-label">Placa</label>
    <input type="text" class="form-control" id="clientPlaca${placaCounter}" name="placas" required autocomplete="off">
`;
    container.appendChild(newPlacaInput);
});

// Validar formulario y enviar datos al backend
document.getElementById('addClientForm').addEventListener('submit', async function (e) {
    e.preventDefault(); // Evita la recarga de la página

    const formData = new FormData(this);
    const params = new URLSearchParams();

    // Convertir FormData en parámetros URL
    formData.forEach((value, key) => {
        if (key === 'placas') {
            params.append(key, value); // Manejar múltiples valores para 'placas'
        } else {
            params.set(key, value);
        }
    });

    // Validar placas duplicadas en el frontend
    const placas = formData.getAll('placas');
    const uniquePlacas = new Set(placas);
    if (placas.length !== uniquePlacas.size) {
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'No se permiten placas duplicadas.',
        });
        return;
    }

    try {
        const response = await fetch('/app/cliente/crear', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: params.toString(),
        });

        if (response.ok) {
            Swal.fire({
                icon: 'success',
                title: 'Éxito',
                text: 'Cliente creado correctamente.',
            }).then(() => {
                window.location.href = '/clientes/lista';
            });
        } else {
            const errorText = await response.text();
            Swal.fire({
                icon: 'error',
                title: 'Error',
                text: errorText,
            });
        }
    } catch (error) {
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'No se pudo crear el cliente. Inténtalo nuevamente.',
        });
    }
});




//LOGICA QUITAR Y AGREGAR PLACAS
document.addEventListener('DOMContentLoaded', function () {
    const editButtons = document.querySelectorAll('.edit-row');

    editButtons.forEach(button => {
        button.addEventListener('click', function () {
            // Obtener los datos del cliente del botón
            const id = this.getAttribute('data-id');
            const nombre = this.getAttribute('data-nombre');
            const identificacion = this.getAttribute('data-identificacion');
            const telefono = this.getAttribute('data-telefono');
            const placas = JSON.parse(this.getAttribute('data-placas'));

            // Rellenar los campos del modal
            document.getElementById('editClientName').value = nombre;
            document.getElementById('editClientDNI').value = identificacion;
            document.getElementById('editClientPhone').value = telefono;

            // Guardar el ID en el formulario
            document.getElementById('editClientForm').setAttribute('data-id', id);

            // Limpiar el contenedor de placas
            const vehiculosContainer = document.getElementById('editVehiculosContainer');
            vehiculosContainer.innerHTML = '';

            // Crear los inputs de placas
            placas.forEach((placa, index) => {
                const inputGroupDiv = document.createElement('div');
                inputGroupDiv.classList.add('input-group', 'mb-3');
                inputGroupDiv.innerHTML = `
                    <input type="text" class="form-control placa-input" id="editClientPlaca${index + 1}" name="placas" value="${placa}">
                    <button class="btn btn-outline-danger remove-placa-button" type="button">
                        <i class="bi bi-x"></i>
                    </button>
                `;
                vehiculosContainer.appendChild(inputGroupDiv);

                // Añadir evento de eliminar para cada placa
                inputGroupDiv.querySelector('.remove-placa-button').addEventListener('click', function () {
                    inputGroupDiv.remove();
                });
            });

            // Mostrar el modal
            const editModal = new bootstrap.Modal(document.getElementById('editClientModal'));
            editModal.show();
        });
    });

    // Evento para añadir una nueva placa
    document.getElementById('editAddPlacaButton').addEventListener('click', function () {
        const vehiculosContainer = document.getElementById('editVehiculosContainer');
        const index = vehiculosContainer.children.length + 1;
        const inputGroupDiv = document.createElement('div');
        inputGroupDiv.classList.add('input-group', 'mb-3');
        inputGroupDiv.innerHTML = `
            <input type="text" class="form-control placa-input" id="editClientPlaca${index}" name="placas" placeholder="Nueva placa">
            <button class="btn btn-outline-danger remove-placa-button" type="button">
                <i class="bi bi-x"></i>
            </button>
        `;
        vehiculosContainer.appendChild(inputGroupDiv);

        // Añadir evento de eliminar
        inputGroupDiv.querySelector('.remove-placa-button').addEventListener('click', function () {
            inputGroupDiv.remove();
        });
    });
});



//EDITAR CLIENTE
document.getElementById('editClientForm').addEventListener('submit', function (e) {
    e.preventDefault();

    // Obtener el ID del cliente desde el formulario
    const id = this.getAttribute('data-id');

    // Obtener los datos del formulario
    const nombre = document.getElementById('editClientName').value;
    const identificacion = document.getElementById('editClientDNI').value;
    const telefono = document.getElementById('editClientPhone').value;

    // Recopilar las placas desde los inputs
    const placas = Array.from(document.querySelectorAll('.placa-input')).map(input => input.value);

    // Construir el cuerpo de la solicitud
    const data = {
        nombre: nombre,
        identificacion: identificacion,
        telefono: telefono,
        listaPlacas: placas
    };

    // Construir la URL del endpoint
    const endpoint = `/app/cliente/editar/${id}`;

    // Imprimir los datos en la consola
    console.log('Enviando los siguientes datos:');
    console.log('Endpoint:', endpoint);
    console.log('Datos:', data);

    // Enviar los datos al endpoint
    fetch(endpoint, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
        .then(response => response.json())
        .then(data => {
            Swal.fire(
                'Éxito',
                data.message,
                'success'
            ).then(() => {
                // Recargar la página después de que el usuario haya cerrado el SweetAlert
                location.reload();
            });
        })
        .catch(error => {
            console.error('Error:', error);
            Swal.fire(
                'Error',
                'Hubo un error al actualizar el cliente',
                'error'
            );
        });
});



//ELIMINAR CLIENTE
document.addEventListener('click', function (event) {
    // Verificar si el clic fue en un botón con la clase 'delete-row'
    if (event.target.closest('.delete-row')) {
        const button = event.target.closest('.delete-row');
        const clienteId = button.getAttribute('data-id'); // Obtener el ID del cliente desde el atributo 'data-id'
        const clienteNombre = button.getAttribute('data-nombre');

        // Confirmar antes de eliminar
        Swal.fire({
            title: `¿Estás seguro de que deseas eliminar al cliente: ${clienteNombre}?`,
            text: "¡Esta acción no se puede deshacer!",
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Sí, eliminar',
            cancelButtonText: 'Cancelar'
        }).then((result) => {
            if (result.isConfirmed) {
                // Enviar solicitud al endpoint para desactivar el cliente
                fetch(`/app/cliente/eliminar/${clienteId}`, {
                    method: 'PUT',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                })
                .then((response) => {
                    if (response.ok) {
                        Swal.fire(
                            'Eliminado',
                            'El cliente ha sido eliminado correctamente.',
                            'success'
                        );
                        // Recargar la página o eliminar la fila del cliente de la tabla
                        button.closest('tr').remove();
                    } else {
                        return response.text().then((text) => {
                            throw new Error(text);
                        });
                    }
                })
                .catch((error) => {
                    console.error('Error al eliminar el cliente:', error);
                    Swal.fire(
                        'Error',
                        'Ocurrió un error al intentar eliminar el cliente.',
                        'error'
                    );
                });
            }
        });
    }
});

