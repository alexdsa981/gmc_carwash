    document.addEventListener("DOMContentLoaded", function() {
// Inicializar DataTables
let table = $("#egresosTable").DataTable({
    "ajax": {
        "url": "/app/egresos/listar",
        "dataSrc": "" // Si el JSON es un array directo
    },
    "columns": [
        { "data": "descripcion" },
        {
            "data": "monto",
            "render": function(data) { return `S/ ${parseFloat(data).toFixed(2)}`; }
        },
        {
            "data": "formattedFecha",
            "render": function(data, type, row) {
                return `${row.formattedFecha} ${row.formattedHora || ''}`;
            }
        },
        {
            "data": "id",
            "render": function(data, type, row) {
                return `
                    <button class="btn btn-warning btn-sm" onclick="abrirModalEditar(${data}, '${row.descripcion}', ${row.monto})">
                        <i class="bi bi-pencil-square"></i>
                    </button>
                    <button class="btn btn-danger btn-sm" onclick="eliminarEgreso(${data})">
                        <i class="bi bi-trash"></i>
                    </button>`;
            }
        }
    ],
    "order": [[2, "desc"]] // Ordenar por la tercera columna (fecha) en orden descendente
});

        // Manejo del formulario para agregar egresos sin recargar la página
        document.getElementById("egresoForm").addEventListener("submit", function(event) {
            event.preventDefault();

            const formData = new FormData(this);
            fetch("/app/egresos/agregar", {
                method: "POST",
                body: new URLSearchParams(formData) // Convertir a `application/x-www-form-urlencoded`
            })
            .then(response => {
                if (response.ok) {
                    table.ajax.reload(null, false); // Recargar DataTables sin resetear paginación
                    this.reset(); // Limpiar formulario
                    Swal.fire({
                        icon: "success",
                        title: "¡Egreso agregado!",
                        text: "El egreso ha sido registrado correctamente.",
                        timer: 2000,
                        showConfirmButton: false
                    });
                } else {
                    Swal.fire({
                        icon: "error",
                        title: "Error",
                        text: "No se pudo agregar el egreso.",
                    });
                }
            })
            .catch(error => console.error("Error al agregar egreso:", error));
        });
    });

    // Función para eliminar egresos con SweetAlert2
    function eliminarEgreso(id) {
        Swal.fire({
            title: "¿Estás seguro?",
            text: "No podrás revertir esta acción.",
            icon: "warning",
            showCancelButton: true,
            confirmButtonColor: "#d33",
            cancelButtonColor: "#3085d6",
            confirmButtonText: "Sí, eliminar",
            cancelButtonText: "Cancelar"
        }).then((result) => {
            if (result.isConfirmed) {
                fetch(`/app/egresos/eliminar/${id}`, { method: "DELETE" })
                    .then(response => {
                        if (response.ok) {
                            $("#egresosTable").DataTable().ajax.reload(null, false);
                            Swal.fire({
                                icon: "success",
                                title: "Eliminado",
                                text: "El egreso ha sido eliminado.",
                                timer: 2000,
                                showConfirmButton: false
                            });
                        } else {
                            Swal.fire({
                                icon: "error",
                                title: "Error",
                                text: "No se pudo eliminar el egreso.",
                            });
                        }
                    })
                    .catch(error => console.error("Error al eliminar:", error));
            }
        });
    }

function abrirModalEditar(id, descripcion, monto) {
    document.getElementById("egresoId").value = id;
    document.getElementById("descripcion").value = descripcion;
    document.getElementById("montoM").value = monto;

    let modal = new bootstrap.Modal(document.getElementById("modalEditarEgreso"));
    modal.show();
}



document.getElementById("formEditarEgreso").addEventListener("submit", function(event) {
    event.preventDefault(); // Evita el envío tradicional del formulario

    let id = document.getElementById("egresoId").value;
    let descripcion = document.getElementById("descripcion").value;
    let monto = document.getElementById("montoM").value;

    fetch(`/app/egresos/editar/${id}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded"
        },
        body: new URLSearchParams({
            descripcion: descripcion,
            monto: monto
        })
    })
    .then(response => {
        if (!response.ok) {
            throw new Error("Error al editar el egreso");
        }
        return response.text();
    })
    .then(message => {
        Swal.fire({
            icon: "success",
            title: "Egreso editado",
            text: message,
            confirmButtonColor: "#3085d6",
            confirmButtonText: "OK"
        }).then(() => {
            $("#modalEditarEgreso").modal("hide"); // Cierra el modal
            $("#egresosTable").DataTable().ajax.reload(); // Recarga la tabla
        });
    })
    .catch(error => {
        Swal.fire({
            icon: "error",
            title: "Error",
            text: error.message,
            confirmButtonColor: "#d33",
            confirmButtonText: "Cerrar"
        });
    });
});
