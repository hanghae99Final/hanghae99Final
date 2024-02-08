$(document).ready(function () {
    $("form").submit(function (event) {
        event.preventDefault();
        submitForm();
    });
});

function submitForm() {
    const formData = {
        broadcastTitle: $("#broadcastTitle").val(),
        broadcastDescription: $("#broadcastDescription").val(),
        productName: $("#productName").val(),
        productDescription: $("#productDescription").val(),
        productPrice: $("#productPrice").val(),
        productStock: $("#productStock").val()
    };

    $.ajax({
        type: "POST",
        url: "/api/broadcasts",
        contentType: "application/json",
        data: JSON.stringify(formData),
        success: function(response) {
            console.log(response);
            window.location.href = "/";
        },
        error: function(error) {
            console.error(error);
        }
    });
}