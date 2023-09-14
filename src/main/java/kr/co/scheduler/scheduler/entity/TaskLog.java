package kr.co.scheduler.scheduler.entity;

import jakarta.persistence.*;
import kr.co.scheduler.global.entity.BaseTimeEntity;
import lombok.*;

@Entity
@Table(name = "tbl_task_log")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TaskLog extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_log_id")
    private Long id;

    @Column(name = "task_log_title")
    private String title;

    @Column(name = "task_log_content", length = 1000000, nullable = false)
    private String content;

    @Column
    private String taskCategory;

    @Column
    private String subTaskCategory;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @Builder
    public TaskLog(String title, String content, String taskCategory, String subTaskCategory, Project project) {

        this.title = title;
        this.content = content;
        this.taskCategory = taskCategory;
        this.subTaskCategory = subTaskCategory;
        this.project = project;
    }
}
