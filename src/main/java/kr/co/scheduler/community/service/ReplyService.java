package kr.co.scheduler.community.service;

import kr.co.scheduler.community.dtos.ReplyReqDTO;
import kr.co.scheduler.community.entity.Comment;
import kr.co.scheduler.community.entity.Post;
import kr.co.scheduler.community.entity.Reply;
import kr.co.scheduler.community.repository.ReplyRepository;
import kr.co.scheduler.user.entity.User;
import kr.co.scheduler.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReplyService {

    private final PostService postService;
    private final CommentService commentService;
    private final UserService userService;
    private final ReplyRepository replyRepository;

    /**
     * selectReply: id 에 해당하는 답글을 조회하여 Reply 객체 리턴
     */
    public Reply selectReply(Long id) {

        Reply reply = replyRepository.findById(id)
                .orElseThrow(()->{
                    return new IllegalArgumentException("해당 답글을 찾을 수 없습니다.");
                });

        return reply;
    }

    /**
     * createReplyToReply: 본 댓글에 대한 대댓글 등록
     */
    @Transactional
    public void createReplyToComment(Long post_id, Long community_id, ReplyReqDTO.CREATE create, String email) {

        User user = userService.selectUser(email);
        Post post = postService.selectPost(post_id);
        Comment comment = commentService.selectComment(community_id);
        Reply reply = null;

        if (user == null) {

            throw new IllegalArgumentException("대댓글 작성자를 찾을 수 없습니다.");
        } else {

            if (comment != null) {

                reply = Reply
                        .builder()
                        .reply(create.getReply())
                        .post(post)
                        .comment(comment)
                        .user(user)
                        .build();
            }
        }

        replyRepository.save(reply);
    }

    /**
     * createReplyToReply: 대댓글에 대한 대댓글 등록
     */
    @Transactional
    public void createReplyToReply(Long post_id, Long community_id, ReplyReqDTO.CREATE create, String email) {

        User user = userService.selectUser(email);
        Post post = postService.selectPost(post_id);
        Comment comment = commentService.selectComment(community_id);
        Reply parentReply = selectReply(create.getParent_reply_id());
        Reply reply = null;

        if (user == null) {

            throw new IllegalArgumentException("대댓글 작성자를 찾을 수 없습니다.");
        } else {

            if (comment != null) {

                reply = Reply
                        .builder()
                        .reply(create.getReply())
                        .post(post)
                        .comment(comment)
                        .parentReply(parentReply)
                        .user(user)
                        .build();
            }
        }

        replyRepository.save(reply);
    }

    /**
     * updateReply: 대댓글 수정
     */
    @Transactional
    public void updateReply(Long id, ReplyReqDTO.UPDATE update) {

        Reply reply = selectReply(id);

        if (reply != null) {

            reply.updateReply(update.getUpdateReply());
        } else {

            throw new IllegalArgumentException("해당 대댓글을 찾을 수 없습니다.");
        }
    }

    /**
     * deleteReply: 대댓글 삭제
     * 1. 대댓글에 대한 대댓글이 있는 경우 데이터베이스에서는 제거하지 않고 delete_yn 값을 'Y'로 변경
     * 2. 대댓글에 대한 대댓글이 없는 경우 데이터베이스에서 제거
     */
    @Transactional
    public void deleteReply(Long id) {

        Reply reply = selectReply(id);

        if (reply != null) {

            if (reply.getChildReplies().size() == 0) {

                replyRepository.delete(reply);
            } else {

                reply.setDelete_yn(Character.toString('Y'));
            }
        } else {

            throw new IllegalArgumentException("해당 대댓글을 찾을 수 없습니다.");
        }
    }
}
