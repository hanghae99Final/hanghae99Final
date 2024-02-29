package org.sparta.mytaek1.domain.payment.controller;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import jakarta.annotation.PostConstruct;
import org.sparta.mytaek1.domain.order.dto.OrderResponseDto;
import org.sparta.mytaek1.domain.order.service.OrderService;
import org.sparta.mytaek1.domain.payment.dto.CancelPayment;
import org.sparta.mytaek1.domain.payment.dto.ImpUidupdateDto;
import org.sparta.mytaek1.domain.payment.dto.PaymentOnetimeDto;
import org.sparta.mytaek1.domain.payment.service.PaymentService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@RestController
public class PaymentController {

    private final OrderService orderService;
    private final PaymentService paymentService;
    @Value("${iamport.api.key}")
    private String apiKey;

    @Value("${iamport.api.secret.key}")
    private String apiSecretKey;
    private IamportClient iamportClient;

    public PaymentController(OrderService orderService, PaymentService paymentService) {
        this.orderService = orderService;
        this.paymentService = paymentService;
    }
    @PostConstruct
    public void init() {
        this.iamportClient = new IamportClient(apiKey, apiSecretKey);
    }

    @ResponseBody
    @PostMapping("/verify/{imp_uid}")
    public IamportResponse<Payment> paymentByImpUid(@PathVariable("imp_uid") String imp_uid,@RequestBody ImpUidupdateDto impUidupdateDto)
            throws IamportResponseException, IOException {
        orderService.updateMerchant(impUidupdateDto.getOrderId(),impUidupdateDto.getMerchant_uid());
        return iamportClient.paymentByImpUid(imp_uid);
    }

    @PatchMapping("/api/orders/{orderId}/paymentStatus")
    public ResponseEntity<OrderResponseDto> updatePayment(@PathVariable Long orderId) {
        OrderResponseDto orderResponseDto = paymentService.updatePaymentStatus(orderId);
        return ResponseEntity.ok(orderResponseDto);
    }

    @PostMapping("/subscribe/payments/onetime")
    public ResponseEntity<CompletableFuture<IamportResponse<Payment>>> paymentOnetime(@RequestBody PaymentOnetimeDto paymentOnetimeDto)
            throws IamportResponseException, IOException {
        CompletableFuture<IamportResponse<Payment>> response = paymentService.getPaymentOnetime(paymentOnetimeDto);
        orderService.updateMerchant(paymentOnetimeDto.getBuyer_orderId(),paymentOnetimeDto.getMerchant_uid());
        return ResponseEntity.ok(response);
    }
    @PostMapping("/payments/cancel")
    public ResponseEntity<IamportResponse<Payment>> cancelPayment(@RequestBody CancelPayment cancelPayment) throws IamportResponseException, IOException {
        IamportResponse<Payment> response = paymentService.cancelPayment(cancelPayment);
        orderService.cancelPaymentStatus(cancelPayment.getOrder_id());
        return ResponseEntity.ok(response);
    }
}