package kr.co.scheduler.community.service;

import kr.co.scheduler.community.dtos.PostReqDTO;
import kr.co.scheduler.community.entity.Post;
import kr.co.scheduler.community.repository.PostRepository;
import kr.co.scheduler.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final UserService userService;
    private final PostRepository postRepository;

    public void writePost(PostReqDTO.CREATE create, String email) {

        Post post = Post.builder()
                .title(create.getTitle())
                .content(create.getContent())
                .user(userService.findUser(email))
                .build();

        postRepository.save(post);
    }

    public Page<Post> viewPosts(Pageable pageable) {

        return postRepository.findAll(pageable);
    }

    public Page<Post> viewPostsOfKeyword(Pageable pageable, String keyword) {

        return postRepository.findByTitleContaining(pageable, keyword);
    }
}
