package kr.co.scheduler.community.entity;

import jakarta.persistence.*;
import kr.co.scheduler.global.entity.BaseTimeEntity;
import kr.co.scheduler.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.util.List;

@Entity
@Getter
@Table(name = "tbl_post")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Column
    private String title;

    @Lob
    @Column(length = 1000000)
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ColumnDefault("0")
    private String view_cnt;

    @Column
    private List<String> postImgPath;

    @Column
    private List<String> postImgName;

    @Builder
    public Post(String title, String content, User user) {

        this.title = title;
        this.content = content;
        this.user = user;
    }
}
