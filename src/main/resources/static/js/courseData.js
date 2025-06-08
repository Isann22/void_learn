$(document).ready(function() {
    $('#courseTable').DataTable({
        "ajax": {
            "url": "/api/course",
            dataSrc: ""
        },
        responsive: true,
        "columns": [
            {"data": "id"},
            {"data": "title"},
            {"data": "description"},
            {
                "data": "image",
                "render": function(data, type, row) {
                    if (type === 'display') {
                        return data ?
                                `<div style=" border: 1px padding: 10px; border-radius: 1px; display: flex;justify-content:center;">
                                    <img src="/${data}" class="img-thumbnail" alt="Course Image" style="width:100%;height:50%">
                                  </div>`
                                : '<span class="text-muted" style="border: 1px dashed #ccc; padding: 5px; border-radius: 4px;">No Image</span>'
                    }
                    return data;
                }
            },
            {
                "data": "id",
                "render": function (data, type, row) {
                    return `
                               <button class="btn btn-primary btn-icon-text btn-edit p-2" 
                                    data-id="${row.id}" 
                                    data-title="${row.title}"
                                    data-description="${row.description}"
                      
                                <i class="ti-file btn-icon-append"></i> Edit
                            </button>
                            <button class="btn btn-danger btn-delete p-2" data-id="${data}">Delete
                            <i class="ti-trash btn-icon-append"></i></button>
                        `;
                },
                "orderable": false
            }
        ],
        "columnDefs": [
            {"targets": [0], "visible": false}
        ],
        "initComplete": function() {
            $('#courseTable').on('click', '.btn-edit', function() {
                const courseId = $(this).data('id');
                const title = $(this).data('title');
                const description = $(this).data('description');
                editModalC(courseId,title,description)
            });

            $('#courseTable').on('click', '.btn-delete', function() {
                const courseId= $(this).data('id');
                deleteCourse(courseId)
            });
        }
    });

    $('#image').change(function() {
        const file = this.files[0];
        const preview = $('#imagePreview');

        // Clear previous preview
        preview.empty();

        if (file) {
            // Check if the file is an image
            if (file.type.match('image.*')) {
                const reader = new FileReader();

                reader.onload = function(e) {
                    // Create and append the preview image
                    preview.html(`<img src="${e.target.result}" class="img-thumbnail mt-2" style="max-height: 200px;">`);
                }

                reader.readAsDataURL(file);
            } else {
                preview.html('<div class="alert alert-warning mt-2">Please select a valid image file</div>');
            }
        }
    });

    $('#createCourseForm').submit(function(e) {
        e.preventDefault();

        const name = $('#title').val();
        const description = $('#description').val();
        const image = $('#image')[0].files[0];

        // Validate required fields
        if (!name || !description || !image) {
            showToast("Error", "Please fill all required fields", "error", 5000);
            return;
        }

        const courseData = new FormData();
        courseData.append('title', name);
        courseData.append('description', description);
        courseData.append('image', image);

        // Get CSRF token
        const token = $("meta[name='_csrf']").attr("content");
        const header = $("meta[name='_csrf_header']").attr("content");

        $.ajax({
            url: `/api/course/create`,
            type: 'POST',
            data: courseData,
            processData: false,
            contentType: false,
            beforeSend: function(xhr) {
                xhr.setRequestHeader(header, token);
            },
            success: () => {
                $('#courseTable').DataTable().ajax.reload();
                showToast("Success", "Berhasil menambahkan course", "success", 5000);
                addCourseModal.hide();
                $('#createCourseForm')[0].reset();
                $('#imagePreview').empty();
            },
            error: function(xhr) {
                let errorMessage = "Error creating course";
                if (xhr.responseJSON && xhr.responseJSON.message) {
                    errorMessage = xhr.responseJSON.message;
                }
                console.log(errorMessage);
                showToast("Error", errorMessage, "error", 5000);
            }
        });
    });

    $('#editCourseForm').submit(function(e) {
        e.preventDefault();

        const courseId = $('#editCourseId').val();
        const formData = new FormData();

        const name = $('#etitle').val();
        const description = $('#edescription').val();
        const image = $('#eimage')[0].files[0];

        formData.append('title', name);
        formData.append('description', description);
        formData.append('image', image);

        const token = $("meta[name='_csrf']").attr("content");
        const header = $("meta[name='_csrf_header']").attr("content");
        $.ajax({
            url: `/api/course/${courseId}`,
            type: 'PUT',
            processData: false,
            contentType: false,
            data: formData,
            beforeSend: function(xhr) {
                xhr.setRequestHeader(header, token);
            },
            success: function() {
                editCourseModal.hide();
                $('#courseTable').DataTable().ajax.reload();
                showToast("Success", "Berhasil update course", "success", 5000);
            },
            error: function(xhr) {
                showToast("Error", "Error saat update course.", "error", 5000);
            },
        });
    });

});

const addCourseModal = new bootstrap.Modal(document.getElementById('addCourseModal'));
$('#btn-add-course').on('click', function() {
    addCourseModal.show();
});

const editCourseModal = new bootstrap.Modal(document.getElementById('editCourseModal'));
function editModalC(courseId,title,description) {
    editCourseModal.show();
    $('#editCourseId').val(courseId)
    $('#etitle').val(title);
    $('#edescription').val(description)
}

async function deleteCourse(courseId) {
    const token = $("meta[name='_csrf']").attr("content");
    const header = $("meta[name='_csrf_header']").attr("content");
    const result = await showConfirmDialog(
        'Are you sure?',
        "You won't be able to revert this!",
        'Yes, delete it!'
    );
    if (result.isConfirmed) {
        $.ajax({
            url: `/api/course/${courseId}`,
            type: 'DELETE',
            beforeSend: function(xhr) {
                xhr.setRequestHeader(header, token);
            },
            success: function(result) {
                $('#courseTable').DataTable().ajax.reload();
                showToast("Success", "Berhasil menghapus course.", "success", 5000);
            },
            error: function(xhr) {
                showToast("Error", "error saat menambahkan user.", "error", 5000);
            }
        });
    }
}