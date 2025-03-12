$(document).ready(function () {
    // Configuración de DataTables
    $('#tablaVehiculo, #tablaMetodoPago').DataTable({
        "pageLength": 5,
        "lengthChange": false,
        "searching": false,
        "info": false,
        "language": {
            "zeroRecords": "No se encontraron resultados",
            "paginate": {
                "first": "Primero",
                "last": "Último",
                "next": "Siguiente",
                "previous": "Anterior"
            }
        }
    });

    // Función para abrir el modal y cargar los datos en el formulario de edición
    $(document).on("click", ".btn-editar", function () {
        let id = $(this).data("id");
        let nombre = $(this).data("nombre");
        let tipo = $(this).data("tipo");

        if (tipo === "vehiculo") {
            $("#editarNombreVehiculo").val(nombre);
            $("#formEditarVehiculo").attr("action", `/app/clasificadores/actualizar/Tipo-Vehiculo/${id}`);
            $("#modalEditarVehiculo").modal("show");
        } else if (tipo === "metodoPago") {
            $("#editarNombreMetodoPago").val(nombre);
            $("#formEditarMetodoPago").attr("action", `/app/clasificadores/actualizar/Metodo-Pago/${id}`);
            $("#modalEditarMetodoPago").modal("show");
        }
    });

    // Función para eliminar (desactivar) un elemento con SweetAlert2
    $(document).on("click", ".btn-eliminar", function () {
        let id = $(this).data("id");
        let tipo = $(this).data("tipo");

        let url = tipo === "vehiculo"
            ? `/app/clasificadores/desactivar/Tipo-Vehiculo/${id}`
            : `/app/clasificadores/desactivar/Metodo-Pago/${id}`;

        Swal.fire({
            title: "¿Estás seguro?",
            text: "Esta acción desactivará el registro.",
            icon: "warning",
            showCancelButton: true,
            confirmButtonColor: "#d33",
            cancelButtonColor: "#3085d6",
            confirmButtonText: "Sí, eliminar",
            cancelButtonText: "Cancelar"
        }).then((result) => {
            if (result.isConfirmed) {
                window.location.href = url;
            }
        });
    });

    // Envío del formulario de edición de Tipo Vehículo
    $("#formEditarVehiculo").submit(function (event) {
        event.preventDefault();
        let form = $(this);
        let url = form.attr("action");

        $.post(url, form.serialize(), function () {
            location.reload();
        });
    });

    // Envío del formulario de edición de Método de Pago
    $("#formEditarMetodoPago").submit(function (event) {
        event.preventDefault();
        let form = $(this);
        let url = form.attr("action");

        $.post(url, form.serialize(), function () {
            location.reload();
        });
    });
});
