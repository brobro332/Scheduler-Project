package kr.co.scheduler.scheduler.controller;

import kr.co.scheduler.global.dtos.ResponseDto;
import kr.co.scheduler.scheduler.dtos.ProjectReqDTO;
import kr.co.scheduler.scheduler.entity.Project;
import kr.co.scheduler.scheduler.entity.Task;
import kr.co.scheduler.scheduler.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

        int idx = 0;

        try {
            List<Task> taskList = new ArrayList<>();

            for (Map.Entry<String, String> entry : create.getJsonData().entrySet()) {

                Task task = Task
                        .builder()
                        .idx(Integer.toString(idx))
                        .task(entry.getValue())
                        .build();
                taskList.add(task);

                idx = idx + 1;
            }

            projectService.createProjectPlanner(create, taskList, principal.getName());

            // 수신한 데이터를 처리하는 로직 추가
            return ResponseDto.ofSuccessData("프로젝트 플래너 생성에 성공했습니다.", null);
        } catch (Exception e) {

            return ResponseDto.ofFailMessage(HttpStatus.BAD_REQUEST.value(), "프로젝트 플래너 생성에 실패했습니다.");
        }
    }

    @PostMapping("/update/{project_id}")
    public ResponseDto<?> updateProject(@PathVariable(name = "project_id") Long id, @RequestBody ProjectReqDTO.UPDATE update) {
        try {
            // projectId에 해당하는 프로젝트를 불러온 후 업데이트할 내용을 적용
            projectService.updateProject(id, update);

            return ResponseDto.ofSuccessData("프로젝트 업데이트에 성공하였습니다.", null);
        } catch (Exception e) {

            return ResponseDto.ofFailData(HttpStatus.BAD_REQUEST.value(), "프로젝트 업데이트에 실패하였습니다.", null);
        }
    }

    @DeleteMapping("/delete/{project_id}")
    public ResponseDto<?> deleteProject(@PathVariable(name = "project_id") Long id) {

        projectService.deleteProject(id);

        return ResponseDto.ofSuccessData("프로젝트 삭제에 성공하였습니다.", null);
    }
}
