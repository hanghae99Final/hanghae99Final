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
import org.sparta.mytaek1.domain.user.service.UserService;
import org.sparta.mytaek1.global.message.ErrorMessage;
import org.sparta.mytaek1.global.security.UserDetailsImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BroadcastService {

    private final BroadcastRepository broadcastRepository;
    private final ProductService productService;

    @Transactional(readOnly = true)
    public List<BroadcastResponseDto> getAllBroadCast() {
        List<Broadcast> broadcastList = broadcastRepository.findAllByOnAirTrue();
        return BroadcastResponseDto.fromBroadcastList(broadcastList);
    }




    public void createBroadcast(UserDetailsImpl auth, BroadcastRequestDto requestDto) {
        if(checkUserOnAir(auth.getId())){
            throw new IllegalArgumentException(ErrorMessage.EXIST_ONAIR_BROADCAST_ERROR_MESSAGE.getErrorMessage());
        }

        User user = auth.getUser();
        Product product = productService.createProduct(requestDto);

        Broadcast broadcast = new Broadcast(requestDto.getBroadcastTitle(), requestDto.getBroadcastDescription(), user, product);
        broadcastRepository.save(broadcast);
    }

    public void endBroadcast(long broadcastId) {
        Broadcast broadCast = getBroadcastByBroadcastId(broadcastId);

        broadCast.endBroadcast();
    }

    public List<Broadcast> findBroadcastListByUserId(Long userId) {
        return broadcastRepository.findAllByUserUserId(userId);
    }

    @Transactional(readOnly = true)
    public Broadcast getBroadcastByBroadcastId(Long broadcastId) {
        return broadcastRepository.findByBroadcastId(broadcastId)
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.NOT_EXIST_BROADCAST_ERROR_MESSAGE.getErrorMessage()));
    }

    public Boolean checkUserOnAir(Long userId) {
        return broadcastRepository.existsByUserIdAndOnAir(userId, true);
    }
}
