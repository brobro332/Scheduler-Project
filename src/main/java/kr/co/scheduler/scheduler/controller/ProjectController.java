package kr.co.scheduler.scheduler.controller;

import kr.co.scheduler.scheduler.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping("/scheduler/create")
    public String createSoleProject() {

        return "scheduler/createSolePRJ";
    }

    @GetMapping("/scheduler/view")
    public String viewProject(Model model, @PageableDefault(size = 6, sort="updatedAt",
                                      direction = Sort.Direction.DESC) Pageable pageable, Principal principal) {

        model.addAttribute("projects", projectService.viewProject(pageable, principal.getName()));

        return "scheduler/viewPRJ";
    }
}
