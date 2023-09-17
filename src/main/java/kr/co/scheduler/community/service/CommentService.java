package kr.co.scheduler.community.service;

import kr.co.scheduler.community.dtos.CommentReqDTO;
import kr.co.scheduler.community.entity.Comment;
import kr.co.scheduler.community.entity.Post;
import kr.co.scheduler.community.repository.CommentRepository;
import kr.co.scheduler.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final UserService userService;
    private final PostService postService;
    private final CommentRepository commentRepository;

    /**
     * selectComment: id 에 해당하는 댓글을 조회하여 Comment 객체 리턴
     */
    public Comment selectComment(Long id) {

        Comment comment = commentRepository.findById(id)
                .orElseThrow(()->{
                    return new IllegalArgumentException("해당 댓글을 찾을 수 없습니다.");
                });

        return comment;
    }

    /**
     * createComment: 댓글 생성
     */
    @Transactional
    public void createComment(Long id, CommentReqDTO.CREATE create, String email) {

        Comment comment = Comment.builder()
                .user(userService.selectUser(email))
                .comment(create.getComment())
                .post(postService.selectPost(id))
                .build();

        commentRepository.save(comment);
    }

    /**
     * updateComment: 댓글 수정
     */
    @Transactional
    public void updateComment(Long id, CommentReqDTO.UPDATE update) {

        Comment comment = commentRepository.findById(id)
                .orElseThrow(()->{
                    return new IllegalArgumentException("해당 댓글을 찾을 수 없습니다.");
                });

        comment.updateComment(update.getUpdateComment());
    }

    /**
     * deleteComment: 댓글 삭제
     */
    @Transactional
    public void deleteComment(Long id) {

        Comment comment = commentRepository.findById(id)
                .orElseThrow(()->{
                    return new IllegalArgumentException("해당 댓글을 찾을 수 없습니다.");
                });

        commentRepository.delete(comment);
    }

    // ================================== 구분 ================================== //

    /**
     * selectComments: 게시글의 댓글 목록 조회하여 Page 객체 리턴
     */
    public Page<Comment> selectComments(Pageable pageable, Post post) {

        return commentRepository.findPageByPost(pageable, post);
    }

    /**
     * countComments: 게시글에 해당하는 댓글의 수를 리턴
     */
    public Long countComments(Post post) {

        return commentRepository.countByPost(post);
    }
}
