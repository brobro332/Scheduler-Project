package kr.co.scheduler.community.controller;

import kr.co.scheduler.community.entity.Post;
import kr.co.scheduler.community.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class CommunityController {

    private final PostService postService;

    @GetMapping("/community/write")
    public String writePost() {

        return "community/write";
    }

    @GetMapping("/community/view")
    public String viewPosts(Model model,
                                @PageableDefault(size = 5, sort="id", direction = Sort.Direction.DESC) Pageable pageable, String keyword) {
        if (keyword == null) {
            model.addAttribute("posts", postService.viewPosts(pageable));
        } else {
            model.addAttribute("posts", postService.viewPostsOfKeyword(pageable, keyword));
        }
        return "community/view";
    }

    @GetMapping("/community/view/post/{post_id}")
    public String viewOneOfPost(Model model, @PathVariable(name = "post_id") Long id) {

        model.addAttribute("post", postService.viewOneOfPost(id));

        return "community/post";
    }

    @GetMapping("/community/update/{post_id}")
    public String updatePost(Model model, @PathVariable(name = "post_id") Long id) {

        Post post = postService.viewOneOfPost(id);

        postService.updateImg(post);

        model.addAttribute("post", post);

        return "community/update";
        }
    }
