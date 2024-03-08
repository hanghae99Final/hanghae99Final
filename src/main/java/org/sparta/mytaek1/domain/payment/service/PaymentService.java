package org.sparta.mytaek1.domain.payment.service;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.request.CardInfo;
import com.siot.IamportRestClient.request.OnetimePaymentData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.sparta.mytaek1.domain.order.dto.OrderResponseDto;
import org.sparta.mytaek1.domain.order.entity.Orders;
import org.sparta.mytaek1.domain.order.service.OrderService;
import org.sparta.mytaek1.domain.payment.dto.CancelPayment;
import org.sparta.mytaek1.domain.payment.dto.PaymentOnetimeDto;
import org.sparta.mytaek1.global.message.ErrorMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
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
    public void processAsync(PaymentOnetimeDto paymentOnetimeDto) throws IamportResponseException, IOException {
        IamportResponse<Payment> payment = getPaymentOnetime(paymentOnetimeDto);
        orderService.updateMerchant(paymentOnetimeDto.getBuyer_orderId(), paymentOnetimeDto.getMerchant_uid());
        checkFailedPayment(payment);

        if (payment.getCode() == 0) {
            updatePaymentStatus(paymentOnetimeDto.getBuyer_orderId());
        }
    }

    @Transactional
    public OrderResponseDto updatePaymentStatus(Long orderId) {
        Orders order = orderService.findOrderById(orderId);
        order.update();
        return new OrderResponseDto(order);
    }

    public IamportResponse<Payment> getPaymentOnetime(PaymentOnetimeDto paymentOnetimeDto) throws IOException, IamportResponseException {
        CardInfo card =new CardInfo(
                paymentOnetimeDto.getCard_number(),
                paymentOnetimeDto.getExpiry(),
                paymentOnetimeDto.getBirth(),
                paymentOnetimeDto.getPwd_2digit()
        );
        OnetimePaymentData data = new OnetimePaymentData(
                paymentOnetimeDto.getMerchant_uid(),
                paymentOnetimeDto.getAmount(),
                card
        );

        data.setCustomer_uid(paymentOnetimeDto.getCustomer_uid());
        data.setPg(paymentOnetimeDto.getPg());
        data.setBuyerName(paymentOnetimeDto.getBuyer_name());
        data.setBuyerEmail(paymentOnetimeDto.getBuyer_email());
        data.setBuyerTel(paymentOnetimeDto.getBuyer_tel());
        data.setBuyerAddr(paymentOnetimeDto.getBuyer_addr());
        data.setBuyerPostcode(paymentOnetimeDto.getBuyer_postcode());

        return iamportClient.onetimePayment(data);
    }

    public IamportResponse<Payment> cancelPayment(CancelPayment cancelPayment) throws IamportResponseException, IOException {
        return iamportClient.cancelPaymentByImpUid(new CancelData(cancelPayment.getMerchant_uid(), false));
    }

    private void checkFailedPayment(IamportResponse<Payment> payment) {
        if (payment.getCode() != 0) {
            throw new IllegalArgumentException(payment.getMessage());
        }
    }
}
