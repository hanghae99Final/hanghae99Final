package org.sparta.mytaek1.domain.payment.service;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CardInfo;
import com.siot.IamportRestClient.request.OnetimePaymentData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.sparta.mytaek1.domain.order.dto.OrderResponseDto;
import org.sparta.mytaek1.domain.order.entity.Orders;
import org.sparta.mytaek1.domain.order.service.OrderService;
import org.sparta.mytaek1.domain.payment.dto.PaymentOnetimeDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class PaymentService {

    private final OrderService orderService;
    private IamportClient iamportClient;
    @Value("${iamport.api.key}")
    private String apiKey;
    @Value("${iamport.api.secret.key}")
    private String apiSecretKey;

    public PaymentService(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostConstruct
    public void init() {
        this.iamportClient = new IamportClient(apiKey, apiSecretKey);
    }

    @Transactional
    public OrderResponseDto updatePaymentStatus(Long orderId) {
        Orders order = orderService.findOrderById(orderId);
        order.update();
        return new OrderResponseDto(order);
    }

    @Async
    public CompletableFuture<IamportResponse<Payment>> getPaymentOnetime(PaymentOnetimeDto paymentOnetimeDto) throws IOException, IamportResponseException {
        String merchant_uid = paymentOnetimeDto.getMerchant_uid();
        BigDecimal amount = paymentOnetimeDto.getAmount();
        String cardNumber = paymentOnetimeDto.getCard_number();
        String expiry = paymentOnetimeDto.getExpiry();
        String birth = paymentOnetimeDto.getBirth();
        String pwd2Digit = paymentOnetimeDto.getPwd_2digit();
        CardInfo card =new CardInfo(cardNumber,expiry,birth,pwd2Digit);

        OnetimePaymentData data = new OnetimePaymentData(merchant_uid,amount, card);
        data.setCustomer_uid(paymentOnetimeDto.getCustomer_uid());
        data.setPg(paymentOnetimeDto.getPg());
        data.setBuyerName(paymentOnetimeDto.getBuyer_name());
        data.setBuyerEmail(paymentOnetimeDto.getBuyer_email());
        data.setBuyerTel(paymentOnetimeDto.getBuyer_tel());
        data.setBuyerAddr(paymentOnetimeDto.getBuyer_addr());
        data.setBuyerPostcode(paymentOnetimeDto.getBuyer_postcode());

        IamportResponse<Payment> payment = iamportClient.onetimePayment(data);
        return CompletableFuture.completedFuture(payment);
    }
}
