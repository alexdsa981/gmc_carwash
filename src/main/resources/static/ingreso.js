$(document).ready(function() {
    var table = $('#ingresos').DataTable();

    function actualizarTotal() {
        var total = 0;
        table.rows({ search: 'applied' }).nodes().to$().find('.semanal').each(function() {
            var valor = $(this).text().replace('S/', '').trim();
            if (valor) {
                total += parseFloat(valor);
            }
        });
});
}