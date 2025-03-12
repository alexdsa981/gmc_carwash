$(document).ready(function () {
    var table = $('#example').DataTable({
        language: { url: 'https://cdn.datatables.net/plug-ins/1.13.6/i18n/es-ES.json' },
        order: [[2, 'desc']],
        dom: '<"d-flex justify-content-between align-items-center"<"left-section d-flex align-items-center"l><"right-section d-flex align-items-center"f>>tip',
        initComplete: function () {
            // Agregar el botón "Volver a Inicio" a la izquierda
            $(".left-section").prepend(`
                <a href="/inicio" class="btn btn-outline-primary me-3">
                    <i class="bi bi-arrow-left me-2"></i> Volver a Inicio
                </a>
            `);

            // Agregar el botón "AÑADIR SERVICIOS" a la derecha después de la barra de búsqueda
            $(".right-section").append(`
                <button class="btn btn-primary ms-3" id="addServicioBtn" data-bs-toggle="modal" data-bs-target="#addServicioModal">
                    Añadir Servicios
                </button>
            `);
        }
    });
});



// ELIMINAR
function eliminarServicio(button) {
    const servicioId = button.getAttribute("data-id");

    Swal.fire({
        icon: 'warning',
        title: '¿Estás seguro de que deseas eliminar este servicio?',
        showCancelButton: true,
        confirmButtonText: 'Sí, eliminar',
        cancelButtonText: 'Cancelar',
        focusCancel: true
    }).then(result => {
        if (result.isConfirmed) {
            fetch(`/app/servicio/desactivar/Tipo-Servicio/${servicioId}`, {
                method: "POST",
            })
            .then(response => {
                if (response.ok) {
                    Swal.fire({
                        icon: 'success',
                        title: 'Servicio eliminado correctamente',
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
                console.error("Error al eliminar el servicio:", error);
                Swal.fire({
                    icon: 'error',
                    title: 'Error al eliminar el servicio',
                    text: error,
                    confirmButtonText: 'Aceptar'
                });
            });
        }
    });
}



//EDITAR SERVICIO
document.addEventListener("DOMContentLoaded", function () {
    // Selecciona todos los botones "Editar Servicio"
    const editButtons = document.querySelectorAll('button[data-bs-target="#editServiceModal"]');

    editButtons.forEach(button => {
        button.addEventListener("click", function () {
            // Obtener los datos del botón
            const id = this.getAttribute("data-id");
            const nombre = this.getAttribute("data-nombre");
            const descripcion = this.getAttribute("data-descripcion");
            const isEspecial = this.getAttribute("data-isEspecial") === "Sí"; // Convertir a booleano

            // Llenar los campos del modal con los datos obtenidos
            document.getElementById("editServiceName").value = nombre;
            document.getElementById("editDescripcion").value = descripcion;
            document.getElementById("editIsEspecial").checked = isEspecial;

            // Guardar el ID del servicio en el formulario
            document.getElementById("editServiceForm").setAttribute("data-id", id);
        });
    });
});

document.getElementById("editServiceForm").addEventListener("submit", function (event) {
    event.preventDefault(); // Evitar el envío predeterminado del formulario

    // Obtener el ID del servicio
    const serviceId = this.getAttribute("data-id");

    // Crear los datos a enviar
    const data = new FormData();
    data.append("nombre", document.getElementById("editServiceName").value);
    data.append("descripcion", document.getElementById("editDescripcion").value);
    if (document.getElementById("editIsEspecial").checked) {
        data.append("isEspecial", "on"); // Mantiene el mismo formato del backend
    }

    // Enviar los datos al endpoint correcto
    fetch(`/app/servicio/actualizar/Tipo-Servicio/${serviceId}`, {
        method: "POST",
        body: data,
    })
    .then(response => {
        if (response.ok) {
            Swal.fire({
                icon: 'success',
                title: 'Servicio editado correctamente',
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
        console.error("Error al editar el servicio:", error);
        Swal.fire({
            icon: 'error',
            title: 'Error al editar el servicio',
            text: error,
            confirmButtonText: 'Aceptar'
        });
    });
});