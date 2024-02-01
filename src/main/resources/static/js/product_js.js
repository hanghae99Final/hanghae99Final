const productName = document.getElementById("productName").textContent;
const productPrice = document.getElementById("productPrice").textContent;
const buyerEmail = document.getElementById("buyerEmail").value;
const buyerName = document.getElementById("buyerName").value;
const buyerTel = document.getElementById("buyerTel").value;
const buyerAddr = document.getElementById("buyerAddr").value;
const buyerPostcode = document.getElementById("buyerPostcode").value;

const IMP = window.IMP;
IMP.init("imp66770306");

function requestPay() {
    IMP.request_pay({
        pg: "kakaopay",
        tid: "8559342fd00616b5ff4b2faaea74ca65",
        pay_method: "card",
        merchant_uid: "ORD20180131-0000011",
        name: productName,
        amount: parseInt(productPrice),
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
                alert("결제 성공");
            } else {
                alert("결제 실패");
            }
        });
    });
}