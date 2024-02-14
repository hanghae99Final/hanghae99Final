package org.sparta.mytaek1.domain.payment.service;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.siot.IamportRestClient.request.BillingCustomerData;
import com.siot.IamportRestClient.request.CardInfo;
import com.siot.IamportRestClient.request.OnetimePaymentData;
import com.siot.IamportRestClient.response.Payment;
import org.sparta.mytaek1.domain.order.dto.OrderResponseDto;
import org.sparta.mytaek1.domain.order.entity.Orders;
import org.sparta.mytaek1.domain.order.service.OrderService;
import org.sparta.mytaek1.domain.payment.dto.PaymentOnetimeDto;
import org.sparta.mytaek1.domain.payment.dto.PaymentRequestDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;

@Service
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class PaymentService {

    private final OrderService orderService;

    public PaymentService(OrderService orderService) {
        this.orderService = orderService;
    }

    @Transactional
    public OrderResponseDto updatePaymentStatus(Long orderId) {
        Orders order = orderService.findOrderById(orderId);
        order.update();
        return new OrderResponseDto(order);
    }

    public BillingCustomerData getPaymentData(PaymentRequestDto requestDto) throws IOException {
        String cardNumber = requestDto.getCard_number();
        String expiry = requestDto.getExpiry();
        String birth = requestDto.getBirth();
        String pwd2Digit =requestDto.getPwd_2digit();
        BillingCustomerData data = new BillingCustomerData(requestDto.getCustomer_uid(), cardNumber,expiry,birth);
        data.setPwd2Digit(pwd2Digit);
        data.setPg(requestDto.getPg());
//        data.setCustomerName(requestDto.getBuyer_name());
//        data.setCustomerTel(requestDto.getBuyer_tel());
//        data.setCustomerAddr(requestDto.getBuyer_addr());
//        data.setCustomerPostcode(requestDto.getBuyer_postcode());
        return data;
    }

    public OnetimePaymentData getPaymentOnetime(PaymentOnetimeDto paymentOnetimeDto) {
        String merchant_uid = paymentOnetimeDto.getMerchant_uid();
        BigDecimal amount = paymentOnetimeDto.getAmount();
        String cardNumber = paymentOnetimeDto.getCard_number();
        String expiry = paymentOnetimeDto.getExpiry();
        String birth = paymentOnetimeDto.getBirth();
        String pwd2Digit = paymentOnetimeDto.getPwd_2digit();
        CardInfo card =new CardInfo(cardNumber,expiry,birth,pwd2Digit);

        return new OnetimePaymentData(merchant_uid,amount, card);
    }
}
