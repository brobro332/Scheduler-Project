package kr.co.scheduler.global.repository;

import kr.co.scheduler.global.entity.AlertUser;
import kr.co.scheduler.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlertUserRepository extends JpaRepository<AlertUser, Long> {

    Page<AlertUser> findPageByUser(Pageable pageable, User user);

    List<AlertUser> findListByUser(User user);
}
