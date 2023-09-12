package kr.co.scheduler.scheduler.controller;

import kr.co.scheduler.scheduler.service.ProjectService;
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
    public String manageProject(@PathVariable(name = "project_id") Long id, Model model) {

        projectService.calculateTaskPercentage(id);

        model.addAttribute("project", projectService.viewProject(id));
        model.addAttribute("d_day", projectService.countD_day(id));

        return "scheduler/manageSolePRJ";
    }
}
