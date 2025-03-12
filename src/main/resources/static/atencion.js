$(document).ready(function () {
    var table = $('#example').DataTable({
        language: { url: 'https://cdn.datatables.net/plug-ins/1.13.6/i18n/es-ES.json' },
        dom: '<"d-flex justify-content-between align-items-center"lfB>tip',
        buttons: [],
        initComplete: function () {
            $(".dataTables_length").prepend(`
                <a href="/inicio" class="btn btn-outline-primary me-2">
                    <i class="bi bi-arrow-left me-2"></i> Volver a Inicio
                </a>
                <a href="/atencion/ventas" class="btn btn-outline-success me-2">
                    <i class="bi bi-cash"></i> Ir a Ventas
                </a>
                <a href="/atencion/otros" class="btn btn-outline-secondary me-3">
                    <i class="bi bi-gear"></i> Otros
                </a>
            `);

            $(".dataTables_filter").append(`
                <button class="btn btn-success ms-3" data-bs-toggle="modal" data-bs-target="#addIngresoModal">
                    Añadir Ingreso
                </button>
            `);
        }
    });
});



//EDITAR PRODUCTO
document.addEventListener('DOMContentLoaded', function () {
    const editProductButtons = document.querySelectorAll('.edit-product-btn');
    const saveProductoBtn = document.getElementById('saveProductoBtn');
    const editProductoForm = document.getElementById('editProductoForm');

    // Llenar los campos del modal al abrirlo
    editProductButtons.forEach(button => {
        button.addEventListener('click', function () {
            document.getElementById('editDetalleId').value = this.dataset.id;
            document.getElementById('editProducto').value = this.dataset.producto;
            document.getElementById('editPrecio').value = this.dataset.precio;
            document.getElementById('editCantidad').value = this.dataset.cantidad;
        });
    });

    // Manejar el envío del formulario
    saveProductoBtn.addEventListener('click', function () {
        const formData = new FormData(editProductoForm);

        fetch('/app/atencion/editar-detalleventa/producto', {
            method: 'POST',
            body: formData
        })
            .then(response => {
                if (response.ok) {
                    // Mostrar confirmación con SweetAlert
                    Swal.fire({
                        icon: 'success',
                        title: 'Éxito',
                        text: 'El producto se actualizó correctamente.',
                        confirmButtonText: 'Aceptar'
                    }).then(() => {
                        location.reload(); // Recargar la página después de cerrar el aviso
                    });
                } else {
                    throw new Error('Error al guardar el producto');
                }
            })
            .catch(error => {
                console.error(error);
                // Mostrar error con SweetAlert
                Swal.fire({
                    icon: 'error',
                    title: 'Error',
                    text: 'Hubo un problema al actualizar el producto. Inténtalo de nuevo.',
                    confirmButtonText: 'Aceptar'
                });
            });
    });
});



//EDITAR SERVICIO
document.addEventListener('DOMContentLoaded', function () {
    const editServicioButtons = document.querySelectorAll('.edit-servicio-btn');
    const saveServicioBtn = document.getElementById('saveServicioBtn');
    const editServicioForm = document.getElementById('editServicioForm');

    // Llenar los campos al abrir el modal
    editServicioButtons.forEach(button => {
        button.addEventListener('click', function () {
            document.getElementById('editDetalleIdS').value = this.dataset.id;
            document.getElementById('editServicio').value = this.dataset.servicio;
            document.getElementById('editPrecioS').value = this.dataset.precio;
        });
    });

    // Manejar el envío del formulario
    saveServicioBtn.addEventListener('click', function () {
        const formData = new FormData(editServicioForm);

        fetch('/app/atencion/editar-detalleventa/servicio', {
            method: 'POST',
            body: formData
        })
            .then(response => {
                if (response.ok) {
                    // Confirmación con SweetAlert
                    Swal.fire({
                        icon: 'success',
                        title: 'Éxito',
                        text: 'El servicio se actualizó correctamente.',
                        confirmButtonText: 'Aceptar'
                    }).then(() => {
                        location.reload(); // Recargar página
                    });
                } else {
                    throw new Error('Error al guardar el servicio');
                }
            })
            .catch(error => {
                console.error(error);
                // Notificación de error
                Swal.fire({
                    icon: 'error',
                    title: 'Error',
                    text: 'Hubo un problema al actualizar el servicio. Inténtalo de nuevo.',
                    confirmButtonText: 'Aceptar'
                });
            });
    });
});


