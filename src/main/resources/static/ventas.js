$(document).ready(function () {
    var table = $('#realizadosTable').DataTable({
        language: { url: 'https://cdn.datatables.net/plug-ins/1.13.6/i18n/es-ES.json' },
        columnDefs: [
            {
                targets: 0, // Primera columna (Fecha y Hora)
                type: 'date', // Ahora se maneja como fecha
                render: function (data, type, row, meta) {
                    if (type === 'sort') {
                        return $(table.cell(meta.row, meta.col).node()).attr('data-order');
                    }
                    return data; // Mostramos el valor visible de la celda
                }
            }
        ],
        order: [[0, "desc"]],
        dom: '<"d-flex justify-content-between align-items-center"lf>tip',
        initComplete: function () {
            $(".dataTables_length").prepend(`
                <a href="/atencion" class="btn btn-outline-primary me-3">
                    <i class="bi bi-arrow-left me-2"></i> Volver a Atención
                </a>
            `);
        }
    });
});



    function eliminarVenta(button) {
        let id = button.getAttribute("data-id");

        Swal.fire({
            title: "¿Estás seguro?",
            text: "Esta acción eliminará la venta de forma permanente.",
            icon: "warning",
            showCancelButton: true,
            confirmButtonColor: "#d33",
            cancelButtonColor: "#3085d6",
            confirmButtonText: "Sí, eliminar",
            cancelButtonText: "Cancelar"
        }).then((result) => {
            if (result.isConfirmed) {
                fetch(`/app/atencion/eliminar-venta/${id}`, {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json"
                    }
                })
                .then(response => {
                    if (response.ok) {
                        Swal.fire({
                            title: "Eliminado",
                            text: "La venta ha sido eliminada correctamente.",
                            icon: "success",
                            confirmButtonColor: "#3085d6",
                            confirmButtonText: "OK"
                        }).then(() => {
                            location.reload();
                        });
                    } else {
                        Swal.fire({
                            title: "Error",
                            text: "Ocurrió un problema al eliminar la venta.",
                            icon: "error",
                            confirmButtonColor: "#d33",
                            confirmButtonText: "OK"
                        });
                    }
                })
                .catch(error => {
                    console.error("Error:", error);
                    Swal.fire({
                        title: "Error",
                        text: "Ocurrió un error inesperado.",
                        icon: "error",
                        confirmButtonColor: "#d33",
                        confirmButtonText: "OK"
                    });
                });
            }
        });
    }