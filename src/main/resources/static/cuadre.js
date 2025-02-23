    $(document).ready(function () {
        const table = $('#example').DataTable({
            language: {
                url: 'https://cdn.datatables.net/plug-ins/1.13.6/i18n/es-ES.json',
            },
            order: [[0, 'desc']] // Ordenar por la primera columna (FECHA) en orden descendente
        });


        // Función para poblar la tabla con los datos del backend
        function populateTable(data) {
            // Limpiar la tabla antes de añadir nuevas filas
            table.clear();

            // Iterar sobre cada cuadre de caja
            data.forEach(cuadre => {
                const totalProductos = cuadre.productos.reduce((sum, producto) => sum + producto.recaudado, 0);
                const totalServicios = cuadre.servicios.reduce((sum, servicio) => sum + servicio.recaudado, 0);
                const totalVehiculos = cuadre.tiposVehiculos.reduce((sum, tipoVehiculo) => sum + tipoVehiculo.cantidad, 0);

                // Preparar los datos para insertar en la tabla
                const rowData = [
                    cuadre.fecha,  // Fecha
                    `<i class="bi bi-truck"></i> ${totalVehiculos} vehículos atendidos <hr><small>${generateVehiculosList(cuadre.tiposVehiculos)}</small>`,
                    `S/. ${totalProductos.toFixed(2)} <hr><small>${generateProductosList(cuadre.productos)}</small>`,
                    `S/. ${totalServicios.toFixed(2)} <hr><small>${generateServiciosList(cuadre.servicios)}</small>`,
                    `S/. ${(totalProductos + totalServicios).toFixed(2)} <hr> <small>${generateMetodosList(cuadre.metodosPago)}</small>`, // Métodos de pago

                ];

                // Añadir la nueva fila con los datos
                table.rows.add([rowData]).draw();
            });
        }

        // Función para generar la lista de vehículos
        function generateVehiculosList(tiposVehiculos) {
            let listHTML = '';
            tiposVehiculos.forEach(tipoVehiculo => {
                listHTML += `
                    <p><i class="bi bi-car-front"></i> ${tipoVehiculo.tipoVehiculo}: <strong>${tipoVehiculo.cantidad}</strong> unidades</p>
                    <hr>
                `;
            });
            return listHTML;
        }

        // Función para generar la lista de productos
        function generateProductosList(productos) {
            let listHTML = '';
            productos.forEach(producto => {
                listHTML += `
                    <p><i class="bi bi-bag"></i> ${producto.nombre}: <strong>${producto.cantidad}</strong> unidades</p>
                    <p>Subtotal: <strong>S/. ${producto.recaudado.toFixed(2)}</strong></p>
                    <hr>
                `;
            });
            return listHTML;
        }

        // Función para generar la lista de servicios
        function generateServiciosList(servicios) {
            let listHTML = '';
            servicios.forEach(servicio => {
                listHTML += `
                    <p><i class="bi bi-gear"></i> ${servicio.nombre}: <strong>${servicio.cantidad}</strong> unidades</p>
                    <p>Subtotal: <strong>S/. ${servicio.recaudado.toFixed(2)}</strong></p>
                    <hr>
                `;
            });
            return listHTML;
        }


        function generateMetodosList(metodosPago) {
            let listHTML = '';
            metodosPago.forEach(metodo => {
                listHTML += `
                    <p><i class="bi bi-credit-card"></i> ${metodo.nombre}: S/. <strong>${metodo.total.toFixed(2)}</strong></p>
                    <hr>
                `;
            });
            return listHTML;
        }

        // Obtener los datos del backend (API)
        fetch('/app/caja/cuadre-caja')
            .then(response => response.json())
            .then(data => {
                populateTable(data);
            })
            .catch(error => {
                console.error('Error al obtener los datos:', error);
            });
    });