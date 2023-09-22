package kr.co.scheduler.user.repository;

import kr.co.scheduler.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    Optional<User> findOptionalByEmail(String email);

    Optional<User> findById(Long userId);

    Page<User> findByLastLoggedDay(LocalDate date, PageRequest pageRequest);
}
