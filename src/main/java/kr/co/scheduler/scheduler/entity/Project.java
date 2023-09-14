package kr.co.scheduler.scheduler.entity;


import jakarta.persistence.*;
import kr.co.scheduler.global.entity.BaseTimeEntity;
import kr.co.scheduler.user.entity.User;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "tbl_project")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
public class Project extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private Long id;

    @Column
    private String title;

    @Column(length = 1000000, nullable = false)
    private String description;

    @Column
    private String goal;

    @Column
    private LocalDate startPRJ;

    @Column
    private LocalDate endPRJ;

    @ColumnDefault("'N'")
    private String active_yn;

    @ColumnDefault("'N'")
    private String complete_yn;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<Task> tasks;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<TaskLog> taskLogs;

    @Builder
    public Project(String title, String description, String goal, LocalDate startPRJ, LocalDate endPRJ, List<Task> tasks, List<TaskLog> taskLogs, User user) {

        this.title = title;
        this.description = description;
        this.goal = goal;
        this.startPRJ = startPRJ;
        this.endPRJ = endPRJ;
        this.tasks = tasks;
        this.taskLogs = taskLogs;
        this.user = user;
    }

    public void updateProject(String title, String description, String goal, LocalDate startPRJ, LocalDate endPRJ) {

        this.title = title;
        this.description = description;
        this.goal = goal;
        this.startPRJ = startPRJ;
        this.endPRJ = endPRJ;
    }
}
