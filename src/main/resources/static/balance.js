        $(document).ready(function () {
            let diaActual = new Date().getDate();
            let mesActual = new Date().getMonth() + 1;
            let anioActual = new Date().getFullYear();

            inicializarFiltros();
            inicializarTabla();
            cargarDatos(anioActual, mesActual);

            function inicializarFiltros() {
                $("#mesFiltro").val(mesActual);
                let selectAnio = $("#anioFiltro");

                for (let i = anioActual; i >= anioActual - 5; i--) {
                    selectAnio.append(new Option(i, i));
                }
                selectAnio.val(anioActual);
            }

            function inicializarTabla() {
                $("#balance").DataTable({
                    paging: false,
                    lengthChange: false,
                    info: false,
                    searching: false,
                });
            }

            function cargarDatos(year, month) {
                $.ajax({
                    url: `/app/balance/balances?year=${year}&month=${month}`,
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
                let table = $("#balance").DataTable();
                table.clear();
                let totalBalance = 0;
                let acumuladorSemanal = 0;
                let ultimaFilaSemana = null;

                data.forEach((item, index) => {
                    let fechaObj = new Date(item.fecha);
                    let esDomingo = fechaObj.getDay() === 6;
                    acumuladorSemanal += item.balance;
                    totalBalance += item.balance;

                    let fila = table.row.add([
                        item.fecha,
                        item.subDia,
                        item.dia,
                        `S/ ${item.ingresos.toFixed(2)}`,
                        `S/ ${item.egresos.toFixed(2)}`,
                        `S/ ${item.balance.toFixed(2)}`,
                        ""
                    ]).node();

               if (esDiaActual(item.fecha)) {
            $(fila).addClass("fila-dia-actual");
        }


                    ultimaFilaSemana = fila;
                    if (esDomingo) {
                        $(fila).addClass("domingo-borde");
                        $(fila).find("td:eq(6)").text(`S/ ${acumuladorSemanal.toFixed(2)}`);
                        acumuladorSemanal = 0;
                    }
                });

                if (ultimaFilaSemana) {
                    $(ultimaFilaSemana).find("td:eq(6)").text(`S/ ${acumuladorSemanal.toFixed(2)}`);
                }

                $("#totalBalanceMes").text(`S/ ${totalBalance.toFixed(2)}`);
                table.draw();
            }

            function esDiaActual(fecha) {
                let partes = fecha.split("-"); // Separar "YYYY-MM-DD"
                return (
                    parseInt(partes[2], 10) === diaActual &&  // Día
                    parseInt(partes[1], 10) === mesActual &&  // Mes
                    parseInt(partes[0], 10) === anioActual    // Año
                );
            }


            window.filtrarBalance = function () {
                let mes = $("#mesFiltro").val();
                let anio = $("#anioFiltro").val();
                cargarDatos(anio, mes);
            };
        });