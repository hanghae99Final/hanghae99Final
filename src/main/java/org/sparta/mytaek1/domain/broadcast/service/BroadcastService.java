package org.sparta.mytaek1.domain.broadcast.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sparta.mytaek1.domain.broadcast.dto.BroadcastRequestDto;
import org.sparta.mytaek1.domain.broadcast.dto.BroadcastResponseDto;
import org.sparta.mytaek1.domain.broadcast.entity.Broadcast;
import org.sparta.mytaek1.domain.broadcast.repository.BroadcastRepository;
import org.sparta.mytaek1.domain.product.entity.Product;
import org.sparta.mytaek1.domain.product.repository.ProductRepository;
import org.sparta.mytaek1.domain.user.entity.User;
import org.sparta.mytaek1.domain.user.repository.UserRepository;
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
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<BroadcastResponseDto> getAllBroadCast() {
        List<Broadcast> broadcastList = broadcastRepository.findAllByOnAirTrue();
        return BroadcastResponseDto.EntityList(broadcastList);
    }

    public BroadcastResponseDto createBroadcast(long userId, long productId, BroadcastRequestDto requestDto) {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        Product product = optionalProduct.orElseThrow();

        Optional<User> optionalUser = userRepository.findById(userId);
        User user = optionalUser.orElseThrow();
        Broadcast broadCast = requestDto.toEntity(user, product);
        broadcastRepository.save(broadCast);
        return BroadcastResponseDto.createdEntity(broadCast, user, product);
    }

    public BroadcastResponseDto endBroadcast(long broadcastId) {
        Optional<Broadcast> optionalBroadcast = broadcastRepository.findById(broadcastId);
        Broadcast broadCast = optionalBroadcast.orElseThrow();

        broadCast.endBroadcast();
        return BroadcastResponseDto.endBroadcast(broadCast);
    }
}
