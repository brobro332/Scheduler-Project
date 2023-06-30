package kr.co.scheduler.user.repository;

import kr.co.scheduler.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    Optional<User> findOptionalByEmail(String emial);

    Optional<User> findById(Long userId);
}
