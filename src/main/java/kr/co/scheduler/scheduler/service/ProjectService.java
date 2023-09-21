package kr.co.scheduler.scheduler.service;

import kr.co.scheduler.global.entity.Alert;
import kr.co.scheduler.global.entity.AlertUser;
import kr.co.scheduler.global.repository.AlertUserRepository;
import kr.co.scheduler.global.service.FCMService;
import kr.co.scheduler.scheduler.dtos.ProjectReqDTO;
import kr.co.scheduler.scheduler.dtos.TaskReqDTO;
import kr.co.scheduler.scheduler.entity.Project;
import kr.co.scheduler.scheduler.entity.SubTask;
import kr.co.scheduler.scheduler.entity.Task;
import kr.co.scheduler.scheduler.repository.ProjectRepository;
import kr.co.scheduler.scheduler.repository.SubTaskRepository;
import kr.co.scheduler.scheduler.repository.TaskRepository;
import kr.co.scheduler.user.entity.User;
import kr.co.scheduler.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
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
    private final FCMService fcmService;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final SubTaskRepository subTaskRepository;
    private final AlertUserRepository alertUserRepository;

    /**
     * selectPRJPlanner: 프로젝트 플래너 조회 및 리턴
     */
    public Project selectPRJPlanner(Long id) {

        return projectRepository.findById(id)
                .orElseThrow(()->{

                    return new IllegalArgumentException("해당 프로젝트가 존재하지 않습니다.");
                });
    }
    
    /**
     * createPRJPlanner: 프로젝트 플래너 등록
     */
    @Transactional
    public void createPRJPlanner(ProjectReqDTO.CREATE create, List<Task> tasks, List<SubTask> subTasks, String email) {

        User user = userService.selectUser(email);

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

    /**
     * updatePRJPlanner: 프로젝트 플래너 수정
     */
    @Transactional
    public void updatePRJPlanner(Long id, ProjectReqDTO.UPDATE update) {
        Project project = projectRepository.findById(id).orElse(null);

        if (project != null) {
            project.updateProject(update.getTitle(), update.getDescription(), update.getGoal(), update.getStartPRJ(), update.getEndPRJ());

            for (TaskReqDTO.UPDATE updatedTask : update.getUpdatedTasks()) {
                Task task = taskRepository.findById(Long.parseLong(updatedTask.getIdx())).orElse(null);
                if (task != null) {
                    List<String> updatedSubTasks = updatedTask.getSubTasks(); // 이 부분에서 세부 업무 리스트를 가져옵니다.
                    taskService.updateTasks(Long.parseLong(updatedTask.getIdx()), updatedTask.getTask(), updatedSubTasks);
                }
            }

            taskService.createTasks(project, update.getAddedTasks());
            taskService.deleteTasks(update.getDeletedTasks());
        }
    }

    /**
     * deleteProject: 프로젝트 플래너 삭제
     */
    @Transactional
    public void deleteProject(Long id) {

        Project project = projectRepository.findById(id).orElse(null);

        if(project != null) {

            projectRepository.delete(project);
        }
    }

    /**
     * activePRJPlanner: 프로젝트 플래너를 활성화
     */
    @Transactional
    public void activePRJPlanner(Long id) {

        Project project = projectRepository.findById(id).orElse(null);

        if (project != null) {

            if (Objects.equals(project.getCompleteYn(), "Y")) {

                throw new IllegalArgumentException("이미 완료된 프로젝트입니다.");
            }

            if (Objects.equals(project.getActiveYn(), "N")) {

                project.setActiveYn("Y");
            } else {

                project.setActiveYn("N");
            }
        }
    }

    @Transactional
    public void endPRJPlanner(Long id) {

        Project project = projectRepository.findById(id).orElse(null);

        if (project != null) {

            if (Objects.equals(project.getCompleteYn(), "N")) {

                project.setCompleteYn("Y");
                project.setActiveYn("N");
            }
        }
    }

    
    // ================================== 구분 ================================== //

    /**
     * selectPRJPlanners: 프로젝트 플래너에 대한 Page 객체를 리턴
     */
    public Page<Project> selectPRJPlanners(Pageable pageable, String email) {

        User user = userService.selectUser(email);

        return projectRepository.findPageByUser_Id(pageable, user.getId());
    }

    // ================================== 구분 ================================== //

    /**
     * countPRJPlanners: 업무 달성률 계산을 위해 프로젝트 플래너의 총 개수를 카운트
     */
    public Long countPRJPlanners(String email) {

        User user = userService.selectUser(email);

        return projectRepository.countByUser(user);
    }

    /**
     * countActivePRJPlanners: 활성화된 프로젝트 플래너 개수 리턴
     */
    public Long countActivePRJPlanners(String email) {

        User user = userService.selectUser(email);

        return projectRepository.countByUserAndActiveYn(user, Character.toString('Y'));
    }

    /**
     * countCompletedPRJPlanners: 완료된 프로젝트 플래너 개수 리턴
     */
    public Long countCompletedPRJPlanners(String email) {

        User user = userService.selectUser(email);

        return projectRepository.countByUserAndCompleteYn(user, Character.toString('Y'));
    }

    /**
     * calculateTaskPercentage: 업무 달성률 계산
     */
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

    /**
     * calculateTotalPercentage: 프로젝트 달성률 계산
     */
    @Transactional
    public Float calculateTotalPercentage(Long id) {

        Project project = projectRepository.findById(id).orElse(null);
        List<Task> tasks = project.getTasks();

        float sum = 0;
        float result = 0;

        if (tasks != null) {

            for (Task task : tasks) {

                sum += task.getTaskPercentage();
            }

            DecimalFormat df = new DecimalFormat("#.##");
            result = Float.parseFloat(df.format(sum / tasks.size()));
        }

        return result;
    }
    
    /**
     * countD_day: 프로젝트 마감일까지 남은 날짜를 계산
     */
    public Long countD_day(Long id) {

        Project project = projectRepository.findById(id).orElse(null);

        if(project != null) {

            LocalDate currentDate = LocalDate.now();
            Long d_day = ChronoUnit.DAYS.between(currentDate, project.getEndPRJ());

            return d_day;
        } else {

            throw new IllegalArgumentException("D-Day 를 계산할 수 없습니다.");
        }
    }

    // ================================== 구분 ================================== //

    /**
     * completePRJOverTheDeadline: 마감일이 지난 프로젝트를 완료된 상태로 전환
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void completePRJOverTheDeadline() {

        List<Project> projects1 = projectRepository.findByCompleteYn(Character.toString('N'));

        for (Project project : projects1) {

            Long D_day = countD_day(project.getId());
            if (D_day != null) {

                if (D_day < 0) {

                    project.setCompleteYn(Character.toString('Y'));
                }
            }
        }
    }

    /**
     * sendFCMMessageToWriter: 스케줄러를 통해 매일 0시 정각에 마감일이 다가온 프로젝트 사용자에게 웹 푸시 전달
     */
    @Scheduled(cron = "30 10 15 * * *")
    public void sendFCMMessageAndAlertToWriter() {

        List<Project> projects2 = projectRepository.findByActiveYn(Character.toString('Y'));

        for (Project project : projects2) {

            Long D_day = countD_day(project.getId());
            if (D_day != null) {

                if (D_day <= 7) {

                    String targetToken = project.getUser().getTargetToken();
                    String body = project.getUser().getName()
                            + "님의 플래너 "
                            + project.getTitle()
                            + " 만기일이 "
                            + D_day
                            + "일 남았습니다.";

                    if (targetToken != null) {

                        fcmService.sendFCMMessage(targetToken, "SPAP 스케줄러", body);
                    }

                Alert alert = Alert
                        .builder()
                        .content(body)
                        .build();

                User user = project.getUser();

                AlertUser alertUser = AlertUser
                        .builder()
                        .alert(alert)
                        .user(user)
                        .build();

                alertUserRepository.save(alertUser);
                }
            }
        }
    }
}
