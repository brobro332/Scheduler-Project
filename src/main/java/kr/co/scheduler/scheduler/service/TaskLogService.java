package kr.co.scheduler.scheduler.service;

import kr.co.scheduler.scheduler.dtos.TaskLogReqDTO;
import kr.co.scheduler.scheduler.entity.Project;
import kr.co.scheduler.scheduler.entity.TaskLog;
import kr.co.scheduler.scheduler.repository.ProjectRepository;
import kr.co.scheduler.scheduler.repository.TaskLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskLogService {

    private final ProjectRepository projectRepository;
    private final TaskLogRepository taskLogRepository;

    /**
     * selectTaskLog: 업무일지 조회 및 TaskLog 객체 리턴
     */
    public TaskLog selectTaskLog(Long id) {

        TaskLog taskLog = taskLogRepository.findById(id)
                .orElseThrow(()->{
                    return new IllegalArgumentException("해당 업무일지를 찾을 수 없습니다.");
                });

        return taskLog;
    }

    /**
     * selectTaskLog: 업무일지 조회 및 Page 객체 리턴
     */
    @Transactional
    public Page<TaskLog> selectTaskLog(Pageable pageable, Long id) {

        Project project = projectRepository.findById(id).orElse(null);
        Page<TaskLog> taskLogs = null;

        if (project != null) {

            taskLogs = taskLogRepository.findByProject(pageable, project);
        }

        return taskLogs;
    }

    /**
     * createTaskLog: 업무일지 등록
     */
    @Transactional
    public void createTaskLog(TaskLogReqDTO taskLogReqDTO, Long id) {

        Project project = projectRepository.findById(id).orElse(null);

        if (project != null) {

            TaskLog taskLog = TaskLog
                    .builder()
                    .title(taskLogReqDTO.getTitle())
                    .content(taskLogReqDTO.getContent())
                    .taskCategory(taskLogReqDTO.getTaskCategory())
                    .subTaskCategory(taskLogReqDTO.getSubTaskCategory())
                    .project(project)
                    .build();

            List<TaskLog> temp = project.getTaskLogs();
            temp.add(taskLog);
            project.setTaskLogs(temp);

            taskLogRepository.save(taskLog);
        }
    }

    /**
     * updateTaskLog: 업무일지 수정
     */
    @Transactional
    public void updateTaskLog(TaskLogReqDTO taskLogReqDTO, Long id) {

        TaskLog taskLog = taskLogRepository.findById(id).orElse(null);


        if (taskLog != null) {

            taskLog.updateTaskLog(taskLogReqDTO.getTitle(), taskLogReqDTO.getContent(), taskLogReqDTO.getTaskCategory(), taskLogReqDTO.getSubTaskCategory());
        }
    }

    /**
     * deleteTaskLog: 업무일지 삭제
     */
    @Transactional
    public void deleteTaskLog(Long task_log_id, Long project_id) {

        Project project = projectRepository.findById(project_id).orElse(null);
        TaskLog taskLog = taskLogRepository.findById(task_log_id).orElse(null);

        if (taskLog != null) {

            project.getTaskLogs().remove(taskLog);

            taskLogRepository.delete(taskLog);
        }
    }

    // ================================== 구분 ================================== //

    /**
     * getTaskCategory: 업무일지 등록 및 수정을 위한 업무 카테고리 목록 조회 및 추출
     */
    public String getTaskCategory(Long id) {

        TaskLog taskLog = taskLogRepository.findById(id).orElse(null);
        String taskCategory = null;

        if (taskLog != null) {

            taskCategory = taskLog.getTaskCategory();
        }

        return taskCategory;
    }

    /**
     * getSubTaskCategory: 업무일지 등록 및 수정을 위한 하위업무 카테고리 목록 조회 및 추출
     */
    public String getSubTaskCategory(Long id) {

        TaskLog subTaskLog = taskLogRepository.findById(id).orElse(null);
        String subTaskCategory = null;

        if (subTaskLog != null) {

            subTaskCategory = subTaskLog.getSubTaskCategory();
        }

        return subTaskCategory;
    }
}
