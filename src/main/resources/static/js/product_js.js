const productStock = document.getElementById("productStock").textContent;
const productPrice = document.getElementById("productPrice").textContent;
const buyerProductId = document.getElementById("buyerProductId").value;

function createOrder() {
    const isAuthenticated = checkCookieExistence('Authorization');
    const quantity = parseInt(document.getElementById("quantity").value);
    const totalPrice = parseInt(productPrice) * quantity;

    if (quantity > productStock) {
        alert("주문 수량이 재고 수량을 초과합니다.");
        return;
    }

    const orderRequest = {
        quantity: quantity,
        totalPrice: totalPrice
    };

    if (!isAuthenticated) {
        alert('로그인 후에 주문할 수 있습니다.');
        window.location.href = '/api/user/login-page';
    }

    $.ajax({
        type: 'POST',
        url: '/api/products/' + buyerProductId +'/orders',
        contentType: 'application/json',
        data: JSON.stringify(orderRequest),
        success: function (response) {
            const orderId = response.orderId;
            window.location.href = `/products/${buyerProductId}/orders/${orderId}`;
        },
        error: function (error) {
            console.error(error);
        }
    });
}

function checkCookieExistence(cookieName) {
    return document.cookie.split(';').some((item) => item.trim().startsWith(cookieName + '='));
}
