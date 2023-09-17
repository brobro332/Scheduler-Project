package kr.co.scheduler.scheduler.controller;

import kr.co.scheduler.scheduler.entity.Project;
import kr.co.scheduler.scheduler.entity.TaskLog;
import kr.co.scheduler.scheduler.service.ProjectService;
import kr.co.scheduler.scheduler.service.TaskLogService;
import kr.co.scheduler.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class ProjectController {

    private final UserService userService;
    private final ProjectService projectService;
    private final TaskLogService taskLogService;

    /**
     * createSolePRJPlanner: 개인 프로젝트 플래너 등록 페이지 리턴
     */
    @GetMapping("/scheduler/createPRJPlanner")
    public String createPRJPlanner() {

        return "scheduler/createPRJPlanner";
    }

    /**
     * selectPRJPlanners: 사용자의 프로젝트 플래너 목록 조회 페이지 리턴
     */
    @GetMapping("/scheduler/selectPRJPlanners")
    public String selectPRJPlanners(Model model, @PageableDefault(size = 3, sort="updatedAt",
                                      direction = Sort.Direction.DESC) Pageable pageable, Principal principal) {

        model.addAttribute("projects", projectService.selectPRJPlanners(pageable, principal.getName()));
        model.addAttribute("info", userService.searchInfo(principal.getName()));
        model.addAttribute("count", projectService.countPRJPlanners(principal.getName()));

        return "scheduler/selectPRJPlanners";
    }

    /**
     * selectPRJPlanner: 사용자의 프로젝트 플래너 조회 페이지 리턴
     */
    @GetMapping("/scheduler/selectPRJPlanner/{project_id}")
    public String selectPRJPlanner(@PathVariable(name = "project_id") Long id, Model model, Principal principal) {

        Project project = projectService.selectPRJPlanner(id);

        if (project.getUser() == userService.selectUser(principal.getName())) {

            model.addAttribute("project", project);
        }

        return "scheduler/selectPRJPlanner";
    }

    /**
     * updatePRJPlanner: 사용자의 프로젝트 플래너 수정 페이지 리턴 
     */
    @GetMapping("/scheduler/updatePRJPlanner/{project_id}")
    public String updatePRJPlanner(@PathVariable(name = "project_id") Long id, Model model, Principal principal) {

        Project project = projectService.selectPRJPlanner(id);

        if (project.getUser() == userService.selectUser(principal.getName())) {

            model.addAttribute("project", project);
        }

        return "scheduler/updatePRJPlanner";
    }

    /**
     * managePRJPlanner: 사용자의 프로젝트 플래너 관리 페이지 리턴
     */
    @GetMapping("/scheduler/managePRJPlanner/{project_id}")
    public String managePRJPlanner(@PathVariable(name = "project_id") Long id, Model model,
                                @PageableDefault(size = 5, sort="createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                                   Principal principal) {

        Project project = projectService.selectPRJPlanner(id);

        if (project.getUser() == userService.selectUser(principal.getName())) {

            projectService.calculateTaskPercentage(id);

            model.addAttribute("project", projectService.selectPRJPlanner(id));
            model.addAttribute("d_day", projectService.countD_day(id));
            model.addAttribute("taskLogs", taskLogService.selectTaskLog(pageable, id));
        }

        return "scheduler/managePRJPlanner";
    }

    // ================================== 구분 ================================== //

    /**
     * selectTaskLog: 사용자의 업무일지 조회 페이지 리턴
     */
    @GetMapping("/scheduler/managePRJPlanner/{project_id}/selectTaskLog/{task_log_id}")
    public String selectTaskLog(@PathVariable(name = "project_id") Long project_id, @PathVariable(name = "task_log_id") Long task_log_id, Model model, Principal principal) {

        Project project = projectService.selectPRJPlanner(project_id);
        TaskLog taskLog = taskLogService.selectTaskLog(task_log_id);

        if (project.getUser() == userService.selectUser(principal.getName())) {

            model.addAttribute("taskLog", taskLog);
        }

        return "scheduler/selectTaskLog";
    }

    /**
     * updateTaskLog: 사용자의 업무일지 수정 페이지 리턴
     */
    @GetMapping("/scheduler/managePRJPlanner/{project_id}/updateTaskLog/{task_log_id}")
    public String updateTaskLog(@PathVariable(name = "project_id") Long project_id,
                                @PathVariable(name = "task_log_id") Long task_log_id,
                                Model model, Principal principal) {

        Project project = projectService.selectPRJPlanner(project_id);
        TaskLog taskLog = taskLogService.selectTaskLog(task_log_id);

        if (project.getUser() == userService.selectUser(principal.getName())) {

            model.addAttribute("project", project);
            model.addAttribute("taskLog", taskLog);
        }

        return "scheduler/updateTaskLog";
    }

}
