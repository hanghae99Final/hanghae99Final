package org.sparta.mytaek1.domain.broadcast.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sparta.mytaek1.domain.broadcast.dto.BroadcastRequestDto;
import org.sparta.mytaek1.domain.broadcast.dto.BroadcastResponseDto;
import org.sparta.mytaek1.domain.broadcast.entity.Broadcast;
import org.sparta.mytaek1.domain.broadcast.repository.BroadcastRepository;
import org.sparta.mytaek1.domain.product.entity.Product;
import org.sparta.mytaek1.domain.product.service.ProductService;
import org.sparta.mytaek1.domain.user.entity.User;
import org.sparta.mytaek1.domain.user.repository.UserRepository;
import org.sparta.mytaek1.global.message.ErrorMessage;
import org.sparta.mytaek1.global.security.UserDetailsImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BroadcastService {


    private final BroadcastRepository broadcastRepository;
    private final UserRepository userRepository;
    private final ProductService productService;

    @Transactional(readOnly = true)
    public List<BroadcastResponseDto> getAllBroadCast() {
        List<Broadcast> broadcastList = broadcastRepository.findAllByOnAirTrue();
        return BroadcastResponseDto.fromBroadcastList(broadcastList);
    }

    public BroadcastResponseDto createBroadcast(UserDetailsImpl auth, BroadcastRequestDto requestDto) {
        Optional<User> optionalUser = userRepository.findByUserEmail(auth.getUsername());
        User user = optionalUser.orElseThrow(() -> new IllegalArgumentException(ErrorMessage.NOT_EXIST_USER_ERROR_MESSAGE.getErrorMessage()));

        Product product = productService.createProduct(requestDto);

        Broadcast broadcast = new Broadcast(requestDto.getBroadcastTitle(), requestDto.getBroadcastDescription(), user, product);
        broadcastRepository.save(broadcast);
        return new BroadcastResponseDto(broadcast);
    }

    public BroadcastResponseDto endBroadcast(long broadcastId) {
        Broadcast broadCast = getBroadcastByBroadcastId(broadcastId);

        broadCast.endBroadcast();
        BroadcastResponseDto responseDto = new BroadcastResponseDto(broadCast);
        return responseDto;
    }

    @Transactional(readOnly = true)
    public Broadcast getBroadcastByBroadcastId(Long broadcastId) {
        return broadcastRepository.findByBroadcastId(broadcastId)
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.NOT_EXIST_BROADCAST_ERROR_MESSAGE.getErrorMessage()));
    }
}
