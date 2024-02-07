package org.sparta.mytaek1.domain.payment.service;

import com.siot.IamportRestClient.IamportClient;
import org.sparta.mytaek1.domain.order.dto.OrderResponseDto;
import org.sparta.mytaek1.domain.order.entity.Orders;
import org.sparta.mytaek1.domain.order.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentService {

    private final IamportClient iamportClient;
    private final OrderRepository orderRepository;

    public PaymentService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
        this.iamportClient = new IamportClient("8474815404642414",
                "iKb4UTArrJKpuSXzivlsLheOgW4NwzVk2pfgxWqKkS4W428lwpwdTp5Xo7cozxjnaw5vJAmIHuvkN6Do");
    }

    @Transactional
    public OrderResponseDto updatePaymentStatus(Long orderId) {
        Orders order = orderRepository.findById(orderId).orElseThrow();
        order.update();
        return new OrderResponseDto(order);
    }
}
