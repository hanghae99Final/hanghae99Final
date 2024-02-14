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

function requestNicePay() {
    const isAuthenticated = checkCookieExistence('Authorization');
    const orderContainer = document.querySelector('.order_container');
    const orderId = orderContainer.getAttribute('data-order-id');

    if (isAuthenticated) {
        const quantity = parseInt(document.getElementById("orderQuantity").textContent);
        const totalPrice = parseInt(productPrice) * quantity;
        const merchant_uid = generateMerchantUid();

        IMP.request_pay({
            pg : 'kcp.AO09C',
            pay_method: "card",
            merchant_uid: merchant_uid,
            name: productName,
            amount: totalPrice,
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
                if(data.response.status === "paid"){
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

async function requestCardPay() {
    const isAuthenticated = checkCookieExistence('Authorization');
    const orderContainer = document.querySelector('.order_container');
    const orderId = orderContainer.getAttribute('data-order-id');
    
    if (isAuthenticated) {
        const quantity = parseInt(document.getElementById("orderQuantity").textContent);
        const totalPrice = parseInt(productPrice) * quantity;
        const merchant_uid = generateMerchantUid();
        const card_number = document.getElementById("cardNumber").value;
        const expiry = document.getElementById("cardExpiry").value;
        const pwd_2digit = document.getElementById("cardPassword").value;
        const birth = document.getElementById("userBirth").value;
        const customer_uid = card_number + expiry;
        const pg = "nice.iamport01m";

        const requestBody = JSON.stringify({
            pg :pg,
            merchant_uid:merchant_uid,
            amount: totalPrice,
            card_number: card_number,
            expiry: expiry,
            birth: birth,
            pwd_2digit: pwd_2digit,
            customer_uid: customer_uid,
            buyer_email: buyerEmail,
            buyer_name: buyerName,
            buyer_tel: buyerTel,
            buyer_addr: buyerAddr,
            buyer_postcode: buyerPostcode
        });

        const response = await fetch("/subscribe/payments/onetime", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: requestBody
        });

        if (!response.ok) {
            const errorMessage = await response.text();
            throw new Error(`Failed to issue billing key: ${errorMessage}`);
        }

        const responseData = await response.json();
        alert("빌링키 발급 성공");

        if (responseData.code === 0) {
            $.ajax({
                type: 'PUT',
                url: `/api/orders/${orderId}/paymentStatus`
            })
            alert("결제 성공");
            window.location.href="/my-page";
        } else {
            alert("결제 실패: " + requestBody);
        }

    } else {
        alert('로그인 후에 결제할 수 있습니다.');
        window.location.href = '/api/user/login-page';
    }
}

function checkCookieExistence(cookieName) {
    return document.cookie.split(';').some((item) => item.trim().startsWith(cookieName + '='));
}
