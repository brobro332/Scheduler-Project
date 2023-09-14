package kr.co.scheduler.scheduler.controller;

import kr.co.scheduler.global.dtos.ResponseDto;
import kr.co.scheduler.scheduler.dtos.ProjectReqDTO;
import kr.co.scheduler.scheduler.dtos.TaskLogReqDTO;
import kr.co.scheduler.scheduler.dtos.TaskReqDTO;
import kr.co.scheduler.scheduler.entity.SubTask;
import kr.co.scheduler.scheduler.entity.Task;
import kr.co.scheduler.scheduler.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/scheduler/project")
public class ProjectApiController {

    private final ProjectService projectService;

    @PostMapping("/create")
    public ResponseDto<?> createProjectPlanner(@RequestBody ProjectReqDTO.CREATE create, Principal principal) {
        try {
            List<Task> tasks = new ArrayList<>();
            List<SubTask> subTasks = new ArrayList<>();

            if (create.getTasks() != null) {
                for (TaskReqDTO.CREATE taskDTO : create.getTasks()) {
                    Task task = Task.builder()
                            .idx(taskDTO.getIdx())
                            .task(taskDTO.getTask())
                            .build();
                    tasks.add(task);


                    if (taskDTO.getSubTasks() != null) {
                        for (String subTaskName : taskDTO.getSubTasks()) {
                            SubTask subTask = SubTask.builder()
                                    .name(subTaskName)
                                    .task(task)
                                    .check_yn("N") // 초기값은 미완료로 설정
                                    .build();
                            subTasks.add(subTask);
                        }
                    }
                }
            }

            projectService.createProjectPlanner(create, tasks, subTasks, principal.getName());

            return ResponseDto.ofSuccessData("프로젝트 플래너 생성에 성공했습니다.", null);
        } catch (Exception e) {

            e.printStackTrace();
            return ResponseDto.ofFailMessage(HttpStatus.BAD_REQUEST.value(), "프로젝트 플래너 생성에 실패했습니다.");
        }
    }

    @PostMapping("/update/{project_id}")
    public ResponseDto<?> updateProjectPlanner(@PathVariable(name = "project_id") Long id, @RequestBody ProjectReqDTO.UPDATE update) {
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

    @PutMapping("/active/{project_id}")
    public ResponseDto<?> activeProject(@PathVariable(name = "project_id") Long id) {

        projectService.activeProject(id);

        return ResponseDto.ofSuccessData("프로젝트 활성 상태를 전환했습니다.", null);
    }

    @PostMapping("/taskLog/{project_id}")
    public ResponseDto<?> writeTaskLog(@PathVariable(name = "project_id") Long id, @RequestBody TaskLogReqDTO taskLogReqDTO) {

        projectService.writeTaskLog(taskLogReqDTO, id);

        return ResponseDto.ofSuccessData("업무 일지를 성공적으로 등록했습니다.", null);
    }
}
