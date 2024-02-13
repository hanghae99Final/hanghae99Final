package org.sparta.mytaek1.domain.user.dto;

import lombok.Getter;
import org.sparta.mytaek1.domain.broadcast.entity.Broadcast;
import org.sparta.mytaek1.domain.order.entity.Orders;
import org.sparta.mytaek1.domain.user.entity.User;

import java.util.List;

@Getter
public class UserResponseDto {

    private final Long userId;
    private final String userName;
    private final String userEmail;
    private final String role;
    private final String streamKey;
    private final List<Broadcast> broadcastList;
    private final List<Orders> orderList;

    public UserResponseDto(User user) {
        this.userId = user.getUserId();
        this.userName = user.getUserName();
        this.userEmail = user.getUserEmail();
        this.role = user.getRole().getAuthority();
        this.streamKey = user.getStreamKey();
        this.broadcastList = user.getBroadcastList();
        this.orderList = user.getOrderList();
    }
}
