package org.sparta.mytaek1.domain.payment.service;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.request.BillingCustomerData;
import org.sparta.mytaek1.domain.order.dto.OrderResponseDto;
import org.sparta.mytaek1.domain.order.entity.Orders;
import org.sparta.mytaek1.domain.order.repository.OrderRepository;
import org.sparta.mytaek1.domain.order.service.OrderService;
import org.sparta.mytaek1.domain.payment.dto.PaymentRequestDto;
import org.sparta.mytaek1.domain.product.entity.Product;
import org.sparta.mytaek1.global.message.ErrorMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class PaymentService {

    private final IamportClient iamportClient;
    private final OrderService orderService;
    public PaymentService(OrderService orderService) {
        this.orderService = orderService;
        this.iamportClient = new IamportClient("8474815404642414",
                "iKb4UTArrJKpuSXzivlsLheOgW4NwzVk2pfgxWqKkS4W428lwpwdTp5Xo7cozxjnaw5vJAmIHuvkN6Do");
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
        BillingCustomerData data = new BillingCustomerData(cardNumber + expiry, cardNumber, expiry, birth);
        data.setPg(requestDto.getPg());
        data.setPwd2Digit(requestDto.getPwd_2digit());
        data.setCustomerName(requestDto.getBuyer_name());
        data.setCustomerTel(requestDto.getBuyer_tel());
        data.setCustomerAddr(requestDto.getBuyer_addr());
        data.setCustomerPostcode(requestDto.getBuyer_postcode());
        return data;
    }


}