//ELIMINAR DETALLE VENTA (PRODUCTO/SERVICIO)
document.addEventListener('DOMContentLoaded', function () {
    const deleteProductoBtn = document.getElementById('deleteProductoBtn'); // Botón Eliminar Producto
    const deleteServicioBtn = document.getElementById('deleteServicioBtn'); // Botón Eliminar Servicio

    // Funcionalidad para eliminar Producto
    deleteProductoBtn.addEventListener('click', function () {
        const detalleId = document.getElementById('editDetalleId').value; // Obtener el ID del detalleVenta

        Swal.fire({
            icon: 'warning',
            title: '¿Estás seguro?',
            text: 'No podrás revertir esta acción.',
            showCancelButton: true,
            confirmButtonText: 'Eliminar',
            cancelButtonText: 'Cancelar'
        }).then((result) => {
            if (result.isConfirmed) {
                fetch(`/app/atencion/eliminar-detalleventa/${detalleId}`, {
                    method: 'DELETE',
                })
                .then(response => {
                    if (response.ok) {
                        Swal.fire({
                            icon: 'success',
                            title: 'Producto Eliminado',
                            text: 'El producto se eliminó correctamente.',
                            confirmButtonText: 'Aceptar'
                        }).then(() => {
                            location.reload(); // Recargar la página después de eliminar
                        });
                    } else {
                        throw new Error('Error al eliminar el producto');
                    }
                })
                .catch(error => {
                    console.error(error);
                    Swal.fire({
                        icon: 'error',
                        title: 'Error',
                        text: 'Hubo un problema al eliminar el producto. Inténtalo de nuevo.',
                        confirmButtonText: 'Aceptar'
                    });
                });
            }
        });
    });

    // Funcionalidad para eliminar Servicio
    deleteServicioBtn.addEventListener('click', function () {
        const detalleId = document.getElementById('editDetalleId').value; // Obtener el ID del detalleVenta

        Swal.fire({
            icon: 'warning',
            title: '¿Estás seguro?',
            text: 'No podrás revertir esta acción.',
            showCancelButton: true,
            confirmButtonText: 'Eliminar',
            cancelButtonText: 'Cancelar'
        }).then((result) => {
            if (result.isConfirmed) {
                fetch(`/app/atencion/eliminar-detalleventa/${detalleId}`, {
                    method: 'DELETE',
                })
                .then(response => {
                    if (response.ok) {
                        Swal.fire({
                            icon: 'success',
                            title: 'Servicio Eliminado',
                            text: 'El servicio se eliminó correctamente.',
                            confirmButtonText: 'Aceptar'
                        }).then(() => {
                            location.reload(); // Recargar la página después de eliminar
                        });
                    } else {
                        throw new Error('Error al eliminar el servicio');
                    }
                })
                .catch(error => {
                    console.error(error);
                    Swal.fire({
                        icon: 'error',
                        title: 'Error',
                        text: 'Hubo un problema al eliminar el servicio. Inténtalo de nuevo.',
                        confirmButtonText: 'Aceptar'
                    });
                });
            }
        });
    });

    // Asegúrate de asignar el ID del detalle al modal de servicio cuando se haga clic en el botón de editar
    const editServicioButtons = document.querySelectorAll('.detalle-btn');
    editServicioButtons.forEach(button => {
        button.addEventListener('click', function () {
            const detalleId = this.dataset.id; // Obtener el ID de detalle desde el botón
            document.getElementById('editDetalleId').value = detalleId; // Asignar al campo oculto
        });
    });
});


