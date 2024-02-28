package org.sparta.mytaek1.domain.payment.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CancelPayDto {
    private String merchant_uid;
}
