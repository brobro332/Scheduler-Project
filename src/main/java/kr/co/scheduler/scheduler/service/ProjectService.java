package kr.co.scheduler.scheduler.service;

import kr.co.scheduler.scheduler.dtos.ProjectReqDTO;
import kr.co.scheduler.scheduler.dtos.TaskLogReqDTO;
import kr.co.scheduler.scheduler.dtos.TaskReqDTO;
import kr.co.scheduler.scheduler.entity.Project;
import kr.co.scheduler.scheduler.entity.SubTask;
import kr.co.scheduler.scheduler.entity.Task;
import kr.co.scheduler.scheduler.entity.TaskLog;
import kr.co.scheduler.scheduler.repository.ProjectRepository;
import kr.co.scheduler.scheduler.repository.SubTaskRepository;
import kr.co.scheduler.scheduler.repository.TaskLogRepository;
import kr.co.scheduler.scheduler.repository.TaskRepository;
import kr.co.scheduler.user.entity.User;
import kr.co.scheduler.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final UserService userService;
    private final TaskService taskService;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final SubTaskRepository subTaskRepository;
    private final TaskLogRepository taskLogRepository;

    @Transactional
    public void createProjectPlanner(ProjectReqDTO.CREATE create, List<Task> tasks, List<SubTask> subTasks, String email) {

        User user = userService.findUser(email);

        if (user != null) {

            Project project = Project.builder()
                    .title(create.getTitle())
                    .description(create.getDescription())
                    .goal(create.getGoal())
                    .startPRJ(create.getStartPRJ())
                    .endPRJ(create.getEndPRJ())
                    .user(user)
                    .build();

            for(Task task : tasks) {

                task.setProject(project);
                taskRepository.save(task);
            }

            for (SubTask subTask : subTasks) {

                subTaskRepository.save(subTask);
            }

            project.setTasks(tasks);
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
    public void updateProject(Long id, ProjectReqDTO.UPDATE update) {
        Project project = projectRepository.findById(id).orElse(null);

        if (project != null) {
            project.updateProject(update.getTitle(), update.getDescription(), update.getGoal(), update.getStartPRJ(), update.getEndPRJ());

            for (TaskReqDTO.UPDATE updatedTask : update.getUpdatedTasks()) {
                Task task = taskRepository.findById(Long.parseLong(updatedTask.getIdx())).orElse(null);
                if (task != null) {
                    List<String> updatedSubTasks = updatedTask.getSubTasks(); // 이 부분에서 세부 업무 리스트를 가져옵니다.
                    taskService.updateTask(Long.parseLong(updatedTask.getIdx()), updatedTask.getTask(), updatedSubTasks);
                }
            }

            taskService.addTasks(project, update.getAddedTasks());
            taskService.deleteTasks(update.getDeletedTasks());
        }
    }

    public void deleteProject(Long id) {

        Project project = projectRepository.findById(id).orElse(null);

        if(project != null) {

            projectRepository.delete(project);
        }
    }

    @Transactional
    public void activeProject(Long id) {

        Project project = projectRepository.findById(id).orElse(null);

        if (project != null) {

            if (Objects.equals(project.getActive_yn(), "N")) {

                project.setActive_yn("Y");
            } else {

                project.setActive_yn("N");
            }
        }
    }

    public String countD_day(Long id) {

        Project project = projectRepository.findById(id).orElse(null);

        if(project != null) {

            LocalDate currentDate = LocalDate.now();
            Long d_day = ChronoUnit.DAYS.between(currentDate, project.getEndPRJ());

            if(d_day == 0) {

                return "D-DAY입니다.";
            } else if(d_day > 0) {

                return "프로젝트 마감일까지 D-" + d_day + " 남았습니다.";
            } else {

                return "프로젝트 마감일까지 D+" + d_day + " 지났습니다.";
            }
        }

        return "D-DAY를 계산할 수 없습니다.";
    }

    @Transactional
    public void calculateTaskPercentage(Long id) {

        Project project = projectRepository.findById(id).orElse(null);

        if (project.getTasks() != null) {

            for (Task task : project.getTasks()) {
                int checkedSubTasks = 0;
                int totalSubTasks = task.getSubTasks().size();

                task.setTotalSubTasks(totalSubTasks);

                for (SubTask subTask : task.getSubTasks()) {

                    if (subTask.getCheck_yn().equals("Y")) {

                        checkedSubTasks++; // 체크된 세부 업무 개수 추가
                    }
                }
                task.setCheckedSubTasks(checkedSubTasks);

                if (totalSubTasks > 0) {

                    float taskPercentage = (float) checkedSubTasks / totalSubTasks * 100;

                    DecimalFormat df = new DecimalFormat("#.##");
                    taskPercentage = Float.parseFloat(df.format(taskPercentage));

                    task.setTaskPercentage(taskPercentage);
                } else if (checkedSubTasks == 0 && task.getCheck_yn().equals("Y")) {

                    task.setTaskPercentage(100.0f);
                } else {

                    task.setTaskPercentage(0.0f); // 총 세부 업무가 없을 경우 0%로 설정
                }
            }
        }
    }

    @Transactional
    public void writeTaskLog(TaskLogReqDTO taskLogReqDTO, Long id) {

        Project project = projectRepository.findById(id).orElse(null);

        if (project != null) {

            TaskLog taskLog = TaskLog
                    .builder()
                    .title(taskLogReqDTO.getTitle())
                    .content(taskLogReqDTO.getContent())
                    .taskCategory(taskLogReqDTO.getTaskCategory())
                    .subTaskCategory(taskLogReqDTO.getSubTaskCategory())
                    .project(project)
                    .build();

            List<TaskLog> temp = project.getTaskLogs();
            temp.add(taskLog);
            project.setTaskLogs(temp);

            taskLogRepository.save(taskLog);
        }
    }
}