//EDITAR INGRESO
//llenar el modal con datos
document.addEventListener("DOMContentLoaded", function () {
    const editButtons = document.querySelectorAll(".edit-btn");

    editButtons.forEach(button => {
        button.addEventListener("click", function () {
            // Asignar el ID al modal
            const id = this.getAttribute("data-id");
            const modal = document.getElementById("editIngresoModal");
            modal.setAttribute("data-id", id);

            // Llenar los campos del modal con los datos del botón
            document.getElementById("editPlaca").value = this.getAttribute("data-placa");
            document.getElementById("editCliente").value = this.getAttribute("data-cliente");
            document.getElementById("editTelefono").value = this.getAttribute("data-telefono");
            document.getElementById("editIdentificacion").value = this.getAttribute("data-identificacion");
            document.getElementById("editMarca").value = this.getAttribute("data-marca");
            document.getElementById("editModelo").value = this.getAttribute("data-modelo");
            document.getElementById("editTipoVehiculo").value = this.getAttribute("data-tipo-vehiculo");
        });
    });
});

//envio del boton editar:
document.addEventListener("DOMContentLoaded", function () {
    // Referencia al botón Guardar del modal de edición
    const guardarBtn = document.getElementById("guardarEdicion");

    // Evento de click en el botón Guardar
    guardarBtn.addEventListener("click", async function () {
        // Obtener los valores del formulario de edición
        const id = document.getElementById("editIngresoModal").getAttribute("data-id");
        const placa = document.getElementById("editPlaca").value.trim();
        const nombre = document.getElementById("editCliente").value.trim() || "";
        const telefono = document.getElementById("editTelefono").value.trim() || "";
        const identificacion = document.getElementById("editIdentificacion").value.trim() || "";
        const marca = document.getElementById("editMarca").value.trim() || "";
        const modelo = document.getElementById("editModelo").value.trim() || "";
        const idTipoVehiculo = document.getElementById("editTipoVehiculo").value || null;

        // Validar que la placa esté presente
        if (!placa) {
            Swal.fire({
                icon: 'error',
                title: 'La placa es obligatoria.',
                confirmButtonText: 'Aceptar'
            });
            return;
        }

        try {
            // Enviar los datos al backend mediante fetch
            const response = await fetch(`/app/atencion/editar-detalle-servicio/${id}`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/x-www-form-urlencoded"
                },
                body: new URLSearchParams({
                    placa,
                    nombre,
                    telefono,
                    identificacion,
                    marca,
                    modelo,
                    idTipoVehiculo: idTipoVehiculo || ""
                })
            });

            if (response.ok) {
                Swal.fire({
                    icon: 'success',
                    title: 'Detalle de servicio editado correctamente.',
                    confirmButtonText: 'Aceptar'
                }).then(() => {
                    location.reload(); // Recargar la página
                });
            } else {
                const errorText = await response.text();
                Swal.fire({
                    icon: 'error',
                    title: 'Error al editar el detalle del servicio',
                    text: errorText,
                    confirmButtonText: 'Aceptar'
                });
            }
        } catch (error) {
            Swal.fire({
                icon: 'error',
                title: 'Ocurrió un error',
                text: error.message,
                confirmButtonText: 'Aceptar'
            });
        }
    });
});



//ELIMINAR INGRESO
function eliminarDesdeBoton(boton) {
    const id = boton.getAttribute("data-id");
    eliminarIngreso(id);
}

function eliminarIngreso(id) {
    Swal.fire({
        title: '¿Está seguro de que desea eliminar este ingreso?',
        text: "Esta acción no se puede deshacer.",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: 'Sí, eliminar',
        cancelButtonText: 'Cancelar'
    }).then((result) => {
        if (result.isConfirmed) {
            fetch(`/app/atencion/eliminar-ingreso/${id}`, {
                method: 'POST'
            })
                .then(response => {
                    if (response.ok) {
                        Swal.fire({
                            icon: 'success',
                            title: 'Ingreso eliminado correctamente.',
                            confirmButtonText: 'Aceptar'
                        }).then(() => {
                            location.reload(); // Recargar la página
                        });
                    } else {
                        Swal.fire({
                            icon: 'error',
                            title: 'Error',
                            text: 'No se pudo eliminar el ingreso.',
                            confirmButtonText: 'Aceptar'
                        });
                    }
                })
                .catch(error => {
                    console.error("Error:", error);
                    Swal.fire({
                        icon: 'error',
                        title: 'Ocurrió un error',
                        text: 'No se pudo eliminar el ingreso.',
                        confirmButtonText: 'Aceptar'
                    });
                });
        }
    });
}


