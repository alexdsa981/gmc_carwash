$(document).ready(function () {
    var paymentsTable = $('#paymentsTable').DataTable({
        language: { url: 'https://cdn.datatables.net/plug-ins/1.13.6/i18n/es-ES.json' },
        dom: '<"d-flex justify-content-between align-items-center"<"left-section d-flex align-items-center"l><"right-section d-flex align-items-center"f>>tip',
        initComplete: function () {
            // Agregar el botón "Regresar" a la izquierda
            $(".left-section").prepend(`
                <a class="btn btn-outline-secondary me-3" href="/colaboradores/lista">Regresar</a>
            `);

            // Agregar el botón "Pagar Sueldo" a la derecha después de la barra de búsqueda
            $(".right-section").append(`
                <button type="button" class="btn btn-outline-success ms-3" data-bs-toggle="modal" data-bs-target="#paySalaryModal">
                    Registrar Pago
                </button>
            `);
        }
    });
});


// ELIMINAR UN PAGO
document.querySelectorAll('.delete-payment').forEach(button => {
    button.addEventListener('click', function () {
        const pagoId = this.getAttribute('data-id');

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
                // Llamar a la API para eliminar el pago
                fetch(`/app/pagos/eliminar/${pagoId}`, {
                    method: 'DELETE'
                })
                .then(response => {
                    if (response.ok) {
                        Swal.fire(
                            'Eliminado!',
                            'El pago ha sido eliminado.',
                            'success'
                        ).then(() => {
                            location.reload(); // Recargar la página para actualizar la lista
                        });
                    } else {
                        Swal.fire(
                            'Error!',
                            'Hubo un problema al eliminar el pago.',
                            'error'
                        );
                    }
                });
            }
        });
    });
});