package kr.co.scheduler.community.controller;

import kr.co.scheduler.community.dtos.PostReqDTO;
import kr.co.scheduler.community.service.PostService;
import kr.co.scheduler.global.dtos.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class CommunityApiController {

    private final PostService postService;

    @PostMapping("/api/community/write")
    public ResponseDto<Object> writePost(@RequestBody PostReqDTO.CREATE create, Principal principal) {

        postService.writePost(create, principal.getName());
        System.out.println(create.getTitle());
        System.out.println(create.getContent());

        return ResponseDto.ofSuccessData(
                "게시물이 정상적으로 등록되었습니다.",
                null);
    }
}
