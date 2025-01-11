$(document).ready(function () {
    var table = $('#example').DataTable({
        language: { url: 'https://cdn.datatables.net/plug-ins/1.13.6/i18n/es-ES.json' }
    });

    var paymentsTable = $('#paymentsTable').DataTable({
        language: { url: 'https://cdn.datatables.net/plug-ins/1.13.6/i18n/es-ES.json' }
    });


    // Botón Añadir Colaborador
    $('#btnAddCollaborator').click(function () {
        editingRow = null;
        $('#collaboratorForm')[0].reset();
        $('#addModal').modal('show');
    });

    // Al hacer clic en el botón Pagar Sueldo
    $('#btnPaySalary').click(function () {
        $('#paySalaryModal').modal('show');
    });

    // Botón para cambiar a la vista de pagos
    $('#btnPayments').click(function () {
        $('#mainView').addClass('d-none');
        $('#paymentsView').removeClass('d-none');
    });

    // Botón para regresar a la vista principal
    $('#btnBackToMain').click(function () {
        $('#paymentsView').addClass('d-none');
        $('#mainView').removeClass('d-none');
    });
});