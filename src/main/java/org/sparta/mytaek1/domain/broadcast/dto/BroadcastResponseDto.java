package org.sparta.mytaek1.domain.broadcast.dto;

import lombok.Builder;
import lombok.Getter;
import org.sparta.mytaek1.domain.broadcast.entity.Broadcast;
import org.sparta.mytaek1.domain.product.entity.Product;
import org.sparta.mytaek1.domain.user.entity.User;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class BroadcastResponseDto {

    private final Long broadCastId;

    private final String broadCastTitle;

    private final String broadCastDescription;

    private final boolean onAir;

    private final String userName;

    private final String productName;

    @Builder
    public BroadcastResponseDto(Long broadCastId, String broadCastTitle, String broadCastDescription, boolean onAir, String userName, String productName) {
        this.broadCastId = broadCastId;
        this.broadCastTitle = broadCastTitle;
        this.broadCastDescription = broadCastDescription;
        this.onAir = onAir;
        this.userName = userName;
        this.productName = productName;
    }

    public static BroadcastResponseDto createdEntity(Broadcast broadCast, User user, Product product) {
        return BroadcastResponseDto.builder()
                .broadCastId(broadCast.getBroadcastId())
                .broadCastTitle(broadCast.getBroadcastTitle())
                .broadCastDescription(broadCast.getBroadcastDescription())
                .onAir(broadCast.isOnAir())
                .userName(user.getUserName())
                .productName(product.getProductName())
                .build();
    }

    public static BroadcastResponseDto endBroadcast(Broadcast broadCast) {
        return BroadcastResponseDto.builder()
                .broadCastId(broadCast.getBroadcastId())
                .broadCastTitle(broadCast.getBroadcastTitle())
                .broadCastDescription(broadCast.getBroadcastDescription())
                .onAir(broadCast.isOnAir())
                .userName(broadCast.getUser().getUserName())
                .productName(broadCast.getProduct().getProductName())
                .build();
    }

    public static List<BroadcastResponseDto> EntityList(List<Broadcast> broadcastList) {
        return broadcastList.stream()
                .map(BroadcastResponseDto::endBroadcast)
                .collect(Collectors.toList());
    }
}
