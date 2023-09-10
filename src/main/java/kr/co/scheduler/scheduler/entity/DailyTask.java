package kr.co.scheduler.scheduler.entity;

import jakarta.persistence.*;
import kr.co.scheduler.global.entity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tbl_daily_task")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DailyTask extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "daily_task_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;
}