//CREAR NUEVO INGRESO
document.addEventListener("DOMContentLoaded", function () {
    // Referencia al botón Guardar
    const guardarBtn = document.getElementById("guardarIngreso");

    // Evento de click en el botón Guardar
    guardarBtn.addEventListener("click", async function () {
        // Obtener los valores de los campos (permitiendo valores vacíos)
        const placa = document.getElementById("placa").value.trim();
        const nombre = document.getElementById("cliente").value.trim() || "";
        const telefono = document.getElementById("telefono").value.trim() || "";
        const identificacion = document.getElementById("identificacion").value.trim() || "";
        const marca = document.getElementById("marca").value.trim() || "";
        const modelo = document.getElementById("modelo").value.trim() || "";
        const idTipoVehiculo = document.getElementById("tipoVehiculo").value || null;

        // Validar que la placa esté presente
        if (!placa) {
            Swal.fire({
                icon: 'error',
                title: 'La placa es obligatoria.',
                confirmButtonText: 'Aceptar'
            });
            return;
        }

        try {
            // Enviar los datos al backend mediante fetch
            const response = await fetch(`/app/atencion/agregar-ingreso/${placa}`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/x-www-form-urlencoded"
                },
                body: new URLSearchParams({
                    nombre,
                    telefono,
                    identificacion,
                    marca,
                    modelo,
                    idTipoVehiculo: idTipoVehiculo || ""
                })
            });

            if (response.ok) {
                Swal.fire({
                    icon: 'success',
                    title: 'Ingreso agregado correctamente.',
                    confirmButtonText: 'Aceptar'
                }).then(() => {
                    location.reload(); // Recargar la página
                });
            } else {
                const errorText = await response.text();
                Swal.fire({
                    icon: 'error',
                    title: 'Error al agregar el ingreso',
                    text: errorText,
                    confirmButtonText: 'Aceptar'
                });
            }
        } catch (error) {
            Swal.fire({
                icon: 'error',
                title: 'Ocurrió un error',
                text: error.message,
                confirmButtonText: 'Aceptar'
            });
        }
    });
});


