$(document).ready(function () {
    var table = $('#example').DataTable({
        language: { url: 'https://cdn.datatables.net/plug-ins/1.13.6/i18n/es-ES.json' },
        dom: '<"d-flex justify-content-between align-items-center"<"left-section d-flex align-items-center"l><"right-section d-flex align-items-center"f>>tip',
        initComplete: function () {
            // Agregar botones "Volver a Inicio" e "Ir a Historial" a la izquierda
            $(".left-section").prepend(`
                <a href="/inicio" class="btn btn-outline-primary me-2">
                    <i class="bi bi-arrow-left me-2"></i> Volver a Inicio
                </a>
                <a href="/inventario/historial" class="btn btn-outline-secondary" style="margin-right:10px;">
                    <i class="bi bi-clock me-2"></i> Ir a Historial
                </a>
            `);

            // Agregar botón "AÑADIR PRODUCTO" a la derecha
            $(".right-section").append(`
                <button class="btn btn-primary ms-3" id="addProductBtn" data-bs-toggle="modal" data-bs-target="#addProductModal">
                    AÑADIR PRODUCTO
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