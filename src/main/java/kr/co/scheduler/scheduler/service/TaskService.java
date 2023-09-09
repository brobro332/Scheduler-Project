package kr.co.scheduler.scheduler.service;

import kr.co.scheduler.scheduler.dtos.TaskReqDTO;
import kr.co.scheduler.scheduler.entity.Project;
import kr.co.scheduler.scheduler.entity.Task;
import kr.co.scheduler.scheduler.repository.ProjectRepository;
import kr.co.scheduler.scheduler.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

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
