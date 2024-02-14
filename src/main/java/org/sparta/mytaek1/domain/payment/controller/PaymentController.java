package org.sparta.mytaek1.domain.payment.controller;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.BillingCustomerData;
import com.siot.IamportRestClient.response.BillingCustomer;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import jakarta.annotation.PostConstruct;
import org.sparta.mytaek1.domain.order.dto.OrderResponseDto;
import org.sparta.mytaek1.domain.payment.dto.PaymentRequestDto;
import org.sparta.mytaek1.domain.payment.service.PaymentService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class PaymentController {

    private final PaymentService paymentService;
    @Value("${iamport.api.key}")
    private String apiKey;

    @Value("${iamport.api.secret.key}")
    private String apiSecretKey;
    private IamportClient iamportClient;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostConstruct
    public void init() {
        this.iamportClient = new IamportClient(apiKey, apiSecretKey);
    }

    @ResponseBody
    @PostMapping("/verify/{imp_uid}")
    public IamportResponse<Payment> paymentByImpUid(@PathVariable("imp_uid") String imp_uid)
            throws IamportResponseException, IOException {
        return iamportClient.paymentByImpUid(imp_uid);
    }

    @PutMapping("/api/orders/{orderId}/paymentStatus")
    public ResponseEntity<OrderResponseDto> updatePayment(@PathVariable Long orderId) {
        OrderResponseDto orderResponseDto = paymentService.updatePaymentStatus(orderId);
        return ResponseEntity.ok(orderResponseDto);
    }

    @PostMapping("/subscriptions/issue-billing")
    public ResponseEntity<IamportResponse<BillingCustomer>> paymentByImpUid(@RequestBody PaymentRequestDto requestDto)
            throws IamportResponseException, IOException {
        BillingCustomerData data = paymentService.getPaymentData(requestDto);
        String customerUid = requestDto.getCustomer_uid();
        IamportResponse<BillingCustomer> response = iamportClient.postBillingCustomer(customerUid, data);
        return ResponseEntity.ok(response);
    }
}