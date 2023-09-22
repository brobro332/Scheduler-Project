package kr.co.scheduler.scheduler.entity;

import jakarta.persistence.*;
import kr.co.scheduler.global.entity.BaseTimeEntity;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Table(name = "tbl_sub_task")
@Getter
@Setter
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SubTask extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sub_task_id")
    private Long id;

    @Column
    private String name;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    @ColumnDefault("'N'")
    private String check_yn;

    @Builder
    public SubTask(String name, Task task, String check_yn) {
        this.name = name;
        this.task = task;
        this.check_yn = check_yn;
    }
}