package org.sparta.mytaek1.domain.payment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ImpUidUpdateDto {
    private String merchant_uid;
    private Long orderId;
}
