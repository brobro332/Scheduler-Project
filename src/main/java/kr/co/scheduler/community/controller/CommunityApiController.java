package kr.co.scheduler.community.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.scheduler.community.dtos.CommentReqDTO;
import kr.co.scheduler.community.dtos.PostReqDTO;
import kr.co.scheduler.community.dtos.ReplyReqDTO;
import kr.co.scheduler.community.entity.Comment;
import kr.co.scheduler.community.entity.Post;
import kr.co.scheduler.community.entity.Reply;
import kr.co.scheduler.community.service.CommentService;
import kr.co.scheduler.community.service.PostService;
import kr.co.scheduler.community.service.ReplyService;
import kr.co.scheduler.global.dtos.ResponseDto;
import kr.co.scheduler.user.entity.User;
import kr.co.scheduler.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class CommunityApiController {

    private final UserService userService;
    private final PostService postService;
    private final CommentService commentService;
    private final ReplyService replyService;

    /**
     * createPost: 게시글 등록
     */
    @PostMapping("/api/community/post")
    public ResponseDto<Object> createPost(@RequestBody PostReqDTO.CREATE create, Principal principal) throws IOException {

        postService.createPost(create, principal.getName());

        return ResponseDto.ofSuccessData(
                "게시글이 정상적으로 등록되었습니다.",
                null);
    }

    /**
     * updatePost: 게시글 수정
     */
    @PutMapping("/api/community/post/{post_id}")
    public ResponseDto<Object> updatePost(@RequestBody PostReqDTO.UPDATE update,
                                          Principal principal,
                                          @PathVariable(name = "post_id") Long id) throws IOException {

        postService.updatePost(update, principal.getName(), id);

        return ResponseDto.ofSuccessData(
                "게시글이 정상적으로 수정되었습니다.",
                null);
    }

    /**
     * deletePost: 게시글 삭제
     */
    @DeleteMapping("/api/community/post/{post_id}")
    public ResponseDto<Object> deletePost(Principal principal, @PathVariable(name = "post_id") Long id) throws IOException {

            Post post = postService.selectPost(id);

            postService.deletePost(post, principal.getName());

            return ResponseDto.ofSuccessData(
                    "게시글이 정상적으로 삭제되었습니다.",
                    null);
    }

    // ================================== 구분 ================================== //

    /**
     * updateViewCnt: 게시글 조회시 쿠키 기반의 조회수 증가
     */
    @PostMapping("/api/community/post/{post_id}/viewCnt")
    public ResponseDto<?> updateViewCnt(@PathVariable Long post_id, HttpServletRequest request, HttpServletResponse response) {

        Post post = postService.selectPost(post_id);

        if (post != null) {

            // 이미 조회된 게시물인지 쿠키를 통해 확인
            String cookieName = "viewed_" + post_id;
            Cookie[] cookies = request.getCookies();
            boolean alreadyViewed = false;
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals(cookieName)) {
                        alreadyViewed = true;
                        break;
                    }
                }
            }

            if (!alreadyViewed) {

                postService.updateViewCnt(post_id);

                // 쿠키 설정 (24시간 동안 유지)
                Cookie viewCookie = new Cookie(cookieName, "1");
                viewCookie.setMaxAge(24 * 60 * 60); // 1일
                viewCookie.setPath("/");
                response.addCookie(viewCookie);

                return ResponseDto.ofSuccessMessage("조회수가 증가했습니다.");
            } else {

                return ResponseDto.ofFailMessage(HttpStatus.BAD_REQUEST.value(), "이미 조회했습니다.");
            }
        } else {

            return ResponseDto.ofFail(HttpStatus.NOT_FOUND.value());
        }
    }

    // ================================== 구분 ================================== //

    /**
     * createComment: 댓글 등록
     */
    @PostMapping("/api/community/post/{post_id}/comment")
    public ResponseDto<?> createComment(@PathVariable(name = "post_id") Long id, @RequestBody CommentReqDTO.CREATE create, Principal principal) {

        commentService.createComment(id, create, principal.getName());

        return ResponseDto.ofSuccessData(
                "댓글이 정상적으로 등록되었습니다.",
                null);
    }

    /**
     * updateComment: 댓글 수정
     */
    @PutMapping("/api/community/post/comment/{comment_id}")
    public ResponseDto<?> updateComment(@PathVariable(name = "comment_id") Long id, @RequestBody CommentReqDTO.UPDATE update, Principal principal) {
        
        User user = userService.selectUser(principal.getName());
        Comment comment = commentService.selectComment(id);

        if(!user.getEmail().equals(comment.getUser().getEmail())) {

            return ResponseDto.ofFailMessage(
                    HttpStatus.BAD_REQUEST.value(),
                    "댓글 수정은 댓글 작성자만 수행할 수 있습니다.");
        }

        commentService.updateComment(id, update);

        return ResponseDto.ofSuccessData(
                "댓글이 정상적으로 수정되었습니다.",
                null);
    }

    /**
     * deleteComment: 댓글 삭제
     */
    @DeleteMapping("/api/community/post/comment/{comment_id}")
    public ResponseDto<?> deleteComment(@PathVariable(name = "comment_id") Long id, Principal principal) {

        User user = userService.selectUser(principal.getName());
        Comment comment = commentService.selectComment(id);

        if(!user.getEmail().equals(comment.getUser().getEmail())) {

            return ResponseDto.ofFailMessage(
                    HttpStatus.BAD_REQUEST.value(),
                    "댓글 작성자만 삭제할 수 있습니다.");
        }

        commentService.deleteComment(id);

        return ResponseDto.ofSuccessData(
                "댓글이 정상적으로 삭제되었습니다.",
                null);
    }

    // ================================== 구분 ================================== //

    /**
     * createReplyToComment: 본 댓글에 대한 대댓글 등록
     */
    @PostMapping("/api/community/post/{post_id}/comment/{comment_id}/replyToComment")
    public ResponseDto<?> createReplyToComment(@PathVariable(name = "post_id") Long post_id, @PathVariable(name = "comment_id") Long comment_id, @RequestBody ReplyReqDTO.CREATE create, Principal principal) {

        replyService.createReplyToComment(post_id, comment_id, create, principal.getName());

        return ResponseDto.ofSuccessData(
                "답글이 정상적으로 등록되었습니다.",
                null);
    }

    /**
     * createReplyToReply: 대댓글에 대한 대댓글 등록
     */
    @PostMapping("/api/community/post/{post_id}/comment/{comment_id}/replyToReply")
    public ResponseDto<?> createReplyToReply(@PathVariable(name = "post_id") Long post_id, @PathVariable(name = "comment_id") Long comment_id, @RequestBody ReplyReqDTO.CREATE create, Principal principal) {

        replyService.createReplyToReply(post_id, comment_id, create, principal.getName());

        return ResponseDto.ofSuccessData(
                "답글이 정상적으로 등록되었습니다.",
                null);
    }

    /**
     * updateComment: 대댓글 수정
     */
    @PutMapping("/api/community/comment/reply/{reply_id}")
    public ResponseDto<?> updateReply(@PathVariable(name = "reply_id") Long id, @RequestBody ReplyReqDTO.UPDATE update, Principal principal) {

        User user = userService.selectUser(principal.getName());
        Reply reply = replyService.selectReply(id);

        if(!user.getEmail().equals(reply.getUser().getEmail())) {

            return ResponseDto.ofFailMessage(
                    HttpStatus.BAD_REQUEST.value(),
                    "대댓글 작성자만 내용을 수정할 수 있습니다.");
        }

        replyService.updateReply(id, update);

        return ResponseDto.ofSuccessData(
                "대댓글이 정상적으로 수정되었습니다.",
                null);
    }

    /**
     * deleteReply: 대댓글 삭제
     */
    @DeleteMapping("/api/community/comment/reply/{reply_id}")
    public ResponseDto<?> deleteReply(@PathVariable(name = "reply_id") Long id, Principal principal) {

        User user = userService.selectUser(principal.getName());
        Reply reply = replyService.selectReply(id);

        if(!user.getEmail().equals(reply.getUser().getEmail())) {

            return ResponseDto.ofFailMessage(
                    HttpStatus.BAD_REQUEST.value(),
                    "대댓글 작성자만 삭제할 수 있습니다.");
        }

        replyService.deleteReply(id);

        return ResponseDto.ofSuccessData(
                "대댓글이 정상적으로 삭제되었습니다.",
                null);
    }

}
