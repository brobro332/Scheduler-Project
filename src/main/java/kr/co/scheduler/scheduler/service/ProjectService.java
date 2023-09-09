package kr.co.scheduler.scheduler.service;

import kr.co.scheduler.scheduler.dtos.ProjectReqDTO;
import kr.co.scheduler.scheduler.dtos.TaskReqDTO;
import kr.co.scheduler.scheduler.entity.Project;
import kr.co.scheduler.scheduler.entity.Task;
import kr.co.scheduler.scheduler.repository.ProjectRepository;
import kr.co.scheduler.scheduler.repository.TaskRepository;
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
    private final TaskRepository taskRepository;

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

    public Page<Project> viewProjects(Pageable pageable, String email) {

        User user = userService.findUser(email);

        return projectRepository.findPageByUser_Id(pageable, user.getId());
    }

    public Project viewProject(Long id) {

        return projectRepository.findById(id)
                .orElseThrow(()->{
                   return new IllegalArgumentException("해당 프로젝트가 존재하지 않습니다.");
                });
    }

    public Long countPRJ(String email) {

        User user = userService.findUser(email);

        return projectRepository.countByUser(user);
    }

    @Transactional
    public void updateTask(Long id, TaskReqDTO.UPDATE update) {
        // 업무 업데이트 로직 작성
        Task task = taskRepository.findById(id).orElse(null);

        if(task != null) {
            task = Task
                    .builder()
                    .idx(update.getTask())
                    .task(update.getTask())
                    .build();
        }
    }

    @Transactional
    public void addTask(Long id, TaskReqDTO.CREATE create) {
        // 업무 추가 로직 작성
        Project project = projectRepository.findById(id).orElse(null);

        Task task = Task
                .builder()
                .task(create.getTask())
                .idx(create.getIdx())
                .project(project)
                .build();

        taskRepository.save(task);
    }


    public Project updateProject(Long id, ProjectReqDTO.UPDATE update) {

}
