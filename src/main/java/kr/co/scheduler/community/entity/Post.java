package kr.co.scheduler.community.entity;

import jakarta.persistence.*;
import kr.co.scheduler.global.entity.BaseTimeEntity;
import kr.co.scheduler.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import java.util.List;

@Entity
@Getter
@Table(name = "tbl_post")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Lob
    @Column(length = 1000000, nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ColumnDefault("0")
    private int view_cnt;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<Comment> comments;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<Reply> replies;

    @Builder
    public Post(String title, String content, User user) {

        this.title = title;
        this.content = content;
        this.user = user;
    }

    public void updatePost(String title, String content) {

        this.title = title;
        this.content = content;
    }
}
