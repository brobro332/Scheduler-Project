package kr.co.scheduler.scheduler.controller;

import kr.co.scheduler.global.dtos.ResponseDto;
import kr.co.scheduler.scheduler.dtos.TaskReqDTO;
import kr.co.scheduler.scheduler.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/scheduler/task")
@RequiredArgsConstructor
public class TaskApiController {

    private final TaskService taskService;

    @PostMapping("/update/checkStatus")
    public ResponseDto<?> updateTaskCheckStatus(@RequestBody TaskReqDTO.CHECKBOX checkbox) {

        try {

            taskService.updateTaskStatus(checkbox);

            return ResponseDto.ofSuccessMessage("업무 수행 체크박스 수정에 성공하였습니다.");
        } catch (Exception e) {

            return ResponseDto.ofFailMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), "업무 수행 체크박스 수정에 실패앴습니다.");
        }
    }
}
