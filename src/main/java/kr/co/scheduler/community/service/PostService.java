package kr.co.scheduler.community.service;

import kr.co.scheduler.community.dtos.PostReqDTO;
import kr.co.scheduler.community.entity.Post;
import kr.co.scheduler.community.repository.PostRepository;
import kr.co.scheduler.global.entity.Img;
import kr.co.scheduler.global.repository.ImgRepository;
import kr.co.scheduler.global.service.ImgService;
import kr.co.scheduler.user.entity.User;
import kr.co.scheduler.user.repository.UserRepository;
import kr.co.scheduler.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final UserService userService;
    private final ImgService imgService;
    private final PostRepository postRepository;
    private final ImgRepository imgRepository;
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

        String content = create.getContent();
        Document doc = Jsoup.parse(content);
        Elements imgElements = doc.select("img");

        String fileName = null;
        for(Element imgElement : imgElements) {

            // src 추출
            String srcValue = imgElement.attr("src");

            // 파일명 추출
            String[] parts = srcValue.split("=");
            if(parts.length == 2) {
                fileName = parts[1];

                // 썸머노트에 업로드할 때 저장된 이미지의 경로와 파일명 추출
                Img img = imgRepository.findByImgName(fileName);

                String beforeFilePath = img.getImgPath();
                String replaceFolder = "C:\\upload\\post\\" + email;

                img.setImgPath(replaceFolder + "\\" + fileName);

                // 폴더 이동
                imgService.renameImg(beforeFilePath, replaceFolder, fileName);
            }
        }

        // temp 폴더 비우기
        String deletePath = "C:\\upload\\temp\\" + email;

        imgService.clearTempDir(deletePath);

        Post post = Post.builder()
                .title(create.getTitle())
                .content(content)
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

        String content = update.getContent();
        Document doc = Jsoup.parse(content);
        Elements imgElements = doc.select("img");

        String fileName = null;
        for(Element imgElement : imgElements) {

            // src 추출
            String srcValue = imgElement.attr("src");

            // 파일명 추출
            String[] parts = srcValue.split("=");
            if(parts.length == 2) {
                fileName = parts[1];

                // 썸머노트에 업로드할 때 저장된 이미지의 경로와 파일명 추출
                Img img = imgRepository.findByImgName(fileName);

                String beforeFilePath = img.getImgPath();
                String replaceFolder = "C:\\upload\\post\\" + email;

                img.setImgPath(replaceFolder + "\\" + fileName);

                // 폴더 이동
                imgService.renameImg(beforeFilePath, replaceFolder, fileName);
            }
        }

        // temp 폴더 비우기
        String deletePath = "C:\\upload\\temp\\" + email;

        imgService.clearTempDir(deletePath);

        Post post = postRepository.findById(id)
                        .orElseThrow(()->{
                            return new IllegalArgumentException("해당 게시글을 찾을 수 없습니다.");
                        });

        post.updatePost(update.getTitle(), content);
    }

    /**
     * deletePost: 게시글 삭제
     * 1. 사용자 이메일 확인
     * 2. 사용자와 게시글 작성자 동일한지 확인
     * 3. 게시글 삭제
     */
    @Transactional
    public void deletePost(Post post, String email) throws IOException {

        String content = post.getContent();
        Document doc = Jsoup.parse(content);
        Elements imgElements = doc.select("img");

        String fileName = null;
        for (Element imgElement : imgElements) {

            // src 추출
            String srcValue = imgElement.attr("src");

            // 파일명 추출
            String[] parts = srcValue.split("=");
            if (parts.length == 2) {
                fileName = parts[1];

                Img img = imgService.selectImg(fileName);
                imgRepository.delete(img);

                imgService.deleteImg(img.getImgPath());
            }
        }

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
     * increaseView_cnt: 게시글 조회시 조회수 증가
     * 수정사항: 쿠키 기반의 조회수 증가 로직으로 구현해야함
     */
    @Transactional
    public int increaseView_cnt(Long id) {

        return postRepository.updateView_cnt(id);
    }
}
