package kr.co.scheduler.scheduler.controller;

import kr.co.scheduler.global.dtos.ResponseDto;
import kr.co.scheduler.scheduler.dtos.SubTaskResDTO;
import kr.co.scheduler.scheduler.dtos.TaskReqDTO;
import kr.co.scheduler.scheduler.entity.SubTask;
import kr.co.scheduler.scheduler.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/scheduler/task")
@RequiredArgsConstructor
public class TaskApiController {

    private final TaskService taskService;

    /**
     * updateTaskCheckStatus: 업무 완료 여부에 대한 체크박스를 수정
     */
    @PutMapping("/checkStatus")
    public ResponseDto<?> updateTaskCheckStatus(@RequestBody TaskReqDTO.CHECKBOX checkbox) {

        try {

            taskService.updateTaskCheckStatus(checkbox);

            return ResponseDto.ofSuccessMessage("업무 수행 체크박스 수정에 성공하였습니다.");
        } catch (Exception e) {

            return ResponseDto.ofFailMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), "업무 수행 체크박스 수정에 실패앴습니다.");
        }
    }

    /**
     * selectSubTasks: 업무에 해당하는 하위업무들을 조회 및 리턴
     */
    @GetMapping("/subTasks")
    @ResponseBody
    public List<SubTaskResDTO> selectSubTasks(@RequestParam Long task_id) {

        List<SubTask> subTasks = taskService.selectSubTasks(task_id);

        List<SubTaskResDTO> subTaskResDTOs = new ArrayList<>();
        for (SubTask subTask : subTasks) {

            SubTaskResDTO subTaskResDTO = new SubTaskResDTO();
            subTaskResDTO.setId(subTask.getId());
            subTaskResDTO.setName(subTask.getName());

            subTaskResDTOs.add(subTaskResDTO);
        }

        return subTaskResDTOs;
    }
}
