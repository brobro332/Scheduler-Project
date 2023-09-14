package kr.co.scheduler.scheduler.repository;

import kr.co.scheduler.scheduler.entity.TaskLog;
import org.springframework.data.jpa.repository.JpaRepository;
public interface TaskLogRepository extends JpaRepository<TaskLog, Long> {

}
