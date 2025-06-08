$(document).ready(function() {
    $('#userTable').DataTable({
        "ajax": {
            "url": "/api/user",
            "dataSrc": ""
        },
        "columns": [
            { "data": "id" },
            { "data": "name" },
            { "data": "username" },
            {
                "data": "email",
                "render": function(data) {
                    return `<a href="mailto:${data}">${data}</a>`;
                }
            },
            {
                "data": "role",
                "render": function(data) {
                    // Add CSS class based on role
                    const roleClass = 'role-' + data.toLowerCase();
                    return `<span class="${roleClass}">${data}</span>`;
                }
            },
            {
                "data": "id",

                "render": function(data, type, row) {
                    return `
  
                               <button class="btn btn-primary btn-icon-text btn-edit p-2" 
                                    data-id="${row.id}" 
                                    data-name="${row.name}"
                                    data-username="${row.username}"
                                    data-email="${row.email}"
                                    data-role="${row.role}">
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
            { "targets": [0], "visible": false }
        ],
        "initComplete": function() {
            // Add event listeners after table is initialized
            $('#userTable').on('click', '.btn-edit', function() {
                const userId = $(this).data('id');
                const email = $(this).data('email');
                const name =$(this).data('name');
                const username =$(this).data('username')
                editUser(userId,email,name,username);
            });

            $('#userTable').on('click', '.btn-delete', function() {
                const userId = $(this).data('id');

                deleteUser(userId);
            });
        }
    });

    $('#editUserForm').submit(function(e) {
        e.preventDefault();

        const userId = $('#editUserId').val();
        const userData = {
            email: $('#editEmail').val(),
            name: $('#editName').val(),
            username: $('#editUsername').val()
        };

        const token = $("meta[name='_csrf']").attr("content");
        const header = $("meta[name='_csrf_header']").attr("content");

        $.ajax({
            url: `/api/user/${userId}`,
            type: 'PUT',
            contentType: 'application/json',
            data: JSON.stringify(userData),
            beforeSend: function(xhr) {
                xhr.setRequestHeader(header, token);
            },
            success: function() {
                editModal.hide();
                $('#userTable').DataTable().ajax.reload();
                showToast("Success", "Berhasil update user", "success", 5000);
            },
            error: function(xhr) {
                showToast("Error", "Error saat update user.", "error", 5000);
            },
        });
    });

    $('#createUserForm').submit(function(e) {
        e.preventDefault();


        const userData = {
            name: $('#name').val(),
            username: $('#username').val(),
            email: $('#email').val(),
            password:$('#password').val()
        };

        const token = $("meta[name='_csrf']").attr("content");
        const header = $("meta[name='_csrf_header']").attr("content");


        $.ajax({
            url: `/api/user/create`,
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(userData),
            beforeSend: function(xhr) {
                xhr.setRequestHeader(header, token);
            },
            success: function() {
                $('#userTable').DataTable().ajax.reload();
                addModal.hide()
                showToast("Success", "Berhasil menambahkan admin", "success", 5000);
                $('#createUserForm')[0].reset();
            },
            error: function(xhr) {
                showToast("Error", "error saat menambahkan user.", "error", 5000);
            },
        });
    });
});




const addModal = new bootstrap.Modal(document.getElementById('addAdminModal'));
$('#btn-modal').on('click', function() {
    addModal.show();
});

const editModal = new bootstrap.Modal(document.getElementById('editUserModal'));
function editUser(userId,email,name,username) {
    editModal.show();
    $('#editUserId').val(userId);
    $('#editEmail').val(email)
    $('#editName').val(name);
    $('#editUsername').val(username);
}



async function deleteUser(userId) {
    const token = $("meta[name='_csrf']").attr("content");
    const header = $("meta[name='_csrf_header']").attr("content");
    const result = await showConfirmDialog(
        'Are you sure?',
        "You won't be able to revert this!",
        'Yes, delete it!'
    );
    if (result.isConfirmed) {
        $.ajax({
            url: `/api/user/${userId}`,
            type: 'DELETE',
            beforeSend: function(xhr) {
                xhr.setRequestHeader(header, token);
            },
            success: function(result) {
                $('#userTable').DataTable().ajax.reload();
                showToast("Success", "Berhasil menghapus user.", "success", 5000);
            },
            error: function(xhr) {
                showToast("Error", "error saat menambahkan user.", "error", 5000);
            }
        });
    }
}
