package kr.co.scheduler.user.entity;

import jakarta.persistence.*;
import kr.co.scheduler.user.enums.Role;
import kr.co.scheduler.global.entity.BaseTimeEntity;
import lombok.*;

@Entity
@Table(name = "tbl_user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

    @Setter private String profileImgName;
    @Setter private String profileImgPath;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(length = 45, unique = true)
    private String email;

    @Column(length = 100)
    private String password;

    @Column(length = 30)
    private String name;

    @Column(length = 30)
    private String phone;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public User(String email, String password, String name, String phone, Role role) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.role = role;
    }

    public void update(String password, String name, String phone) {
        this.password = password;
        this.name = name;
        this.phone = phone;
    }
}