//LOGICA BUSQUEDA DE PLACAS
document.addEventListener("DOMContentLoaded", function () {
    const buscarBtn = document.querySelector("#placa + button");
    deshabilitarCamposInicialmente();

    buscarBtn.addEventListener("click", async function () {
        const placaInput = document.getElementById("placa");
        const placa = placaInput.value.trim();

        if (!placa) {
            Swal.fire({
                icon: 'error',
                title: 'Por favor, ingrese una placa.',
                confirmButtonText: 'Aceptar'
            });
            return;
        }

        try {
            const clienteResponse = await fetch(`/app/cliente/buscar-cliente-por-placa/${placa}`);
            if (!clienteResponse.ok) throw new Error("Cliente no encontrado");

            const cliente = await clienteResponse.json();
            document.getElementById("cliente").value = cliente.nombre;
            document.getElementById("telefono").value = cliente.telefono;
            document.getElementById("identificacion").value = cliente.identificacion;

            const vehiculoResponse = await fetch(`/app/cliente/buscar-vehiculo-por-placa/${placa}`);
            if (!vehiculoResponse.ok) throw new Error("Vehículo no encontrado");

            const vehiculo = await vehiculoResponse.json();
            document.getElementById("marca").value = vehiculo.marca;
            document.getElementById("modelo").value = vehiculo.modelo;
            const tipoVehiculoId = vehiculo.tipo_vehiculo.id;
            const select = document.getElementById("tipoVehiculo");
            for (let option of select.options) {
                if (option.value == tipoVehiculoId) {
                    option.selected = true;
                    break;
                }
            }
            Swal.fire({
            icon: 'success',
            title: 'Placa encontrada.',
            text: 'Los datos se han cargado correctamente.',
            confirmButtonText: 'Aceptar'
        });

            deshabilitarCampos();
            habilitarBotones();


        } catch (error) {
                Swal.fire({
                    icon: 'info',
                    title: 'Placa no encontrada.',
                    text: 'Puede ingresar nuevos datos.',
                    confirmButtonText: 'Aceptar'
                });
                habilitarCampos();
            }
    });

    function deshabilitarCamposInicialmente() {
        const campos = ["cliente", "telefono", "identificacion", "marca", "modelo", "tipoVehiculo"];
        campos.forEach(campo => {
            const input = document.getElementById(campo);
            input.readOnly = true;
            input.classList.add("disabled-input");
        });
        deshabilitarBotones();
    }

    function deshabilitarCampos() {
        const campos = ["placa", "cliente", "telefono", "identificacion", "marca", "modelo", "tipoVehiculo"];
        campos.forEach(campo => {
            const input = document.getElementById(campo);
            input.readOnly = true;
            input.classList.add("disabled-input");
        });
    }

    function habilitarCampos() {
        const campos = ["placa", "cliente", "telefono", "identificacion", "marca", "modelo", "tipoVehiculo"];
        campos.forEach(campo => {
            const input = document.getElementById(campo);
            input.readOnly = false;
            input.classList.remove("disabled-input");
        });
        habilitarBotones();
    }

    function habilitarBotones() {
        document.querySelector("#editClienteBtn").disabled = false;
        document.querySelector("#editTelefonoBtn").disabled = false;
        document.querySelector("#editIdentificacionBtn").disabled = false;
        document.querySelector("#editMarcaBtn").disabled = false;
        document.querySelector("#editModeloBtn").disabled = false;
        document.querySelector("#editTipoVehiculoBtn").disabled = false;
        document.querySelector("#limpiarFormulario").disabled = false;
        document.querySelector("#guardarIngreso").disabled = false;
    }

    function deshabilitarBotones() {
        document.querySelector("#editClienteBtn").disabled = true;
        document.querySelector("#editTelefonoBtn").disabled = true;
        document.querySelector("#editIdentificacionBtn").disabled = true;
        document.querySelector("#editMarcaBtn").disabled = true;
        document.querySelector("#editModeloBtn").disabled = true;
        document.querySelector("#editTipoVehiculoBtn").disabled = true;
        document.querySelector("#limpiarFormulario").disabled = true;
        document.querySelector("#guardarIngreso").disabled = true;
    }
    document.querySelector("#editClienteBtn").addEventListener("click", function () { habilitarCampo("cliente"); });
    document.querySelector("#editTelefonoBtn").addEventListener("click", function () { habilitarCampo("telefono"); });
    document.querySelector("#editIdentificacionBtn").addEventListener("click", function () { habilitarCampo("identificacion"); });
    document.querySelector("#editMarcaBtn").addEventListener("click", function () { habilitarCampo("marca"); });
    document.querySelector("#editModeloBtn").addEventListener("click", function () { habilitarCampo("modelo"); });
    document.querySelector("#editTipoVehiculoBtn").addEventListener("click", function () { habilitarCampo("tipoVehiculo"); });

    document.querySelector("#limpiarFormulario").addEventListener("click", function () {
        const campos = ["placa", "cliente", "telefono", "identificacion", "marca", "modelo", "tipoVehiculo"];
        campos.forEach(campo => {
            const input = document.getElementById(campo);
            input.value = "";
            input.readOnly = false;
            input.classList.remove("disabled-input");
        });
        deshabilitarCamposInicialmente();
    });

    function habilitarCampo(campo) {
        const input = document.getElementById(campo);
        input.readOnly = false;
        input.classList.remove("disabled-input");
    }
});


