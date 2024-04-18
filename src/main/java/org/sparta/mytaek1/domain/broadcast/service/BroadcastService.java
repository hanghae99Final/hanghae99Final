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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class BroadcastService {

    private final BroadcastRepository broadcastRepository;
    private final ProductService productService;
    private final UserService userService;

    @Transactional(readOnly = true)
    public Page<BroadcastResponseDto> getAllBroadCast(Pageable pageable) {
        Page<Broadcast> broadcastPage  = broadcastRepository.findAllByOnAirTrue(pageable);
        return broadcastPage.map(broadcast -> new BroadcastResponseDto(
                broadcast.getBroadcastId(),
                broadcast.getBroadcastTitle(),
                broadcast.getBroadcastDescription(),
                broadcast.getUser().getUserName(),
                broadcast.getProduct().getProductName(),
                broadcast.getProduct().getImageUrl()
        ));
    }

    @Transactional
    public void createBroadcast(UserDetails auth, BroadcastRequestDto requestDto, MultipartFile imageFile) {
        User user = userService.findByUserEmail(auth.getUsername());

        if (checkUserOnAir(user.getUserId())) {
            throw new IllegalArgumentException(ErrorMessage.EXIST_ON_AIR_BROADCAST_ERROR_MESSAGE.getErrorMessage());
        }

        Product product = productService.createProduct(requestDto, imageFile);
        Broadcast broadcast = new Broadcast(requestDto.getBroadcastTitle(), requestDto.getBroadcastDescription(), user, product);
        broadcastRepository.save(broadcast);
    }

    @Transactional
    public void endBroadcast(long broadcastId) {
        Broadcast broadCast = getBroadcastByBroadcastId(broadcastId);
        broadCast.endBroadcast();
    }

    @Transactional(readOnly = true)
    public Broadcast getBroadcastByBroadcastId(Long broadcastId) {
        return broadcastRepository.findByBroadcastId(broadcastId)
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.NOT_EXIST_BROADCAST_ERROR_MESSAGE.getErrorMessage()));
    }

    public Boolean checkUserOnAir(Long userId) {
        return broadcastRepository.existsByUserIdAndOnAir(userId, true);
    }

    public Page<Broadcast> findBroadcastListByUserId(Long userId, Pageable pageable) {
        return broadcastRepository.findAllByUserUserId(userId,pageable);
    }
}
