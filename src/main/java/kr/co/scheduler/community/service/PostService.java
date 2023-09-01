package kr.co.scheduler.community.service;

import kr.co.scheduler.community.dtos.PostReqDTO;
import kr.co.scheduler.community.entity.Post;
import kr.co.scheduler.community.repository.PostRepository;
import kr.co.scheduler.global.entity.Img;
import kr.co.scheduler.global.repository.ImgRepository;
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

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final UserService userService;
    private final PostRepository postRepository;
    private final ImgRepository imgRepository;

    public void writePost(PostReqDTO.CREATE create, String email) {

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

                Img img = imgRepository.findByImgName(fileName);

                String beforeFilePath = img.getImgPath();
                String replaceFolder = "C:\\upload\\post\\";

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date = new Date();
                String str = sdf.format(date);
                String datePath = str.replace("-", File.separator);

                img.setImgPath(replaceFolder + "\\" + datePath + "\\" + fileName);

                File replacePath = new File(replaceFolder, datePath);

                if(replacePath.exists() == false) {
                    replacePath.mkdirs();
                }

                try {
                    File file = new File(beforeFilePath);

                    file.renameTo(new File(replaceFolder + "\\" + datePath + "\\" + fileName));
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }

        // temp 폴더 비우기
        String deletePath = "C:\\upload\\temp\\" + email;

        try {

            File folder = new File(deletePath);

            if(folder.exists()) {

                File[] file_list = folder.listFiles();
                for(File file : file_list) {

                    file.delete();
                }
            }

        } catch(Exception e) {

            e.printStackTrace();
        }

        Post post = Post.builder()
                .title(create.getTitle())
                .content(content)
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

    public Post viewOneOfPost(Long id) {

        return postRepository.findById(id)
                .orElseThrow(()->{
                   return new IllegalArgumentException("해당 게시글을 조회할 수 없습니다.");
                });
    }
}
