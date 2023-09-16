package kr.co.scheduler.scheduler.controller;

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

    private final ProjectService projectService;
    private final UserService userService;
    private final TaskLogService taskLogService;

    @GetMapping("/scheduler/create")
    public String createSoleProject() {

        return "scheduler/createSolePRJ";
    }

    @GetMapping("/scheduler/view")
    public String viewProjects(Model model, @PageableDefault(size = 3, sort="updatedAt",
                                      direction = Sort.Direction.DESC) Pageable pageable, Principal principal) {

        model.addAttribute("projects", projectService.viewProjects(pageable, principal.getName()));
        model.addAttribute("info", userService.searchInfo(principal.getName()));
        model.addAttribute("count", projectService.countPRJ(principal.getName()));

        return "scheduler/viewPRJs";
    }

    @GetMapping("/scheduler/view/project/{project_id}")
    public String viewProject(@PathVariable(name = "project_id") Long id, Model model) {

        model.addAttribute("project", projectService.viewProject(id));

        return "scheduler/project";
    }

    @GetMapping("/scheduler/update/project/{project_id}")
    public String updateProject(@PathVariable(name = "project_id") Long id, Model model) {

        model.addAttribute("project", projectService.viewProject(id));

        return "scheduler/updateSolePRJ";
    }

    @GetMapping("/scheduler/manage/project/{project_id}")
    public String manageProject(@PathVariable(name = "project_id") Long id, Model model,
                                @PageableDefault(size = 5, sort="createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        projectService.calculateTaskPercentage(id);

        model.addAttribute("project", projectService.viewProject(id));
        model.addAttribute("d_day", projectService.countD_day(id));
        model.addAttribute("taskLogs", projectService.viewTaskLog(pageable, id));

        return "scheduler/manageSolePRJ";
    }

    @GetMapping("/scheduler/manage/project/taskLog/{task_log_id}")
    public String selectTaskLog(@PathVariable(name = "task_log_id") Long id, Model model) {

        model.addAttribute("taskLog", taskLogService.selectTaskLog(id));

        return "scheduler/taskLog";
    }

    @GetMapping("/scheduler/manage/project/{project_id}/taskLog/updateForm/{task_log_id}")
    public String TaskLogUpdateForm(@PathVariable(name = "project_id") Long project_id, @PathVariable(name = "task_log_id") Long task_log_id, Model model) {

        model.addAttribute("project", projectService.viewProject(project_id));
        model.addAttribute("taskLog", taskLogService.selectTaskLog(task_log_id));

        return "scheduler/taskLogUpdateForm";
    }

}
