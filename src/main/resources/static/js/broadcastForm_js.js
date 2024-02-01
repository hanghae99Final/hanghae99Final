    $(document).ready(function () {
        // 폼 제출 이벤트를 가로채서 submitForm 함수를 호출합니다.
        $("form").submit(function (event) {
            event.preventDefault(); // 폼의 기본 제출 방지
            submitForm(); // submitForm 함수 호출
        });
    });

function submitForm() {
    // 폼 데이터를 JSON 형식으로 변환합니다.
    var formData = {
        broadcastTitle: $("#broadcastTitle").val(),
        broadcastDescription: $("#broadcastDescription").val(),
        productName: $("#productName").val(),
        productDescription: $("#productDescription").val(),
        productPrice: $("#productPrice").val(),
        productStock: $("#productStock").val()
    };

    // 서버에 데이터를 전송합니다.
    $.ajax({
        type: "POST",
        url: "/api/broadcasts",
        contentType: "application/json",
        data: JSON.stringify(formData),
        success: function(response) {
            // 성공적으로 전송되었을 때의 처리
            console.log(response);
             window.location.href = "/";
        },
        error: function(error) {
            // 전송 중 오류가 발생했을 때의 처리
            console.error(error);
        }
    });
}