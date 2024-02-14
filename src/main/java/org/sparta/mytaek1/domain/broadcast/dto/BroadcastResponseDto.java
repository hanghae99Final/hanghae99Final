package org.sparta.mytaek1.domain.broadcast.dto;

import lombok.Getter;
import org.sparta.mytaek1.domain.broadcast.entity.Broadcast;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class BroadcastResponseDto {

    private final long broadcastId;
    private final String broadcastTitle;
    private final String broadcastDescription;
    private final String userName;
    private final String productName;

    public BroadcastResponseDto(long broadcastId, String broadcastTitle, String broadcastDescription, String userName, String productName) {
        this.broadcastId = broadcastId;
        this.broadcastTitle = broadcastTitle;
        this.broadcastDescription = broadcastDescription;
        this.userName = userName;
        this.productName = productName;
    }

    public BroadcastResponseDto(Broadcast broadcast) {
        this.broadcastId = broadcast.getBroadcastId();
        this.broadcastTitle = broadcast.getBroadcastTitle();
        this.broadcastDescription = broadcast.getBroadcastDescription();
        this.userName = broadcast.getUser().getUserName();
        this.productName = broadcast.getProduct().getProductName();
    }

    public static List<BroadcastResponseDto> fromBroadcastList(List<Broadcast> broadcastList) {
        return broadcastList.stream()
                .map(BroadcastResponseDto::new)
                .collect(Collectors.toList());
    }
}
