$(document).ready(function() {
    $('#enrolform').submit(function (e) {
        e.preventDefault();
        const formData = {
            userId: $('#userId').val(),
            courseId: $('#courseId').val()
        };

        const token = $("meta[name='_csrf']").attr("content");
        const header = $("meta[name='_csrf_header']").attr("content");

        // Show loading state
        const submitBtn = $(this).find('[type="submit"]');
        const originalBtnText = submitBtn.html();
        submitBtn.prop('disabled', true).html(`
            <span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>
            Memproses...
        `);


        $.ajax({
            url: '/course/enrollment',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(formData),
            beforeSend: function (xhr) {
                xhr.setRequestHeader(header, token);
            },
            success: function (data) {
                $('#enrolform')[0].reset();
                showToast("Success", "Berhasil enroll course", "success", 5000);
                setTimeout(()=> {
                    window.location.href = '/user/my-courses';
                }, 3000);
            },
            error: function(xhr) {
                let errorMessage = "Error creating course";
                submitBtn.prop('disabled', false).html(originalBtnText);
                showToast("Error",  xhr.responseJSON?.message || 'error saat enroll coba lagi nanti', "error", 5000);
            }
        })
    });
});