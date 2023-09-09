package kr.co.scheduler.scheduler.repository;

import kr.co.scheduler.scheduler.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {

}
