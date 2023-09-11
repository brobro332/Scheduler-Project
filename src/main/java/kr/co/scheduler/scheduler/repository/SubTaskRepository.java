package kr.co.scheduler.scheduler.repository;

import kr.co.scheduler.scheduler.entity.SubTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SubTaskRepository extends JpaRepository<SubTask, Long> {

    @Modifying
    @Query("UPDATE SubTask s SET s.check_yn = :checkYn WHERE s.id = :taskId")
    void updateSubTaskStatus(@Param("taskId") Long taskId, @Param("checkYn") String checkYn);
}
