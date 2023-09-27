package kr.co.scheduler.community.service;

import kr.co.scheduler.community.dtos.PostReqDTO;
import kr.co.scheduler.community.entity.Post;
import kr.co.scheduler.community.repository.PostRepository;
import kr.co.scheduler.global.service.ImgService;
import kr.co.scheduler.user.entity.User;
import kr.co.scheduler.user.repository.UserRepository;
import kr.co.scheduler.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final UserService userService;
    private final ImgService imgService;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    /**
     * selectPost: Post 객체 리턴
     */
    public Post selectPost(Long id) {

        return postRepository.findById(id)
                .orElseThrow(()->{
                    return new IllegalArgumentException("해당 게시글을 조회할 수 없습니다.");
                });
    }

    /**
     * createPost: 게시글 등록
     * 1. 썸머노트에 이미지 업로드 시 c:\\upload\\temp\\{email}에 이미지 저장
     * 2. 게시글 등록시 썸머노트 내용의 이미지 파일을 통해 이미지 경로 및 파일명 조회
     * 3. 해당 이미지를 c:\\upload\\post\\{email}로 이동
     * 4. c:\\upload\\temp\\{email} 경로 내 파일 모두 삭제(임시폴더를 비우는 개념)
     * 5. 게시글 등록
     */
    public void createPost(PostReqDTO.CREATE create, String email) throws IOException {

        imgService.renameImgInSummernote(create.getContent(), "C:\\upload\\post\\" + email);

        // temp 폴더 비우기
        String deletePath = "C:\\upload\\temp\\" + email;
        imgService.clearTempDir(deletePath);

        Post post = Post.builder()
                .title(create.getTitle())
                .content(create.getContent())
                .user(userService.selectUser(email))
                .build();

        postRepository.save(post);
    }

    /**
     * updatePost: 게시글 수정
     * 1. 썸머노트에 이미지 업로드 시 c:\\upload\\temp\\{email}에 이미지 저장
     * 2. 게시글 수정시 썸머노트 내용의 이미지 파일을 통해 이미지 경로 및 파일명 조회
     * 3. 해당 이미지를 c:\\upload\\post\\{email}로 이동
     * 4. c:\\upload\\temp\\{email} 경로 내 파일 모두 삭제(임시폴더를 비우는 개념)
     * 5. 게시글 수정
     */
    @Transactional
    public void updatePost(PostReqDTO.UPDATE update, String email, Long id) throws IOException {

        Post post = postRepository.findById(id)
                .orElseThrow(()->{
                    return new IllegalArgumentException("해당 게시글을 찾을 수 없습니다.");
                });

        // 기존 이미지들을 temp 폴더로 이동
        imgService.renameImgInSummernote(post.getContent(), "C:\\upload\\temp\\" + email);

        // 최종적으로 게시글에 업로드된 이미지들을 post 폴더로 이동
        imgService.renameImgInSummernote(update.getContent(), "C:\\upload\\post\\" + email);

        // temp 폴더 비우기
        String deletePath = "C:\\upload\\temp\\" + email;
        imgService.clearTempDir(deletePath);

        post.updatePost(update.getTitle(), update.getContent());
    }

    /**
     * deletePost: 게시글 삭제
     * 1. 사용자 이메일 확인
     * 2. 사용자와 게시글 작성자 동일한지 확인
     * 3. 게시글 삭제
     */
    @Transactional
    public void deletePost(Post post, String email) {

        imgService.deleteImgInSummernote(post.getContent());

        User user = userRepository.findOptionalByEmail(email)
            .orElseThrow(()->{
                return new IllegalArgumentException("가입된 이메일이 아닙니다.");
            });

        if (user != post.getUser()) {

            throw new IllegalArgumentException("작성자가 아니라면 게시글을 삭제할 수 없습니다.");
        }

        postRepository.delete(post);
    }

    // ================================== 구분 ================================== //

    /**
     * selectPosts: 게시글 목록 조회 페이지에 Page 객체 리턴
     */
    public Page<Post> selectPosts(Pageable pageable) {

        return postRepository.findAll(pageable);
    }

    /**
     * selectPostsOfKeyword: 검색어를 포함한 게시글 목록 조회 페이지에 Page 객체 리턴
     */
    public Page<Post> selectPostsOfKeyword(Pageable pageable, String keyword) {

        return postRepository.findByTitleContaining(pageable, keyword);
    }

    // ================================== 구분 ================================== //

    /**
     * updateViewCnt: 게시글 조회시 쿠키 기반의 조회수 증가
     */
    @Transactional
    public int updateViewCnt(Long id) {

        return postRepository.updateViewCnt(id);
    }
}
