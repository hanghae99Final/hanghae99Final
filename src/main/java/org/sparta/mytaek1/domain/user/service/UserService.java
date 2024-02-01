package org.sparta.mytaek1.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.sparta.mytaek1.domain.broadcast.entity.Broadcast;
import org.sparta.mytaek1.domain.order.entity.Orders;
import org.sparta.mytaek1.domain.user.dto.UserRequestDto;
import org.sparta.mytaek1.domain.user.dto.UserResponseDto;
import org.sparta.mytaek1.domain.user.entity.User;
import org.sparta.mytaek1.domain.user.repository.UserRepository;
import org.sparta.mytaek1.global.message.ErrorMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void createUser(UserRequestDto requestDto) {
        String userName = requestDto.getUserName();
        checkDuplicatedUserName(userName);

        String userEmail = requestDto.getUserEmail();
        checkDuplicatedUserEmail(userEmail);

        String password = passwordEncoder.encode(requestDto.getPassword());
        String streamKey = UUID.randomUUID().toString();
        User user =  userRepository.save(new User(userName, userEmail, password, streamKey));
        new UserResponseDto(user);
    }

    public List<Broadcast> getBroadcasts(Long userId) {
        User user = findUser(userId);
        return new ArrayList<>(user.getBroadcastList());
    }

    public List<Orders> getOrders(Long userId) {
        User user = findUser(userId);
        return new ArrayList<>(user.getOrderList());
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException(ErrorMessage.NOT_EXIST_USER_ERROR_MESSAGE.getErrorMessage()));
    }

    private void checkDuplicatedUserName(String userName) {
        Optional<User> checkUsername = userRepository.findByUserName(userName);

        if (checkUsername.isPresent()) {
            throw new IllegalArgumentException(ErrorMessage.DUPLICATED_USER__ERROR_MESSAGE.getErrorMessage());
        }
    }

    private void checkDuplicatedUserEmail(String userEmail) {
        Optional<User> checkEmail = userRepository.findByUserEmail(userEmail);

        if (checkEmail.isPresent()) {
            throw new IllegalArgumentException(ErrorMessage.DUPLICATED_EMAIL_ERROR_MESSAGE.getErrorMessage());
        }
    }
}

