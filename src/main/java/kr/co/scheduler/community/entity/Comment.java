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

@Entity
@Getter
@Table(name = "tbl_comment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Column(nullable = false)
    private String comment;

    @ColumnDefault("'N'")
    private String delete_yn;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @Builder
    public Comment(User user, String comment, Post post) {

        this.user = user;
        this.comment = comment;
        this.post = post;
    }

    public void updateComment(String comment) {

        this.comment = comment;
    }
}
