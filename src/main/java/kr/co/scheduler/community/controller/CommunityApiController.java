package kr.co.scheduler.community.controller;

import kr.co.scheduler.community.dtos.PostReqDTO;
import kr.co.scheduler.community.service.PostService;
import kr.co.scheduler.global.dtos.ResponseDto;
import kr.co.scheduler.global.entity.Img;
import kr.co.scheduler.global.repository.ImgRepository;
import kr.co.scheduler.user.entity.User;
import kr.co.scheduler.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class CommunityApiController {

    private final PostService postService;
    private final UserService userService;
    private final ImgRepository imgRepository;

    @PostMapping("/api/community/write")
    public ResponseDto<Object> writePost(@RequestBody PostReqDTO.CREATE create, Principal principal) {

        postService.writePost(create, principal.getName());

        return ResponseDto.ofSuccessData(
                "게시물이 정상적으로 등록되었습니다.",
                null);
    }

    /**
     * getProfileImg: 프로필이미지를 뷰로 전송
     * 뷰에서 바이트 단위의 데이터를 받기 위해서는
     * HttpStatus, HttpHeaders, HttpBody 를 모두 포함해야하기 때문에
     * ResponseDTO 가 아닌 ResponseEntity 를 응답한다.
     */
    @GetMapping("/api/community/post/profileImg/{email}")
    public ResponseEntity<?> getProfileImg(@PathVariable(name = "email") String email) throws IOException {

        User user = userService.findUser(email);

        if(user.getProfileImgPath() == null) {

            InputStream inputStream = getClass().getResourceAsStream("/static/image/profile-spap.png");

            byte[] imageByteArray = IOUtils.toByteArray(inputStream);
            inputStream.close();

            return new ResponseEntity<>(imageByteArray, HttpStatus.OK);
        } else {

            InputStream inputStream = new FileInputStream(user.getProfileImgPath());

            byte[] imageByteArray = IOUtils.toByteArray(inputStream);
            inputStream.close();

            return new ResponseEntity<>(imageByteArray, HttpStatus.OK);
        }
    }

    /**
     * uploadPostImg: 게시글 이미지를 등록 및 수정
     * 1. 업로드 된 게시글 이미지 등록
     */
    @PostMapping("/api/community/postImg/upload")
    public ResponseEntity<?> uploadPostImg(@RequestParam("file") MultipartFile uploadImg, Principal principal) throws IOException {

        String uploadFolder = "C:\\upload\\temp\\" + principal.getName();

        File uploadPath = new File(uploadFolder);

        if(uploadPath.exists() == false) {
            uploadPath.mkdirs();
        }

        String uploadFileName = uploadImg.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();

        uploadFileName = uuid + "_" + uploadFileName;

        File saveFile = new File(uploadPath, uploadFileName);

        System.out.println(uploadFolder + "\\" + uploadFileName);
        System.out.println(uploadFileName);

        Img image = Img.builder()
                .imgPath(uploadFolder + "\\" + uploadFileName)
                .imgName(uploadFileName)
                .build();
        imgRepository.save(image);
        uploadImg.transferTo(saveFile);

        return ResponseEntity.ok("/api/community/postImg/get?uploadFileName=" + uploadFileName);
    }

    @GetMapping("/api/community/postImg/get")
    public ResponseEntity<?> getPostImg(@RequestParam String uploadFileName) throws IOException {

        Img img = imgRepository.findByImgName(uploadFileName);

        InputStream inputStream = new FileInputStream(img.getImgPath());
        byte[] imageByteArray = IOUtils.toByteArray(inputStream);
        inputStream.close();

        return new ResponseEntity<>(imageByteArray, HttpStatus.OK);
    }
}
