const productStock = document.getElementById("productStock").textContent;
const productPrice = document.getElementById("productPrice").textContent;
const buyerProductId = document.getElementById("buyerProductId").value;

function createOrder() {
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