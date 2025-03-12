$(document).ready(function () {
    // DataTable para la tabla principal
    var table = $('#example').DataTable({
        language: { url: 'https://cdn.datatables.net/plug-ins/1.13.6/i18n/es-ES.json' },
        dom: '<"d-flex justify-content-between align-items-center"<"left-section d-flex align-items-center"l><"right-section-main d-flex align-items-center"f>>tip',
        initComplete: function () {
            $(".left-section").prepend(`
                <a href="/inicio" class="btn btn-outline-primary me-2">
                    <i class="bi bi-arrow-left me-2"></i> Volver a Inicio
                </a>
                <a href="/inventario/historial" class="btn btn-outline-secondary" style="margin-right:10px;">
                    <i class="bi bi-clock me-2"></i> Ir a Historial
                </a>
            `);

            $(".right-section-main").append(`
                <button class="btn btn-secondary ms-3" data-bs-toggle="modal" data-bs-target="#tipoProductoModal">
                    Ver Categorias
                </button>
                <button class="btn btn-primary ms-2" id="addProductBtn" data-bs-toggle="modal" data-bs-target="#addProductModal">
                    Añadir Producto
                </button>
            `);
        }
    });

    // DataTable para la tabla dentro del modal
    $('#tipoProductoTable').DataTable({
        language: { url: 'https://cdn.datatables.net/plug-ins/1.13.6/i18n/es-ES.json' },
        dom: '<"d-flex justify-content-between align-items-center"<"left-section-modal d-flex align-items-center"l><"right-section-modal d-flex align-items-center"f>>tip',
        initComplete: function () {
            $(".right-section-modal").append(`
                <button class="btn btn-success ms-2" data-bs-toggle="modal" data-bs-target="#addTipoProductoModal">
                    Añadir Categoría
                </button>
            `);
        }
    });
});

// EDITAR PRODUCTO
    document.addEventListener("DOMContentLoaded", function () {
        // Selecciona todos los botones "Editar"
        const editButtons = document.querySelectorAll('button[data-bs-target="#editProductModal"]');

        editButtons.forEach(button => {
            button.addEventListener("click", function () {
                // Obtener los datos del botón
                const id = this.getAttribute("data-id");
                const nombre = this.getAttribute("data-nombre");
                const categoriaId = this.getAttribute("data-categoria"); // ID de la categoría
                const precioCosto = this.getAttribute("data-precio-costo");
                const precioVenta = this.getAttribute("data-precio-venta");

                // Llenar los campos del modal con los datos obtenidos
                document.getElementById("editProductName").value = nombre;
                document.getElementById("editProductCost").value = precioCosto;
                document.getElementById("editProductPrice").value = precioVenta;

                // Preseleccionar la categoría en el <select>
                const categorySelect = document.getElementById("editProductCategory");
                Array.from(categorySelect.options).forEach(option => {
                    option.selected = option.value === categoriaId;
                });

                // Si necesitas el ID del producto para guardar cambios
                document.getElementById("editProductForm").setAttribute("data-id", id);
            });
        });
    });

document.getElementById("editProductForm").addEventListener("submit", function (event) {
    event.preventDefault(); // Evitar el envío predeterminado del formulario

    // Obtener el ID del producto
    const productId = this.getAttribute("data-id");

    // Crear los datos a enviar
    const data = new FormData(this);
    data.append("nombre", document.getElementById("editProductName").value);
    data.append("precio_costo", document.getElementById("editProductCost").value);
    data.append("precio_venta", document.getElementById("editProductPrice").value);
    data.append("id_tipo_producto", document.getElementById("editProductCategory").value);

    // Enviar los datos al endpoint
    fetch(`/app/inventario/producto-${productId}/editar`, {
        method: "POST",
        body: data,
    })
    .then(response => {
        if (response.ok) {
            Swal.fire({
                icon: 'success',
                title: 'Producto editado correctamente',
                confirmButtonText: 'Aceptar'
            }).then(() => {
                location.reload(); // Recargar la página para reflejar cambios
            });
        } else {
            response.text().then(text => {
                Swal.fire({
                    icon: 'error',
                    title: 'Error',
                    text: `Error: ${text}`,
                    confirmButtonText: 'Aceptar'
                });
            });
        }
    })
    .catch(error => {
        console.error("Error al editar el producto:", error);
        Swal.fire({
            icon: 'error',
            title: 'Error al editar el producto',
            text: error,
            confirmButtonText: 'Aceptar'
        });
    });
});



//ELIMINAR PRODUCTO
function eliminarProducto(button) {
    const productId = button.getAttribute("data-id");

    Swal.fire({
        icon: 'warning',
        title: '¿Estás seguro de que deseas eliminar este producto?',
        showCancelButton: true,
        confirmButtonText: 'Sí, eliminar',
        cancelButtonText: 'Cancelar',
        focusCancel: true
    }).then(result => {
        if (result.isConfirmed) {
            fetch(`/app/inventario/producto-${productId}/eliminar`, {
                method: "POST",
            })
            .then(response => {
                if (response.ok) {
                    Swal.fire({
                        icon: 'success',
                        title: 'Producto eliminado correctamente',
                        confirmButtonText: 'Aceptar'
                    }).then(() => {
                        location.reload(); // Recargar la página para reflejar los cambios
                    });
                } else {
                    response.text().then(text => {
                        Swal.fire({
                            icon: 'error',
                            title: 'Error',
                            text: `Error: ${text}`,
                            confirmButtonText: 'Aceptar'
                        });
                    });
                }
            })
            .catch(error => {
                console.error("Error al eliminar el producto:", error);
                Swal.fire({
                    icon: 'error',
                    title: 'Error al eliminar el producto',
                    text: error,
                    confirmButtonText: 'Aceptar'
                });
            });
        }
    });
}


