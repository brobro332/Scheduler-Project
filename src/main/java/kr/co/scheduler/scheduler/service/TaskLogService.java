package kr.co.scheduler.scheduler.service;

import kr.co.scheduler.global.service.ImgService;
import kr.co.scheduler.scheduler.dtos.TaskLogReqDTO;
import kr.co.scheduler.scheduler.entity.Project;
import kr.co.scheduler.scheduler.entity.TaskLog;
import kr.co.scheduler.scheduler.repository.ProjectRepository;
import kr.co.scheduler.scheduler.repository.TaskLogRepository;
import kr.co.scheduler.user.entity.User;
import kr.co.scheduler.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TaskLogService {

    private final UserService userService;
    private final ImgService imgService;
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
    public void createTaskLog(TaskLogReqDTO taskLogReqDTO, Long id, String email) throws IOException {

        Project project = projectRepository.findById(id).orElse(null);

        if (project != null) {

            if (Objects.equals(project.getCompleteYn(), "Y")) {

                throw new IllegalArgumentException("이미 완료된 프로젝트입니다.");
            }

            imgService.renameImgInSummernote(taskLogReqDTO.getContent(), "C:\\upload\\taskLog\\" + email);

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
    public void updateTaskLog(TaskLogReqDTO taskLogReqDTO, Long id, String email) throws IOException {

        User user = userService.selectUser(email);
        TaskLog taskLog = taskLogRepository.findById(id).orElse(null);

        if (user != null) {

            if (taskLog != null) {

                imgService.renameImgInSummernote(taskLog.getContent(), "C:\\upload\\temp\\" + email);
                imgService.renameImgInSummernote(taskLogReqDTO.getContent(), "C:\\upload\\taskLog\\" + email);

                // temp 폴더 비우기
                String deletePath = "C:\\upload\\temp\\" + email;
                imgService.clearTempDir(deletePath);

                taskLog.updateTaskLog(taskLogReqDTO.getTitle(), taskLogReqDTO.getContent(), taskLogReqDTO.getTaskCategory(), taskLogReqDTO.getSubTaskCategory());
            }
        }
    }

    /**
     * deleteTaskLog: 업무일지 삭제
     */
    @Transactional
    public void deleteTaskLog(Long task_log_id, Long project_id, String email) {

        Project project = projectRepository.findById(project_id).orElse(null);
        TaskLog taskLog = taskLogRepository.findById(task_log_id).orElse(null);
        User user = userService.selectUser(email);

        if (user != null) {

            if (project != null) {

                if (taskLog != null) {

                    imgService.deleteImgInSummernote(taskLog.getContent());

                    taskLogRepository.delete(taskLog);
                    System.out.println("hey2");
                }
            }
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
