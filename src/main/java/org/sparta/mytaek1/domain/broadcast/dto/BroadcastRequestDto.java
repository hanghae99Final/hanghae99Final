package org.sparta.mytaek1.domain.broadcast.dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BroadcastRequestDto {

    @NonNull
    private final String broadcastTitle;
    @NonNull
    private final String broadcastDescription;
    @NonNull
    private final String productName;
    @NonNull
    private final String productDescription;
    @NonNull
    private final int productPrice;
    @NonNull
    private final int productStock;
}