//ACTUALIZAR STOCK
function cargarDatosModal(button) {
    const productId = button.getAttribute('data-id');
    const productName = button.getAttribute('data-nombre');
    const productStock = button.getAttribute('data-stock');
    const productPrice = button.getAttribute('data-precio-venta');

    // Llenar los campos del modal con los datos del producto
    document.getElementById('cantidad').value = ''; // Limpiar el campo de cantidad
    document.getElementById('motivo').value = ''; // Limpiar el campo de motivo
    document.getElementById('precioUnitario').value = productPrice; // Establecer el precio de venta

    // Establecer el evento para el botón de "Actualizar Stock"
    document.getElementById('submitStockChange').onclick = function() {
        actualizarStock(productId);
    };
}

function actualizarStock(productId) {
    const cantidad = document.getElementById('cantidad').value;
    const motivo = document.getElementById('motivo').value;
    const precioUnitario = document.getElementById('precioUnitario').value;

    if (!cantidad || !motivo || !precioUnitario) {
        Swal.fire({
            icon: 'error',
            title: 'Por favor, complete todos los campos.',
            confirmButtonText: 'Aceptar'
        });
        return;
    }

    // Realizar la solicitud fetch al endpoint
    fetch(`/app/inventario/producto-${productId}/stock`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: new URLSearchParams({
            'cantidad': cantidad,
            'motivo': motivo,
            'precio_venta': precioUnitario
        })
    })
    .then(response => {
        if (response.ok) {
            Swal.fire({
                icon: 'success',
                title: 'Stock actualizado correctamente',
                confirmButtonText: 'Aceptar'
            }).then(() => {
                window.location.reload(); // Recargar la página para reflejar los cambios
            });
        } else {
            response.text().then(error => {
                Swal.fire({
                    icon: 'error',
                    title: 'Error',
                    text: error,
                    confirmButtonText: 'Aceptar'
                });
            });
        }
    })
    .catch(error => {
        Swal.fire({
            icon: 'error',
            title: 'Error al actualizar el stock',
            text: error,
            confirmButtonText: 'Aceptar'
        });
    });
}


    $(document).ready(function() {
        $('table').DataTable();
    });

    // Añadir Tipo Producto con SweetAlert
    $('#addTipoProductoForm').submit(function(event) {
        event.preventDefault();
        let nombre = $('#nombreTipoProducto').val();
        $.post("/app/inventario/tipoproducto/crear", {nombre: nombre}, function(response) {
            Swal.fire({
                title: "¡Éxito!",
                text: "Tipo de producto creado correctamente",
                icon: "success"
            }).then(() => {
                location.reload();
            });
        }).fail(function() {
            Swal.fire({
                title: "Error",
                text: "Error al crear tipo de producto",
                icon: "error"
            });
        });
    });

    // Eliminar Tipo Producto con SweetAlert
    function eliminarTipoProducto(element) {
        let id = $(element).attr("data-id");
        Swal.fire({
            title: "¿Estás seguro?",
            text: "Esta acción desactivará el tipo de producto",
            icon: "warning",
            showCancelButton: true,
            confirmButtonColor: "#d33",
            cancelButtonColor: "#3085d6",
            confirmButtonText: "Sí, eliminar"
        }).then((result) => {
            if (result.isConfirmed) {
                $.get(`/app/inventario/desactivar/Tipo-Producto/${id}`, function() {
                    Swal.fire({
                        title: "Eliminado!",
                        text: "Tipo de producto eliminado correctamente",
                        icon: "success"
                    }).then(() => {
                        location.reload();
                    });
                }).fail(function() {
                    Swal.fire({
                        title: "Error",
                        text: "Error al eliminar tipo de producto",
                        icon: "error"
                    });
                });
            }
        });
    }



// Cargar datos en el modal de edición
$('button[data-bs-target="#editTipoProductoModal"]').click(function() {
    let id = $(this).attr("data-id");
    let nombre = $(this).attr("data-nombre");
    $('#editTipoProductoId').val(id);
    $('#editNombreTipoProducto').val(nombre);
});

// Enviar formulario de edición con SweetAlert
$('#editTipoProductoForm').submit(function(event) {
    event.preventDefault();
    let id = $('#editTipoProductoId').val();
    let nombre = $('#editNombreTipoProducto').val();

    $.post(`/app/inventario/tipoproducto-${id}/editar`, {nombre: nombre}, function(response) {
        Swal.fire({
            title: "¡Éxito!",
            text: "Tipo de producto editado correctamente",
            icon: "success"
        }).then(() => {
            location.reload();
        });
    }).fail(function() {
        Swal.fire({
            title: "Error",
            text: "Error al editar tipo de producto",
            icon: "error"
        });
    });
});