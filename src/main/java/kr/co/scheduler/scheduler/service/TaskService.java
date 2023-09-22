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
    
    /**
     * selectSubTasks: 업무에 해당되는 하위 업무 List 객체 리턴
     */
    public List<SubTask> selectSubTasks(Long id) {

        Task task = taskRepository.findById(id).orElse(null);

        return task.getSubTasks();
    }

    /**
     * createTasks: 업무 목록 및 요소 등록
     */
    @Transactional
    public void createTasks(Project project, List<TaskReqDTO.CREATE> addedTasks) {

        for (TaskReqDTO.CREATE create : addedTasks) {

            Task task = Task
                    .builder()
                    .task(create.getTask())
                    .project(project)
                    .check_yn(Character.toString('N'))
                    .build();

            taskRepository.save(task);
        }
    }

    /**
     * updateTasks: 업무 목록 및 요소 수정
     */
    @Transactional
    public void updateTasks(Long id, String updatedTask, List<String> updatedSubTasks) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("업무를 찾을 수 없습니다."));

        task.updateTask(Long.toString(id), updatedTask);

        if (updatedSubTasks != null) {

            List<SubTask> subTasks = new ArrayList<>();
            for (int i = 0; i < updatedSubTasks.size(); i++) {
                String updatedSubTask = updatedSubTasks.get(i);
                SubTask subTask;
                if (i < task.getSubTasks().size()) {

                    subTask = task.getSubTasks().get(i);
                    subTask.setName(updatedSubTask);
                } else {

                    subTask = new SubTask(updatedSubTask, task, Character.toString('N'));
                }
                subTasks.add(subTask);
            }
            task.setSubTasks(subTasks);
        }
    }

    /**
     * deleteTasks: 업무 목록 및 요소 삭제
     */
    @Transactional
    public void deleteTasks(List<TaskReqDTO.DELETE> deletedTasks) {

        for (TaskReqDTO.DELETE delete : deletedTasks) {

            Task task = taskRepository.findById(Long.parseLong(delete.getIdx())).orElse(null);

            taskRepository.delete(task);
        }
    }

    // ================================== 구분 ================================== //

    /**
     * updateTaskCheckStatus: 업무 완료 여부에 대한 체크박스를 수정
     */
    @Transactional
    public void updateTaskCheckStatus(TaskReqDTO.CHECKBOX checkbox) {
        List<Long> taskIds = checkbox.getTaskIds();
        List<String> taskTypes = checkbox.getTaskTypes();
        List<String> checkYnList = checkbox.getCheckYnList();

        if (taskIds.size() != taskTypes.size() || taskIds.size() != checkYnList.size()) {
            throw new IllegalArgumentException("입력된 데이터 크기가 일치하지 않습니다.");
        }

        for (int i = 0; i < taskIds.size(); i++) {
            Long taskId = taskIds.get(i);
            String taskType = taskTypes.get(i);
            String checkYn = checkYnList.get(i);

            if ("task".equals(taskType)) {

                taskRepository.updateTaskStatus(taskId, checkYn);
            } else if ("subTask".equals(taskType)) {

                subTaskRepository.updateSubTaskStatus(taskId, checkYn);
            } else {

                throw new IllegalArgumentException("유효하지 않은 업무타입 값입니다");
            }
        }
    }
}
