package kr.co.scheduler.global.entity;

import jakarta.persistence.*;
import kr.co.scheduler.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_alert_user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AlertUser extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alert_user_id")
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "alert_id")
    private Alert alert;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public AlertUser(Alert alert, User user) {
        this.alert = alert;
        this.user = user;
    }
}
