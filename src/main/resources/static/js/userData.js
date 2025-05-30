$(document).ready(function() {
    $('#userTable').DataTable({
        "ajax": {
            "url": "/api/data",
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
});



const editModal = new bootstrap.Modal(document.getElementById('editUserModal'));
const addModal = new bootstrap.Modal(document.getElementById('addAdminModal'));

$('#btn-modal').on('click', function() {
    addModal.show();
});

function editUser(userId,email,name,username) {
    editModal.show();
    $('#editUserId').val(userId);
    $('#editEmail').val(email)
    $('#editName').val(name);
    $('#editUsername').val(username);
}

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
        url: `/api/data/${userId}`,
        type: 'PUT',
        contentType: 'application/json',
        data: JSON.stringify(userData),
        beforeSend: function(xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function() {
            editModal.hide();
            $('#userTable').DataTable().ajax.reload();
            $.toast({
                title:"Success",
                message:"Berhasil mengubah user.",
                type:"success",
                duration: 5000,
            });
        },
        error: function(xhr) {
            $.toast({
                title:"Error",
                message:"error saat menghapus user.",
                type:"error",
                duration: 3000,
            });
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
        url: `/api/data/create`,
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(userData),
        beforeSend: function(xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function() {
            $('#userTable').DataTable().ajax.reload();
            addModal.hide()
            $.toast({
                title:"Success",
                message:"Berhasil menambahkan user.",
                type:"success",
                duration: 5000,
            });
        },
        error: function(xhr) {
            $.toast({
                title:"Error",
                message:"error saat menambahkan user.",
                type:"error",
                duration: 3000,
            });
        },
    });
});


function deleteUser(userId) {
    const token = $("meta[name='_csrf']").attr("content");
    const header = $("meta[name='_csrf_header']").attr("content");
    if (confirm('Are you sure you want to delete this user?')) {
        // AJAX call to delete the user
        $.ajax({
            url: `/api/data/${userId}`,
            type: 'DELETE',
            beforeSend: function(xhr) {
                xhr.setRequestHeader(header, token);
            },
            success: function(result) {
                $('#userTable').DataTable().ajax.reload();
                $.toast({
                    title:"Success",
                    message:"Berhasil menghapus user.",
                    type:"success",
                    duration: 5000,
                });
            },
            error: function(xhr) {
                $.toast({
                    title:"Error",
                    message:"error saat menghapus user.",
                    type:"error",
                    duration: 3000,
                });
            }
        });
    }
}