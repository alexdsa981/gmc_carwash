$(document).ready(function () {
    let diaActual = new Date().getDate();
    let mesActual = new Date().getMonth() + 1;
    let anioActual = new Date().getFullYear();

    inicializarFiltros();
    inicializarTabla();
    cargarPagos(anioActual, mesActual);

    function inicializarFiltros() {
        $("#mesFiltro").val(mesActual);
        let selectAnio = $("#anioFiltro");

        for (let i = anioActual; i >= anioActual - 5; i--) {
            selectAnio.append(new Option(i, i));
        }
        selectAnio.val(anioActual);
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
    }

    function cargarPagos(year, month) {
        $.ajax({
            url: `/app/sueldos/pagos?year=${year}&month=${month}`,
            method: 'GET',
            dataType: 'json',
            success: function (data) {
                actualizarTabla(data);
            },
            error: function (xhr, status, error) {
                console.error('Error al obtener los datos:', error);
            }
        });
    }

    function actualizarTabla(data) {
        let table = $('#paymentsTable').DataTable();
        table.clear();

        let totalPagado = 0;
        let pagosPorColaborador = {};

        data.forEach(pago => {
            let monto = parseFloat(pago.montoPagado);
            let sueldoFijo = parseFloat(pago.sueldoFijo);

            // Si el colaborador aún no está en el objeto, inicializar con su sueldo fijo
            if (!pagosPorColaborador[pago.nombre]) {
                pagosPorColaborador[pago.nombre] = sueldoFijo;
            }

            if (pago.tipoOperacion === 1) {
                pagosPorColaborador[pago.nombre] += monto; // Sumar si afecta sumando
            } else if (pago.tipoOperacion === -1) {
                pagosPorColaborador[pago.nombre] -= monto; // Restar si afecta restando
            }

            // Solo sumar al total general los pagos que afectan (sumando o restando)
            if (pago.tipoOperacion !== 0) {
                totalPagado += monto;
            }

            let afectaSueldoBadge = pago.tipoOperacion === 1
                ? '<span class="badge bg-success">Suma</span>'
                : pago.tipoOperacion === -1
                    ? '<span class="badge bg-danger">Resta</span>'
                    : '<span class="badge bg-secondary">No afecta</span>';

            table.row.add([
                pago.nombre,
                `S/. ${sueldoFijo.toFixed(2)}`,
                `S/. ${monto.toFixed(2)}`,
                pago.comentario,
                pago.fecha + " " + pago.hora,
                afectaSueldoBadge,
                '<button class="btn btn-outline-danger delete-payment" data-id="' + pago.id + '"><i class="bi bi-trash"></i></button>'
            ]);
        });

        $('#totalPagado').text(`S/. ${totalPagado.toFixed(2)}`);
        table.draw();

        agregarTotalesPorColaborador(pagosPorColaborador);
    }

    function agregarTotalesPorColaborador(pagosPorColaborador) {
        let tbody = $('#paymentsTable tbody');
        Object.entries(pagosPorColaborador).forEach(([nombre, total]) => {
            tbody.append(`
                <tr class="fw-bold bg-light">
                    <td colspan="2">${nombre} (Total a pagar)</td>
                    <td>S/. ${total.toFixed(2)}</td>
                    <td colspan="4"></td>
                </tr>
            `);
        });
    }

    window.filtrarPagos = function () {
        let mes = $("#mesFiltro").val();
        let anio = $("#anioFiltro").val();
        cargarPagos(anio, mes);
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
