package kr.co.scheduler.scheduler.repository;

import kr.co.scheduler.scheduler.entity.Project;
import kr.co.scheduler.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    Page<Project> findPageByUser_Id(Pageable pageable, Long id);

    Long countByUser(User user);

    Long countByUserAndActiveYn(User user, String active_yn);

    Long countByUserAndCompleteYn(User user, String complete_yn);

    List<Project> findByActiveYn(String active_yn);
}
