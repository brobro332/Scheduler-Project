package kr.co.scheduler.community.controller;

import kr.co.scheduler.community.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
                                @PageableDefault(size = 10, sort="id", direction = Sort.Direction.DESC) Pageable pageable, String keyword) {
        if (keyword == null) {
            model.addAttribute("posts", postService.viewPosts(pageable));
        } else {
            model.addAttribute("posts", postService.viewPostsOfKeyword(pageable, keyword));
        }
        return "community/view";
    }
}
