$(document).ready(function () {
    var table = $('#realizadosTable').DataTable({
        language: { url: 'https://cdn.datatables.net/plug-ins/1.13.6/i18n/es-ES.json' },
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
