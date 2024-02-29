package org.sparta.mytaek1.domain.payment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Getter
@NoArgsConstructor
public class CancelPayment {
    private String merchant_uid;
    private Long order_id;
}
