package kr.co.scheduler.scheduler.entity;

import jakarta.persistence.*;
import kr.co.scheduler.global.entity.BaseTimeEntity;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import java.util.List;

@Entity
@Table(name = "tbl_task")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Task extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Long id;

    @Column
    private String idx;

    @Column
    private String task;

    @Column
    private Float taskPercentage;

    @Column
    private Integer checkedSubTasks;

    @Column
    private Integer totalSubTasks;

    @Column
    private String check_yn;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    private List<SubTask> subTasks;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @Builder
    public Task(String idx, String task, Project project, String check_yn) {

        this.idx = idx;
        this.task = task;
        this.project = project;
        this.check_yn = check_yn;
    }

    public void updateTask(String idx, String task) {

        this.idx = idx;
        this.task = task;
    }
}
