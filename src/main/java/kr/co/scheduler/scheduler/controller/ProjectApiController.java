package kr.co.scheduler.scheduler.controller;

import kr.co.scheduler.global.dtos.ResponseDto;
import kr.co.scheduler.scheduler.dtos.ProjectReqDTO;
import kr.co.scheduler.scheduler.dtos.TaskLogReqDTO;
import kr.co.scheduler.scheduler.dtos.TaskReqDTO;
import kr.co.scheduler.scheduler.entity.SubTask;
import kr.co.scheduler.scheduler.entity.Task;
import kr.co.scheduler.scheduler.service.ProjectService;
import kr.co.scheduler.scheduler.service.TaskLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/scheduler/project")
public class ProjectApiController {

    private final ProjectService projectService;
    private final TaskLogService taskLogService;

    /**
     * createPRJPlanner: 프로젝트 플래너 등록
     */
    @PostMapping
    public ResponseDto<?> createPRJPlanner(@RequestBody ProjectReqDTO.CREATE create, Principal principal) {

        try {

            List<Task> tasks = new ArrayList<>();
            List<SubTask> subTasks = new ArrayList<>();

            if (create.getTasks() != null) {
                for (TaskReqDTO.CREATE taskDTO : create.getTasks()) {
                    Task task = Task.builder()
                            .idx(taskDTO.getIdx())
                            .task(taskDTO.getTask())
                            .check_yn("N")
                            .build();
                    tasks.add(task);


            if (taskDTO.getSubTasks() != null) {
                for (String subTaskName : taskDTO.getSubTasks()) {
                    SubTask subTask = SubTask.builder()
                            .name(subTaskName)
                            .task(task)
                            .check_yn("N")
                            .build();
                    subTasks.add(subTask);
                        }
                    }
                }
            }

            projectService.createPRJPlanner(create, tasks, subTasks, principal.getName());

            return ResponseDto.ofSuccessData("프로젝트 플래너 생성에 성공했습니다.", null);
        } catch (Exception e) {

            return ResponseDto.ofFailMessage(HttpStatus.BAD_REQUEST.value(), "프로젝트 플래너 생성에 실패했습니다.");
        }
    }

    /**
     * updatePRJPlanner: 프로젝트 플래너 수정
     */
    @PutMapping("/{project_id}")
    public ResponseDto<?> updatePRJPlanner(@PathVariable(name = "project_id") Long id, @RequestBody ProjectReqDTO.UPDATE update) {
        
        try {

            projectService.updatePRJPlanner(id, update);

            return ResponseDto.ofSuccessData("프로젝트 업데이트에 성공하였습니다.", null);
        } catch (Exception e) {

            return ResponseDto.ofFailData(HttpStatus.BAD_REQUEST.value(), "프로젝트 업데이트에 실패하였습니다.", null);
        }
    }

    /**
     * deletePRJPlanner: 프로젝트 플래너 삭제
     */
    @DeleteMapping("/{project_id}")
    public ResponseDto<?> deletePRJPlanner(@PathVariable(name = "project_id") Long id) {

        projectService.deleteProject(id);

        return ResponseDto.ofSuccessData("프로젝트 삭제에 성공하였습니다.", null);
    }

    /**
     * activePRJPlanner: 프로젝트 플래너 활성화
     * 활성화된 경우 스케줄러가 작동하여 FCM 메세지를 사용자의 브라우저로 전송
     */
    @PutMapping("/{project_id}/activity")
    public ResponseDto<?> activePRJPlanner(@PathVariable(name = "project_id") Long id) {

        projectService.activePRJPlanner(id);

        return ResponseDto.ofSuccessData("프로젝트 활성 상태를 전환했습니다.", null);
    }

    /**
     * endPRJPlanner: 프로젝트 플래너 마감
     */
    @PutMapping("/{project_id}/deadline")
    public ResponseDto<?> endPRJPlanner(@PathVariable(name = "project_id") Long id) {

        projectService.endPRJPlanner(id);

        return ResponseDto.ofSuccessData("프로젝트를 마감했습니다.", null);
    }

    // ================================== 구분 ================================== //

    /**
     * createTaskLog: 업무일지 등록
     */
    @PostMapping("/{project_id}/taskLog")
    public ResponseDto<?> createTaskLog(@PathVariable(name = "project_id") Long id, @RequestBody TaskLogReqDTO taskLogReqDTO) {

        taskLogService.createTaskLog(taskLogReqDTO, id);

        return ResponseDto.ofSuccessData("업무 일지를 성공적으로 등록했습니다.", null);
    }

    /**
     * updateTaskLog: 업무일지 수정
     */
    @PutMapping("/taskLog/{task_log_id}")
    public ResponseDto<?> updateTaskLog(@PathVariable(name = "task_log_id") Long id, @RequestBody TaskLogReqDTO taskLogReqDTO) {

        taskLogService.updateTaskLog(taskLogReqDTO, id);

        return ResponseDto.ofSuccessData("업무 일지를 성공적으로 수정했습니다.", null);
    }

    /**
     * deleteTaskLog: 업무일지 삭제
     */
    @DeleteMapping("/taskLog/{task_log_id}")
    public ResponseDto<?> deleteTaskLog(@PathVariable(name = "task_log_id") Long task_log_id, @RequestParam("project_id") Long project_id) {

        taskLogService.deleteTaskLog(task_log_id, project_id);

        return ResponseDto.ofSuccessData("업무 일지를 성공적으로 삭제했습니다.", null);
    }

    /**
     * selectCategories: 업무 및 세부업무 카테고리 조회 및 리턴
     */
    @GetMapping("/taskLog/categories")
    public @ResponseBody Map<String, String> selectCategories(@RequestParam("taskLog_id") Long id) {

        Map<String, String> data = new HashMap<>();
        data.put("taskCategory", taskLogService.getTaskCategory(id));
        data.put("subTaskCategory", taskLogService.getSubTaskCategory(id));

        return data;
    }
}
