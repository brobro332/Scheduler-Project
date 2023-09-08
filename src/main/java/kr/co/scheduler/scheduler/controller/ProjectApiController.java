package kr.co.scheduler.scheduler.controller;

import kr.co.scheduler.global.dtos.ResponseDto;
import kr.co.scheduler.scheduler.dtos.ProjectReqDTO;
import kr.co.scheduler.scheduler.entity.Project;
import kr.co.scheduler.scheduler.entity.Task;
import kr.co.scheduler.scheduler.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/scheduler/project")
public class ProjectApiController {

    private final ProjectService projectService;

    @PostMapping("/create")
    public ResponseDto<?> createScheduler(@RequestBody ProjectReqDTO.CREATE create, Principal principal) {
        try {
            List<Task> taskList = new ArrayList<>();

            for (Map.Entry<String, String> entry : create.getJsonData().entrySet()) {
                Task task = Task
                        .builder()
                        .idx(entry.getKey())
                        .task(entry.getValue())
                        .build();
                taskList.add(task);
            }

            projectService.createProjectPlanner(create, taskList, principal.getName());

            // 수신한 데이터를 처리하는 로직 추가
            return ResponseDto.ofSuccessData("프로젝트 플래너 생성에 성공했습니다.", null);
        } catch (Exception e) {

            return ResponseDto.ofFailMessage(HttpStatus.BAD_REQUEST.value(), "프로젝트 플래너 생성에 실패했습니다.");
        }
    }

}
