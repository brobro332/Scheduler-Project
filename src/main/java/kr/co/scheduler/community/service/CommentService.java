package kr.co.scheduler.community.service;

import kr.co.scheduler.community.dtos.CommentReqDTO;
import kr.co.scheduler.community.entity.Comment;
import kr.co.scheduler.community.entity.Post;
import kr.co.scheduler.community.repository.CommentRepository;
import kr.co.scheduler.user.entity.User;
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

    public void writeComment(Long id, CommentReqDTO.CREATE create, String email) {

        Comment comment = Comment.builder()
                .user(userService.findUser(email))
                .comment(create.getComment())
                .post(postService.viewOneOfPost(id))
                .build();

        commentRepository.save(comment);
    }

    public Page<Comment> viewComments(Pageable pageable, Post post) {

        return commentRepository.findPageByPost(pageable, post);
    }
}
