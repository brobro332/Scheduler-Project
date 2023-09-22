package kr.co.scheduler.global.entity;

import jakarta.persistence.*;
import kr.co.scheduler.user.entity.User;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tbl_alert")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Alert extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alert_id")
    private Long id;

    @Column(name = "alert_content")
    private String content;

    @OneToMany(mappedBy = "alert")
    private List<AlertUser> alertUsers = new ArrayList<>();

    @Builder
    public Alert(String content, User user) {

        this.content = content;
    }
}
