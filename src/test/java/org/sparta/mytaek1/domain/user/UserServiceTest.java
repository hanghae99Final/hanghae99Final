package org.sparta.mytaek1.domain.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sparta.mytaek1.domain.user.dto.UserRequestDto;
import org.sparta.mytaek1.domain.user.entity.User;
import org.sparta.mytaek1.domain.user.repository.UserRepository;
import org.sparta.mytaek1.domain.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
@Rollback
class UserServiceTest {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceTest(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Test
    @DisplayName("회원가입 테스트")
    void testCreateUser() {
        UserService userService = new UserService(userRepository, passwordEncoder);
        UserRequestDto requestDto = new UserRequestDto(
                "dabomi",
                "dabomi@email.com",
                "asdf1234!",
                "000-0000-0000",
                "Paju, Korea",
                "10003"
        );

        userService.createUser(requestDto);

        User savedUser = userRepository.findByUserEmail("dabomi@email.com").orElseThrow();
        assertNotNull(savedUser);
        assertEquals("dabomi", savedUser.getUserName());
        assertEquals("dabomi@email.com", savedUser.getUserEmail());
        assertEquals("000-0000-0000", savedUser.getUserPhone());
        assertEquals("Paju, Korea", savedUser.getUserAddress());
        assertEquals("10003", savedUser.getPostcode());
    }
}