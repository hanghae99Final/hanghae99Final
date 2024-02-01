package org.sparta.mytaek1.domain.broadcast.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sparta.mytaek1.domain.broadcast.entity.Broadcast;
import org.sparta.mytaek1.domain.broadcast.repository.BroadcastRepository;
import org.sparta.mytaek1.domain.user.entity.User;
import org.sparta.mytaek1.domain.user.repository.UserRepository;
import org.sparta.mytaek1.domain.user.service.UserService;
import org.sparta.mytaek1.global.message.ErrorMessage;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BroadcastService {
    private final BroadcastRepository broadcastRepository;
    private final UserRepository userRepository;

    public Broadcast getBroadcastByBroadcastId(Long broadcastId) {
        return broadcastRepository.findByBroadcastId(broadcastId)
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.NOT_EXIST_BROADCAST_ERROR_MESSAGE.getErrorMessage()));
    }
}
