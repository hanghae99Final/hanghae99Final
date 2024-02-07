const productName = document.getElementById("orderProductPrice").textContent;
const productPrice = document.getElementById("orderProductPrice").textContent;
const buyerEmail = document.getElementById("orderEmail").textContent;
const buyerName = document.getElementById("orderName").textContent;
const buyerTel = document.getElementById("orderPhone").textContent;
const buyerAddr = document.getElementById("orderAddress").textContent;
const buyerPostcode = document.getElementById("orderPostcode").textContent;
const buyerProductId = document.getElementById("buyerProductId").value;
const buyerUserId = document.getElementById("buyerUserId").value;

const IMP = window.IMP;
IMP.init("imp66770306");

function generateMerchantUid() {
    const now = new Date();
    const timestamp = now.getTime();
    return "ORD" + timestamp + "-" + buyerProductId.toString().padStart(4, '0') + buyerUserId.toString().padStart(4, '0');
}

function requestPay() {
    const isAuthenticated = checkCookieExistence('Authorization');
    const orderContainer = document.querySelector('.order_container');
    const orderId = orderContainer.getAttribute('data-order-id');


    if (isAuthenticated) {
        const quantity = parseInt(document.getElementById("orderQuantity").textContent);
        const amount = parseInt(productPrice) * quantity;
        const merchant_uid = generateMerchantUid();

        IMP.request_pay({
            pg: "kakaopay",
            tid: "8559342fd00616b5ff4b2faaea74ca65",
            pay_method: "card",
            merchant_uid: merchant_uid,
            name: productName,
            amount: amount,
            buyer_email: buyerEmail,
            buyer_name: buyerName,
            buyer_tel: buyerTel,
            buyer_addr: buyerAddr,
            buyer_postcode: buyerPostcode
        }, function (rsp) {
            $.ajax({
                type: 'POST',
                url: '/verify/' + rsp.imp_uid
            }).done(function(data) {
                if(rsp.paid_amount === data.response.amount){
                    $.ajax({
                        type: 'PUT',
                        url: `/api/orders/${orderId}/paymentStatus`
                    })
                    alert("결제 성공");
                    window.location.href="/my-page";
                } else {
                    alert("결제 실패");
                }
            });
        });
    } else {
        alert('로그인 후에 결제할 수 있습니다.');
        window.location.href = '/api/user/login-page';
    }
}

function checkCookieExistence(cookieName) {
    return document.cookie.split(';').some((item) => item.trim().startsWith(cookieName + '='));
}