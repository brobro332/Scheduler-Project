package kr.co.scheduler.user.entity;

import jakarta.persistence.*;
import kr.co.scheduler.community.entity.Comment;
import kr.co.scheduler.community.entity.Post;
import kr.co.scheduler.community.entity.Reply;
import kr.co.scheduler.global.entity.AlertUser;
import kr.co.scheduler.user.enums.Role;
import kr.co.scheduler.global.entity.BaseTimeEntity;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tbl_user")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

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

    @Setter
    private String profileImgName;

    @Setter
    private String profileImgPath;

    private String oauth;

    private String targetToken;

    private LocalDate lastLoggedDay;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<AlertUser> alertUsers = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Reply> replies = new ArrayList<>();

    @Builder
    public User(String email, String password, String name, String phone, Role role, String oauth) {

        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.role = role;
        this.oauth = oauth;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateInfo(String name, String phone) {

        this.name = name;
        this.phone = phone;
    }
}