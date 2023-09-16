package kr.co.scheduler.scheduler.repository;

import kr.co.scheduler.scheduler.entity.Project;
import kr.co.scheduler.scheduler.entity.TaskLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
public interface TaskLogRepository extends JpaRepository<TaskLog, Long> {

    Page<TaskLog> findByProject(Pageable pageable, Project project);
}
