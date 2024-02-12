package org.sparta.mytaek1.domain.payment.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PaymentRequestDto {
    private String pg;
    private String merchant_uid;
    private Integer amount;
    private String card_number;
    private String expiry;
    private String birth;
    private String pwd_2digit;
    private String buyer_email;
    private String buyer_name;
    private String buyer_tel;
    private String buyer_addr;
    private String buyer_postcode;
}
