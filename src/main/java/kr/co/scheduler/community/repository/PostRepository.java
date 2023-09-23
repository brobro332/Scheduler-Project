package kr.co.scheduler.community.repository;

import kr.co.scheduler.community.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findByTitleContaining(Pageable pageable, String keyword);

    @Modifying
    @Query("update Post p set p.view_cnt = p.view_cnt + 1 where p.id = :id")
    int updateViewCnt(Long id);
}
