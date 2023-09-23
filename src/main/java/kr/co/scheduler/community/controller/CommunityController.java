package kr.co.scheduler.community.controller;

import kr.co.scheduler.community.entity.Post;
import kr.co.scheduler.community.service.CommentService;
import kr.co.scheduler.community.service.PostService;
import kr.co.scheduler.global.config.security.PrincipalDetails;
import kr.co.scheduler.global.service.ImgService;
import kr.co.scheduler.user.entity.User;
import kr.co.scheduler.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class CommunityController {

    private final UserService userService;
    private final PostService postService;
    private final ImgService imgService;
    private final CommentService commentService;

    /**
     * createPost: 게시글 등록 페이지 리턴
     */
    @GetMapping("/community/createPost")
    public String createPost() {

        return "community/createPost";
    }

    /**
     * selectPosts: 게시글 목록 조회 페이지 리턴
     */
    @GetMapping("/community/selectPosts")
    public String selectPosts(Model model,
                                @PageableDefault(size = 5, sort="id", direction = Sort.Direction.DESC) Pageable pageable, String keyword) {
        if (keyword == null) {
            model.addAttribute("posts", postService.selectPosts(pageable));
        } else {
            model.addAttribute("posts", postService.selectPostsOfKeyword(pageable, keyword));
        }
        return "community/selectPosts";
    }

    /**
     * selectPost: id 에 해당되는 게시글 조회 페이지 리턴
     */
    @GetMapping("/community/selectPost/{post_id}")
    public String selectPost(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model, @PathVariable(name = "post_id") Long id, @PageableDefault(size = 5, sort="post", direction = Sort.Direction.ASC) Pageable pageable) {

        Post post = postService.selectPost(id);
        User user = userService.selectUser(principalDetails.getUsername());

        model.addAttribute("post", postService.selectPost(id));
        model.addAttribute("comments", commentService.selectComments(pageable, post));
        model.addAttribute("countComment", commentService.countComments(post));

        if (user != null) {

            model.addAttribute("name", user.getName());
        }
        return "community/selectPost";
    }

    /**
     * updatePost: 게시글 수정 페이지 리턴
     */
    @GetMapping("/community/updatePost/{post_id}")
    public String updatePost(Model model, @PathVariable(name = "post_id") Long id, Principal principal) throws IOException {

        User user = userService.selectUser(principal.getName());
        Post post = postService.selectPost(id);

        if (user == post.getUser()) {

            // 수정하려는 게시물에 이미지가 있다면 temp 폴더로 파일 이동
            imgService.renameImgInSummernote(post.getContent(), principal.getName());

            model.addAttribute("post", post);

            return "community/updatePost";
        } else {

            return "redirect:community/selectPosts";
        }
    }
}
