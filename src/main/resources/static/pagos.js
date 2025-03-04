$(document).ready(function () {
    let diaActual = new Date().getDate();
    let mesActual = new Date().getMonth() + 1;
    let anioActual = new Date().getFullYear();

    inicializarFiltros();
    inicializarTabla();
    cargarPagos(anioActual, mesActual, calcularQuincena(diaActual));

function inicializarFiltros() {
    $("#mesFiltro").val(mesActual);
    let selectAnio = $("#anioFiltro");

    for (let i = anioActual; i >= anioActual - 5; i--) {
        selectAnio.append(new Option(i, i));
    }
    selectAnio.val(anioActual);

    // Obtener la quincena actual y seleccionarla en el filtro
    let quincenaActual = calcularQuincena(diaActual);
    $("#quincenaFiltro").val(quincenaActual);
}

function inicializarTabla() {
    $('#paymentsTable').DataTable({
        language: { url: 'https://cdn.datatables.net/plug-ins/1.13.6/i18n/es-ES.json' },
        paging: false,
        lengthChange: false,
        info: false,
        searching: false,
        order: [[4, 'desc']],
        columnDefs: [{ type: "datetime", targets: 4 }]
    });

    $('#totalsTable').DataTable({
        language: { url: 'https://cdn.datatables.net/plug-ins/1.13.6/i18n/es-ES.json' },
        paging: false,
        lengthChange: false,
        info: false,
        searching: false
    });
}


    function cargarPagos(year, month, quincena) {
        $.ajax({
            url: `/app/sueldos/pagos?year=${year}&month=${month}&quincena=${quincena}`,
            method: 'GET',
            dataType: 'json',
            success: function (data) {
                actualizarTabla(data);
                agregarTotalesPorColaborador(calcularTotales(data));
            },
            error: function (xhr, status, error) {
                console.error('Error al obtener los datos:', error);
            }
        });
    }
function calcularTotales(data) {
    let pagosPorColaborador = {};

    data.forEach(pago => {
        let monto = parseFloat(pago.montoPagado);
        let sueldoFijo = parseFloat(pago.sueldoFijo);

        if (!pagosPorColaborador[pago.nombre]) {
            pagosPorColaborador[pago.nombre] = sueldoFijo;
        }

        if (pago.tipoOperacion === 1 && !pago.estado) {
            pagosPorColaborador[pago.nombre] += monto;
        } else if (pago.tipoOperacion === -1 && pago.estado) {
            pagosPorColaborador[pago.nombre] -= monto;
        }
    });

    return pagosPorColaborador;
}

    function calcularQuincena(dia) {
        return dia <= 15 ? 1 : 2;
    }


function actualizarTabla(data) {
    let table = $('#paymentsTable').DataTable();
    table.clear();

    let pagosPorColaborador = {};

    data.forEach(pago => {
        let monto = parseFloat(pago.montoPagado);
        let sueldoFijo = parseFloat(pago.sueldoFijo);

        if (!pagosPorColaborador[pago.nombre]) {
            pagosPorColaborador[pago.nombre] = sueldoFijo;
        }

        if (pago.tipoOperacion === 1 && !pago.estado) {
            pagosPorColaborador[pago.nombre] += monto; // Se suma si no está confirmado
        } else if (pago.tipoOperacion === -1 && pago.estado) {
            pagosPorColaborador[pago.nombre] -= monto; // Se resta solo si está confirmado
        }

        let afectaSueldoBadge = pago.tipoOperacion === 1
            ? '<span class="badge bg-success">Suma</span>'
            : pago.tipoOperacion === -1
                ? '<span class="badge bg-danger">Resta</span>'
                : '<span class="badge bg-secondary">No afecta</span>';

        let estadoBadge = pago.estado
            ? '<span class="badge bg-success">Confirmado</span>'
            : '<span class="badge bg-warning text-dark">Pendiente</span>';

        let estadoButton = `<button class="btn btn-sm cambiar-estado" data-id="${pago.id}" data-estado="${pago.estado}" title="Cambiar Estado">
                                <i class="bi ${pago.estado ? 'bi-x-circle text-danger' : 'bi-check-circle text-success'}"></i>
                            </button>`;

        table.row.add([
            pago.nombre,
            `S/. ${sueldoFijo.toFixed(2)}`,
            `S/. ${monto.toFixed(2)}`,
            pago.comentario,
            pago.fecha + " " + pago.hora,
            afectaSueldoBadge,
            `<div class="d-flex align-items-center gap-2">${estadoBadge} ${estadoButton}</div>`,
            '<button class="btn btn-outline-danger delete-payment" data-id="' + pago.id + '" title="Eliminar"><i class="bi bi-trash"></i></button>'
        ]);
    });

    table.draw();
    agregarTotalesPorColaborador(pagosPorColaborador);
    activarEventosBotones();
}



function activarEventosBotones() {
    $(".cambiar-estado").off("click").on("click", function () {
        let button = $(this);
        let id = button.data("id");
        let nuevoEstado = !button.data("estado");

        Swal.fire({
            title: `¿Estás seguro?`,
            text: `El estado cambiará a ${nuevoEstado ? "Pagado" : "Pendiente"}.`,
            icon: "warning",
            showCancelButton: true,
            confirmButtonColor: nuevoEstado ? "#28a745" : "#dc3545",
            cancelButtonColor: "#6c757d",
            confirmButtonText: "Sí, cambiar",
            cancelButtonText: "Cancelar"
        }).then((result) => {
            if (result.isConfirmed) {
                $.ajax({
                    url: `/app/sueldos/cambiar-estado/${id}`,
                    method: "PUT",
                    contentType: "application/json",
                    data: JSON.stringify({ estado: nuevoEstado }),
                    success: function () {
                        Swal.fire("Actualizado", "El estado ha sido cambiado correctamente.", "success");
                        filtrarPagos(); // Recargar la tabla después de actualizar
                    },
                    error: function (xhr, status, error) {
                        console.error("Error al cambiar el estado:", error);
                        Swal.fire("Error", "No se pudo cambiar el estado.", "error");
                    }
                });
            }
        });
    });
}



function agregarTotalesPorColaborador(pagosPorColaborador) {
    let tbody = $('#totalsTable tbody');
    tbody.empty(); // Limpiar la tabla antes de agregar nuevos datos

    Object.entries(pagosPorColaborador).forEach(([nombre, total]) => {
        tbody.append(`
            <tr>
                <td>${nombre}</td>
                <td>S/. ${total.toFixed(2)}</td>
            </tr>
        `);
    });
}

    window.filtrarPagos = function () {
        let mes = $("#mesFiltro").val();
        let anio = $("#anioFiltro").val();
        let quincena = $("#quincenaFiltro").val(); // Obtener quincena seleccionada
        cargarPagos(anio, mes, quincena);
    };



    // ELIMINAR UN PAGO (Delegado para soportar eventos dinámicos)
    $(document).on('click', '.delete-payment', function () {
        const pagoId = $(this).attr('data-id');

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
                fetch(`/app/sueldos/eliminar/${pagoId}`, {
                    method: 'DELETE'
                })
                .then(response => {
                    if (response.ok) {
                        Swal.fire(
                            'Eliminado!',
                            'El pago ha sido eliminado.',
                            'success'
                        ).then(() => {
                            filtrarPagos(); // Recargar los datos sin recargar la página
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
