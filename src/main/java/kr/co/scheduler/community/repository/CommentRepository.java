package kr.co.scheduler.community.repository;

import kr.co.scheduler.community.entity.Comment;
import kr.co.scheduler.community.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findPageByPost(Pageable pageable, Post post);

    Long countByPost(Post post);
}
