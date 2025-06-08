$(document).ready(function() {
    $('#enrolmentTable').DataTable({
        "ajax": {
            "url": "/api/enrollment",
            dataSrc: ""
        },
        responsive: true,
        "columns": [
            {"data": "enrolmentId"},
            {"data": "userName"},
            {"data": "courseName"},
            {
                "data": "createdDate",
                "render": function (data, type, row) {
                    if (type === 'display') {
                        return data ? new Date(data).toLocaleDateString() : '<span class="text-muted">No date</span>';
                    }
                    return data;
                }
            },
            {
                "data": "enrolmentId",
                "render": function (data, type, row) {
                    return `
                    <button class="btn btn-primary btn-icon-text btn-edit p-2" 
                        data-id="${row.enrolmentId}" 
                        data-user="${row.userName}">
                        <i class="ti-file btn-icon-append"></i> Edit
                    </button>
                    <button class="btn btn-danger btn-delete p-2" data-id="${data}">
                        Delete <i class="ti-trash btn-icon-append"></i>
                    </button>
                `;
                },
                "orderable": false
            }
        ],
        "columnDefs": [
            {"targets": [0], "visible": false}
        ],
        "initComplete": function () {
            $('#enrolmentTable').on('click', '.btn-edit', function () {
                const enrolmentId = $(this).data('id');
                openEditEnrolmentModal(enrolmentId);
            });

            $('#enrolmentTable').on('click', '.btn-delete', function () {
                const enrolmentId = $(this).data('id');
                deleteEnrol(enrolmentId)
            });
        }
    });

    $('#createEnrolmentForm').submit(function(e) {
        e.preventDefault();

        const enrolmentData = {
            userId: $('#userId').val(),
            courseId: $('#courseId').val(),
        };

        const token = $("meta[name='_csrf']").attr("content");
        const header = $("meta[name='_csrf_header']").attr("content");

        $.ajax({
            url: `/course/enrollment`,
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(enrolmentData),
            beforeSend: function(xhr) {
                xhr.setRequestHeader(header, token);
            },
            success: function() {
                $('#enrolmentTable').DataTable().ajax.reload();
                showToast("Success", "Berhasil menambahkan enrolment", "success", 5000);
                addEnrollModal.hide()
                $('#createEnrolmentForm')[0].reset();
            },
            error: function(xhr) {
                showToast("Error", xhr.responseJSON?.message || 'error saat enroll coba lagi nanti', "error", 5000);
            }
        });
    });

    $('#editEnrolmentForm').submit(function(e) {
        e.preventDefault();

        const enrolmentData = {
            id: $('#editEnrolmentId').val(),
            userId: $('#editUserId').val(),
            courseId: $('#editCourseId').val(),
        };

        const token = $("meta[name='_csrf']").attr("content");
        const header = $("meta[name='_csrf_header']").attr("content");

        $.ajax({
            url: `/api/enrollment/${enrolmentData.id}`,
            type: 'PUT',
            contentType: 'application/json',
            data: JSON.stringify(enrolmentData),
            beforeSend: function(xhr) {
                xhr.setRequestHeader(header, token);
            },
            success: function() {
                $('#enrolmentTable').DataTable().ajax.reload();
                editEnrolModal.hide();
                showToast('Success', 'Berhasil update enrollment');
            },
            error: function(xhr) {
                showToast('Error', 'error saat update enrollment');
            }
        });
    });
})

const addEnrollModal = new bootstrap.Modal(document.getElementById('addEnrolmentModal'));

$('#btn-add-enrollment').on('click', function() {
    addEnrollModal.show();
});

const editEnrolModal = new bootstrap.Modal(document.getElementById('editEnrolmentModal'));

function openEditEnrolmentModal(enrolmentId) {
    editEnrolModal.show();
    $('#editEnrolmentId').val(enrolmentId);
}

async function deleteEnrol(enrollId) {
    const token = $("meta[name='_csrf']").attr("content");
    const header = $("meta[name='_csrf_header']").attr("content");
    const result = await showConfirmDialog(
        'Are you sure?',
        "You won't be able to revert this!",
        'Yes, delete it!'
    );
    if (result.isConfirmed) {
        $.ajax({
            url: `/api/enrollment/${enrollId}`,
            type: 'DELETE',
            beforeSend: function(xhr) {
                xhr.setRequestHeader(header, token);
            },
            success: function(result) {
                $('#enrolmentTable').DataTable().ajax.reload();
                showToast("Success", "Berhasil menghapus enrollment.", "success", 5000);
            },
            error: function(xhr) {
                showToast("Error", "error saat menghapus enrollment.", "error", 5000);
            }
        });
    }
}