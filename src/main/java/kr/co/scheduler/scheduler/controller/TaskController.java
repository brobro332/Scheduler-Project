package kr.co.scheduler.scheduler.controller;

import kr.co.scheduler.scheduler.dtos.SubTaskResDTO;
import kr.co.scheduler.scheduler.entity.SubTask;
import kr.co.scheduler.scheduler.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/scheduler/manage/subTask")
    @ResponseBody
    public List<SubTaskResDTO> getSubTasks(@RequestParam Long task_id) {

        List<SubTask> subTasks = taskService.getSubTasks(task_id);

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
