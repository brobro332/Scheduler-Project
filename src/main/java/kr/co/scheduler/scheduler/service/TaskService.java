package kr.co.scheduler.scheduler.service;

import kr.co.scheduler.scheduler.dtos.TaskReqDTO;
import kr.co.scheduler.scheduler.entity.Project;
import kr.co.scheduler.scheduler.entity.SubTask;
import kr.co.scheduler.scheduler.entity.Task;
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
}
