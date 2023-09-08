package kr.co.scheduler.scheduler.service;

import kr.co.scheduler.scheduler.dtos.ProjectReqDTO;
import kr.co.scheduler.scheduler.entity.Project;
import kr.co.scheduler.scheduler.entity.Task;
import kr.co.scheduler.scheduler.repository.ProjectRepository;
import kr.co.scheduler.user.entity.User;
import kr.co.scheduler.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserService userService;

    @Transactional
    public void createProjectPlanner(ProjectReqDTO.CREATE create, List<Task> taskList, String email) {

        User user = userService.findUser(email);

        if (user != null) {

            Project project = Project.builder()
                    .title(create.getTitle())
                    .description(create.getDescription())
                    .goal(create.getGoal())
                    .startPRJ(create.getStartPRJ())
                    .endPRJ(create.getEndPRJ())
                    .tasks(taskList)
                    .user(user)
                    .build();

            for(Task task : taskList) {

                task.setProject(project);
            }

            projectRepository.save(project);
        }
    }

    public Page<Project> viewProject(Pageable pageable, String email) {

        User user = userService.findUser(email);

        return projectRepository.findPageByUser_Id(pageable, user.getId());
    }
}