//LOGICA DE ENVIO SOLO PRODUCTO O SOLO SERVICIO
//enviar el id del detalle servicio al modal:
document.addEventListener('DOMContentLoaded', function () {
    const addServiceButtons = document.querySelectorAll('.add-service-btn');
    addServiceButtons.forEach(button => {
        button.addEventListener('click', function () {
            const detalleId = this.getAttribute('data-id');
            document.getElementById('detalleId').value = detalleId;
        });
    });
});
document.addEventListener('DOMContentLoaded', function () {
    const productoSelect = document.getElementById('producto');
    const cantidadInput = document.getElementById('cantidad');
    const tipoServicioSelect = document.getElementById('tipoServicio');
    const servicioBasicoSelect = document.getElementById('servicioBasico');
    const servicioEspecialSelect = document.getElementById('servicioEspecial');
    // Resetea los atributos `required`
    function resetRequired(inputs) {
        inputs.forEach(input => input.removeAttribute('required'));
    }

    // Asigna los atributos `required`
    function setRequired(inputs) {
        inputs.forEach(input => input.setAttribute('required', ''));
    }

    // Cambios dinámicos en el tipo de venta
    const tipoVentaSelect = document.getElementById('tipoVenta');
    const productoContainer = document.getElementById('productoContainer');
    const servicioContainer = document.getElementById('servicioContainer');

    tipoVentaSelect.addEventListener('change', function () {
        const tipo = tipoVentaSelect.value;
        productoContainer.classList.toggle('d-none', tipo !== '2');
        servicioContainer.classList.toggle('d-none', tipo !== '1');

        // Si se muestra el contenedor de producto o servicio, selecciona el primer option
        if (tipo === '2') { // Producto
            document.getElementById('producto').selectedIndex = 0; // Selecciona el primer producto

            setRequired([productoSelect, cantidadInput]);
            resetRequired([tipoServicioSelect, servicioBasicoSelect, servicioEspecialSelect]);

        }
        if (tipo === '1') { // Servicio
            document.getElementById('tipoServicio').selectedIndex = 0; // Selecciona el primer tipo de servicio
            // Resetea los servicios básicos y especiales a su primer opción
            document.getElementById('servicioBasico').selectedIndex = 0;
            document.getElementById('servicioEspecial').selectedIndex = 0;
            resetRequired([cantidadInput, productoSelect]);
            setRequired([tipoServicioSelect]);

        }
    });

    // Cambios dinámicos en el tipo de servicio
    const servicioBasicoContainer = document.getElementById('servicioBasicoContainer');
    const servicioEspecialContainer = document.getElementById('servicioEspecialContainer');

    tipoServicioSelect.addEventListener('change', function () {
        const tipoServicio = tipoServicioSelect.value;
        servicioBasicoContainer.classList.toggle('d-none', tipoServicio !== 'Basico');
        servicioEspecialContainer.classList.toggle('d-none', tipoServicio !== 'Especial');

        // Resetea los servicios a su primer opción al mostrar un contenedor
        if (tipoServicio === 'Basico') {
            document.getElementById('servicioBasico').selectedIndex = 0;
            setRequired([servicioBasicoSelect]);
        }
        if (tipoServicio === 'Especial') {
            document.getElementById('servicioEspecial').selectedIndex = 0;
            setRequired([servicioEspecialSelect]);
        }
    });
});





