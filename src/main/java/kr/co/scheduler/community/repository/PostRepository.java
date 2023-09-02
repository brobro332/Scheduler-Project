package kr.co.scheduler.community.repository;

import kr.co.scheduler.community.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findByTitleContaining(Pageable pageable, String keyword);
}
