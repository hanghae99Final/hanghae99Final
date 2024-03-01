package org.sparta.mytaek1.domain.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sparta.mytaek1.domain.broadcast.entity.Broadcast;
import org.sparta.mytaek1.domain.order.entity.Orders;
import org.sparta.mytaek1.global.audit.Auditable;
import org.sparta.mytaek1.global.security.UserRoleEnum;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String userEmail;

    @Column(nullable = false)
    private String userPhone;

    @Column(nullable = false)
    private String userAddress;

    @Column(nullable = false)
    private String postcode;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    @Column(unique = true)
    private String streamKey;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Broadcast> broadcastList;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Orders> orderList;

    private String refreshToken;

    public User(String userName, String userEmail, String password, String streamKey, String userPhone, String userAddress, String postcode) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.password = password;
        this.role = UserRoleEnum.valueOf("USER");
        this.streamKey = streamKey;
        this.userPhone = userPhone;
        this.userAddress = userAddress;
        this.postcode = postcode;
    }

    public void updateRefreshToken(String updateRefreshToken) {
        this.refreshToken = updateRefreshToken;
    }
}