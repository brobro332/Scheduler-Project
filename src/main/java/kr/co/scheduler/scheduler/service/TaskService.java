package kr.co.scheduler.scheduler.service;

import kr.co.scheduler.scheduler.dtos.TaskReqDTO;
import kr.co.scheduler.scheduler.entity.Project;
import kr.co.scheduler.scheduler.entity.SubTask;
import kr.co.scheduler.scheduler.entity.Task;
import kr.co.scheduler.scheduler.repository.SubTaskRepository;
import kr.co.scheduler.scheduler.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final SubTaskRepository subTaskRepository;

    @Transactional
    public void updateTask(Long id, String updatedTask, List<String> updatedSubTasks) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("업무를 찾을 수 없습니다."));

        // 업무 업데이트 로직을 여기에 추가합니다.
        task.updateTask(Long.toString(id), updatedTask);

        if (updatedSubTasks != null) {
            // 업데이트된 세부 업무 목록을 가져와서 기존 세부 업무와 비교하며 업데이트 또는 추가합니다.
            List<SubTask> subTasks = new ArrayList<>();
            for (int i = 0; i < updatedSubTasks.size(); i++) {
                String updatedSubTask = updatedSubTasks.get(i);
                SubTask subTask;
                if (i < task.getSubTasks().size()) {
                    // 기존 세부 업무가 있으면 업데이트
                    subTask = task.getSubTasks().get(i);
                    subTask.setName(updatedSubTask);
                } else {
                    // 기존 세부 업무가 없으면 추가
                    subTask = new SubTask(updatedSubTask, task, Character.toString('N'));
                }
                subTasks.add(subTask);
            }
            task.setSubTasks(subTasks);
        }
    }
    @Transactional
    public void addTasks(Project project, List<TaskReqDTO.CREATE> addedTasks) {
        // 업무 추가 로직 작성
        for (TaskReqDTO.CREATE create : addedTasks) {

            Task task = Task
                    .builder()
                    .task(create.getTask())
                    .project(project)
                    .build();

            taskRepository.save(task);
        }
    }

    @Transactional
    public void deleteTasks(List<TaskReqDTO.DELETE> deletedTasks) {

        for (TaskReqDTO.DELETE delete :  deletedTasks) {

            Task task = taskRepository.findById(Long.parseLong(delete.getIdx())).orElse(null);

            taskRepository.delete(task);
        }
    }

    @Transactional
    public void updateTaskStatus(TaskReqDTO.CHECKBOX checkbox) {
        List<Long> taskIds = checkbox.getTaskIds();
        List<String> taskTypes = checkbox.getTaskTypes();
        List<String> checkYnList = checkbox.getCheckYnList();

        // taskIds, taskTypes, checkYnList 의 크기가 동일한지 확인
        if (taskIds.size() != taskTypes.size() || taskIds.size() != checkYnList.size()) {
            throw new IllegalArgumentException("입력된 데이터 크기가 일치하지 않습니다.");
        }

        // 업무 및 세부업무 업데이트 처리
        for (int i = 0; i < taskIds.size(); i++) {
            Long taskId = taskIds.get(i);
            String taskType = taskTypes.get(i);
            String checkYn = checkYnList.get(i);

            if ("task".equals(taskType)) {
                // 업무 업데이트 로직 구현
                taskRepository.updateTaskStatus(taskId, checkYn);
            } else if ("subTask".equals(taskType)) {
                // 세부업무 업데이트 로직 구현
                subTaskRepository.updateSubTaskStatus(taskId, checkYn);
            } else {
                throw new IllegalArgumentException("올바르지 않은 taskType 값입니다.");
            }
        }
    }

    public List<SubTask> getSubTasks(Long id) {

        Task task = taskRepository.findById(id).orElse(null);

        return task.getSubTasks();
    }
}
