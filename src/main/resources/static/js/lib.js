
function showToast(title, message, type, duration) {
    $.toast({
        title: title,
        message: message,
        type: type,
        duration: duration
    });
}

function showConfirmDialog(title, text, confirmButtonText = 'Yes') {
    return Swal.fire({
        title: title,
        text: text,
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: confirmButtonText,
        cancelButtonText: 'Cancel'
    });
}