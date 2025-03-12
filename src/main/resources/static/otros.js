$(document).ready(function () {
    $('#tablaVehiculo, #tablaMetodoPago').DataTable({
        "pageLength": 5, // Muestra 5 registros por defecto
        "lengthChange": false, // Oculta la opción de cambiar la cantidad de registros por página
        "searching": false, // Desactiva la barra de búsqueda
        "info": false, // Oculta la información de paginación (ej. "Mostrando página 1 de 3")
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
});
