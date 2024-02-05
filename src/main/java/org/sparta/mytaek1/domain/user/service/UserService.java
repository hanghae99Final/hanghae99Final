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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String PASSWORD_PATTERN = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,15}$";
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void createUser(UserRequestDto requestDto) {
        String userName = requestDto.getUserName();

        String userEmail = requestDto.getUserEmail();
        checkEmailPattern(userEmail);
        checkDuplicatedUserEmail(userEmail);

        String password = passwordEncoder.encode(requestDto.getPassword());
        checkPasswordPattern(password);

        String streamKey = UUID.randomUUID() + userEmail.split("@")[0];
        String userPhone = requestDto.getUserPhone();
        String userAddress = requestDto.getUserAddress();
        String postcode = requestDto.getPostcode();

        User user =  userRepository.save(new User(userName, userEmail, password, streamKey, userPhone, userAddress, postcode));
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

    private void checkDuplicatedUserEmail(String userEmail) {
        Optional<User> checkEmail = userRepository.findByUserEmail(userEmail);

        if (checkEmail.isPresent()) {
            throw new IllegalArgumentException(ErrorMessage.DUPLICATED_EMAIL_ERROR_MESSAGE.getErrorMessage());
        }
    }

    private void checkEmailPattern(String email) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            throw new IllegalArgumentException(ErrorMessage.EMAIL_FORMAT_ERROR_MESSAGE.getErrorMessage());
        }
    }

    private void checkPasswordPattern(String password) {
        Pattern passwordPattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher passwordMatcher = passwordPattern.matcher(password);
        if (!passwordMatcher.matches()) {
            throw new IllegalArgumentException(ErrorMessage.PASSWORD_VALIDATION_ERROR_MESSAGE.getErrorMessage());
        }
    }
}

