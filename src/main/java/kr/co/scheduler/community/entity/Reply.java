package kr.co.scheduler.community.entity;

import jakarta.persistence.*;
import kr.co.scheduler.global.entity.BaseTimeEntity;
import kr.co.scheduler.user.entity.User;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import java.util.List;

@Entity
@Getter
@Table(name = "tbl_reply")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
public class Reply extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reply_id")
    private Long id;

    @Column(nullable = false)
    private String reply;

    @Setter
    @ColumnDefault("'N'")
    private String delete_yn;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @ManyToOne
    @JoinColumn(name = "parent_reply_id")
    private Reply parentReply;

    @OneToMany(mappedBy = "parentReply")
    private List<Reply> childReplies;

    @Builder
    public Reply(User user, String reply, Comment comment, Post post, Reply parentReply) {

        this.user = user;
        this.reply = reply;
        this.post = post;
        this.comment = comment;
        this.parentReply = parentReply;
    }

    public void updateReply(String reply) {

        this.reply = reply;
    }
}
