$(document).ready(function () {
    var table = $('#example').DataTable({
        language: { url: 'https://cdn.datatables.net/plug-ins/1.13.6/i18n/es-ES.json' },
        dom: '<"d-flex justify-content-between align-items-center"<"left-section d-flex align-items-center"l><"right-section d-flex align-items-center"f>>tip',
        initComplete: function () {
            // Agregar los botones "Volver a Inicio" e "Ir a Pagos" a la izquierda
            $(".left-section").prepend(`
                <a href="/inicio" class="btn btn-outline-primary me-2">
                    <i class="bi bi-arrow-left me-2"></i> Volver a Inicio
                </a>
                <a class="btn btn-outline-secondary me-3" href="/colaboradores/pagos">Ir a Pagos</a>
            `);

            // Agregar botón "Añadir Colaborador" a la derecha después de la barra de búsqueda
            $(".right-section").append(`
                <button type="button" class="btn btn-outline-primary ms-3" data-bs-toggle="modal" data-bs-target="#addModal">
                    Añadir Colaborador
                </button>
            `);
        }
    });
});



//ELIMINAR COLABORADOR
document.querySelectorAll('.delete-row').forEach(button => {
    button.addEventListener('click', function () {
        const colaboradorId = this.getAttribute('data-id');

        // Usar SweetAlert para la confirmación
        Swal.fire({
            title: '¿Estás seguro?',
            text: 'No podrás revertir esta acción.',
            icon: 'warning',
            showCancelButton: true,
            confirmButtonText: 'Sí, eliminar',
            cancelButtonText: 'Cancelar',
            reverseButtons: true
        }).then((result) => {
            if (result.isConfirmed) {
                // Llamar a la API para eliminar el colaborador
                fetch(`/app/colaboradores/eliminar/${colaboradorId}`, {
                    method: 'POST'
                })
                    .then(response => {
                        if (response.ok) {
                            Swal.fire(
                                'Eliminado!',
                                'El colaborador ha sido eliminado.',
                                'success'
                            ).then(() => {
                                location.reload(); // Recargar la página para actualizar la lista
                            });
                        } else {
                            return response.text().then((text) => {
                                throw new Error(text);
                            });
                        }
                    })
                    .catch(error => {
                        Swal.fire(
                            'Error!',
                            'Hubo un problema al eliminar el colaborador.',
                            'error'
                        );
                    });
            }
        });
    });
});





// EDITAR COLABORADOR
document.querySelectorAll('.edit-row').forEach(button => {
    button.addEventListener('click', function () {
        // Cargar los datos del colaborador en el modal
        document.getElementById('editName').value = this.getAttribute('data-nombre');
        document.getElementById('editId').value = this.getAttribute('data-identificacion');
        document.getElementById('editPhone').value = this.getAttribute('data-telefono');
        document.getElementById('editMoney').value = this.getAttribute('data-sueldo-fijo');
        document.getElementById('editDescription').value = this.getAttribute('data-descripcion');

        // Guardar el ID del colaborador en un atributo de la forma
        document.getElementById('editCollaboratorForm').setAttribute('data-id', this.getAttribute('data-id'));

        // Abrir el modal
        $('#editModal').modal('show');
    });
});

// Función para enviar el formulario con una solicitud PUT usando fetch
function enviarFormulario() {
    const form = document.getElementById('editCollaboratorForm');
    const formData = new FormData(form);

    // Obtener el ID del colaborador del atributo de la forma
    const id = form.getAttribute('data-id');

    // Enviar solicitud PUT con fetch
    fetch(`/app/colaboradores/editar/${id}`, {
        method: 'PUT',
        body: formData
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
            'Hubo un problema al editar el colaborador.',
            'error'
        );
    });
}