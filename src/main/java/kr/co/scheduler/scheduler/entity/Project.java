package kr.co.scheduler.scheduler.entity;


import jakarta.persistence.*;
import kr.co.scheduler.global.entity.BaseTimeEntity;
import kr.co.scheduler.user.entity.User;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tbl_project")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Project extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private Long id;

    @Column
    private String title;

    @Column
    private String description;

    @Column
    private String goal;

    @Column
    private LocalDate startPRJ;

    @Column
    private LocalDate endPRJ;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<Task> tasks;

    @Builder
    public Project(String title, String description, String goal, LocalDate startPRJ, LocalDate endPRJ, List<Task> tasks, User user) {

        this.title = title;
        this.description = description;
        this.goal = goal;
        this.startPRJ = startPRJ;
        this.endPRJ = endPRJ;
        this.tasks = tasks;
        this.user = user;
    }
}
