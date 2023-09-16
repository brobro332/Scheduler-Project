package kr.co.scheduler.scheduler.service;


import kr.co.scheduler.scheduler.entity.TaskLog;
import kr.co.scheduler.scheduler.repository.TaskLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskLogService {

    private final TaskLogRepository taskLogRepository;

    public TaskLog selectTaskLog(Long id) {

        TaskLog taskLog = taskLogRepository.findById(id)
                .orElseThrow(()->{
                    return new IllegalArgumentException("해당 업무일지를 찾을 수 없습니다.");
                });

        return taskLog;
    }

    public String getTaskCategory(Long id) {

        TaskLog taskLog = taskLogRepository.findById(id).orElse(null);
        String taskCategory = null;

        if (taskLog != null) {

            taskCategory = taskLog.getTaskCategory();
        }

        return taskCategory;
    }

    public String getSubTaskCategory(Long id) {

        TaskLog subTaskLog = taskLogRepository.findById(id).orElse(null);
        String subTaskCategory = null;

        if (subTaskLog != null) {

            subTaskCategory = subTaskLog.getSubTaskCategory();
        }

        return subTaskCategory;
    }
}
