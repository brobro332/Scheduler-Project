package kr.co.scheduler.scheduler.entity;

import jakarta.persistence.*;
import kr.co.scheduler.global.entity.BaseTimeEntity;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "tbl_task")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
public class Task extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Long id;

    @Column
    private String idx;

    @Column
    private String task;

    @ColumnDefault("'N'")
    private String complete_yn;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    private List<SubTask> subTasks;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @Builder
    public Task(String idx, String task, Project project) {

        this.idx = idx;
        this.task = task;
        this.project = project;
    }

    public void updateTask(String idx, String task) {

        this.idx = idx;
        this.task = task;
    }
}