//CONCRETAR PAGO
const pagarModal = document.getElementById('pagarModal');
pagarModal.addEventListener('show.bs.modal', async function (event) {
    const button = event.relatedTarget;  // Botón que abrió el modal
    const detalleId = button.getAttribute('data-id');  // ID del detalle ingreso

    try {
        const response = await fetch(`/app/atencion/ingreso/${detalleId}`);
        const servicios = await response.json();

        const detallePago = document.getElementById('detallePago');
        detallePago.innerHTML = '';  // Limpiar la tabla

        let total = 0;
        servicios.forEach(servicio => {
            const row = document.createElement('tr');
            row.setAttribute('data-id', servicio.id);  // Agregar el atributo data-id aquí
            row.innerHTML = `
                <td>${servicio.nombreItem}</td>
                <td>${servicio.cantidad}</td>
                <td>${servicio.precio_unitario}</td>
                <td>S/. ${servicio.subtotal}</td>
            `;
            detallePago.appendChild(row);
            total += parseFloat(servicio.subtotal);  // Sumar el subtotal
        });

        document.getElementById('total').value = 'S/. ' + total.toFixed(2);
    } catch (error) {
        console.error("Error al obtener los servicios:", error);
    }
});
document.getElementById('confirmarPago').addEventListener('click', async function () {
    // Obtener los datos del modal
    const total = document.getElementById('total').value.replace('S/. ', '').trim(); // Eliminar el prefijo de moneda
    const metodoPago = document.getElementById('metodoPago').value; // Obtener el método de pago
    const detallePagoRows = document.querySelectorAll('#detallePago tr'); // Filas de la tabla
    const detalleVentaIds = []; // Lista de IDs de detalle_venta

    // Extraer los IDs de detalle_venta (asumiendo que has agregado un atributo "data-id" en cada fila)
    detallePagoRows.forEach(row => {
        const id = row.getAttribute('data-id'); // Asume que has agregado el atributo data-id en cada fila
        if (id) {
            detalleVentaIds.push(id);
        }
    });

    // Validar datos requeridos
    if (!metodoPago || detalleVentaIds.length === 0) {
        console.log('Método de pago seleccionado:', metodoPago);
        console.log('Lista id detalle venta:', detalleVentaIds);
        Swal.fire({
            icon: 'error',
            title: 'Debe seleccionar un método de pago y tener al menos un detalle para pagar.',
            confirmButtonText: 'Aceptar'
        });
        return;
    }

    try {
        // Crear un objeto JSON para enviar los datos
        const data = {
            total: total,
            metodoPago: metodoPago,
            detalleVentaIds: detalleVentaIds
        };

        // Verificar el contenido antes de enviarlo
        console.log('Datos a enviar:', JSON.stringify(data));

        // Realizar la solicitud POST con el objeto JSON
        const response = await fetch('/app/atencion/venta', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json' // Establecemos el tipo de contenido a JSON
            },
            body: JSON.stringify(data) // Enviar el objeto JSON
        });

        if (response.ok) {
            $('#pagarModal').modal('hide');
            window.location.reload();
        } else {
            const error = await response.json();
            console.error('Error al realizar el pago:', error);
            Swal.fire({
                icon: 'error',
                title: 'Error al realizar el pago',
                text: error.message,
                confirmButtonText: 'Aceptar'
            });
        }

    } catch (error) {
        console.error('Error en la solicitud:', error);
        Swal.fire({
            icon: 'error',
            title: 'Ocurrió un error inesperado',
            text: 'Inténtelo de nuevo más tarde.',
            confirmButtonText: 'Aceptar'
        });
    }

});


//PRESIONAR ENTER EN MODAL

let bloquearEnterBusqueda = false; // Variable de control

// SweetAlert se cierra con Enter
document.addEventListener("keydown", function (event) {
    if (event.key === "Enter" && Swal.isVisible()) {
        Swal.clickConfirm(); // Simula clic en el botón de confirmación
    }
});

    // Capturar la búsqueda de placas con Enter, pero bloquearla si SweetAlert está abierto
document.getElementById("placa").addEventListener("keydown", function (event) {
    if (event.key === "Enter" && !bloquearEnterBusqueda) { // Solo se activa si no está bloqueado
        event.preventDefault(); // Evita que el formulario se envíe automáticamente
        bloquearEnterBusqueda = true; // Bloquea la búsqueda hasta que se cierre el SweetAlert

        document.querySelector("#addIngresoForm .btn-secondary").click(); // Simula el clic en el botón

        // Esperamos a que se abra un SweetAlert y lo desbloqueamos al cerrarse
        setTimeout(() => {
            const observer = new MutationObserver((mutations, obs) => {
                if (!Swal.isVisible()) { // Detecta cuando se ha cerrado el SweetAlert
                    bloquearEnterBusqueda = false; // Desbloquea la búsqueda
                    obs.disconnect(); // Deja de observar
                }
            });

            observer.observe(document.body, { childList: true, subtree: true });
        }, 100);
    }
});
//evita que haya conflicto al usar enter dentro del modal
    document.addEventListener("keydown", function (event) {
    if (event.key === "Enter" && document.getElementById("addIngresoModal").classList.contains("show")) {
        event.preventDefault(); // Evita que "Enter" haga que el modal se abra de nuevo
    }
});

    //focus en el input de placa en modal añadir ingreso:
    document.getElementById("addIngresoModal").addEventListener("shown.bs.modal", function () {
    document.getElementById("placa").focus(); // Selecciona automáticamente el input de Placa
});
