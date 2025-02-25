$(document).ready(function () {
    var table = $('#example').DataTable({
        language: { url: 'https://cdn.datatables.net/plug-ins/1.13.6/i18n/es-ES.json' },
        dom: '<"d-flex justify-content-between align-items-center"<"left-section d-flex align-items-center"l><"right-section"f>>tip',
        initComplete: function () {
            // Agregar botón "Regresar" a la izquierda
            $(".left-section").prepend(`
                <a href="/inventario/lista" class="btn btn-outline-primary me-3">
                    <i class="bi bi-arrow-left me-2"></i> Regresar
                </a>
            `);
        }
    });
});


//ELIMINAR
function eliminarHistorial(button) {
    const id = button.getAttribute('data-id');

    Swal.fire({
        icon: 'warning',
        title: '¿Estás seguro de que deseas eliminar este historial?',
        showCancelButton: true,
        confirmButtonText: 'Sí, eliminar',
        cancelButtonText: 'Cancelar',
        focusCancel: true
    }).then(result => {
        if (result.isConfirmed) {
            fetch(`/app/inventario/eliminar-historial/${id}`, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json'
                },
            })
            .then(response => {
                if (response.ok) {
                    // Si la eliminación fue exitosa, eliminamos la fila de la tabla
                    const row = button.closest('tr');
                    row.remove();
                    Swal.fire({
                        icon: 'success',
                        title: 'Historial eliminado exitosamente',
                        confirmButtonText: 'Aceptar'
                    });
                } else {
                    Swal.fire({
                        icon: 'error',
                        title: 'Error',
                        text: 'Error al eliminar el historial.',
                        confirmButtonText: 'Aceptar'
                    });
                }
            })
            .catch(error => {
                console.error('Error:', error);
                Swal.fire({
                    icon: 'error',
                    title: 'Error',
                    text: 'Error al eliminar el historial.',
                    confirmButtonText: 'Aceptar'
                });
            });
        }
    });
}