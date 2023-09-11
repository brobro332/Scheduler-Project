package kr.co.scheduler.scheduler.repository;

import kr.co.scheduler.scheduler.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TaskRepository extends JpaRepository<Task, Long> {

    @Modifying
    @Query("UPDATE Task t SET t.check_yn = :checkYn WHERE t.id = :taskId")
    void updateTaskStatus(@Param("taskId") Long taskId, @Param("checkYn") String checkYn);
}
