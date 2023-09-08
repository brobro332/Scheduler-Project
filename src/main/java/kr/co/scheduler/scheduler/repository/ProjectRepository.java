package kr.co.scheduler.scheduler.repository;

import kr.co.scheduler.scheduler.entity.Project;
import kr.co.scheduler.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    Page<Project> findPageByUser_Id(Pageable pageable, Long id);

    Long countByUser(User user);
}
