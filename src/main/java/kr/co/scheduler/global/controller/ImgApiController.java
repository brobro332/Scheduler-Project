package kr.co.scheduler.global.controller;

import kr.co.scheduler.global.entity.Img;
import kr.co.scheduler.global.service.ImgService;
import kr.co.scheduler.user.entity.User;
import kr.co.scheduler.user.service.UserService;
import lombok.RequiredArgsConstructor;

import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class ImgApiController {

    private final ImgService imgService;
    private final UserService userService;

    /**
     * selectProfileImg: 프로필이미지를 뷰로 전송
     * 뷰에서 바이트 단위의 데이터를 받기 위해서는
     * HttpStatus, HttpHeaders, HttpBody 를 모두 포함해야하기 때문에
     * ResponseDTO 가 아닌 ResponseEntity 를 응답한다.
     */
    @GetMapping("/api/profileImg/{email}")
    public ResponseEntity<?> selectProfileImg(@PathVariable(name = "email") String email) throws IOException {

        User user = userService.selectUser(email);
        String profileImgPath = user.getProfileImgPath();
        InputStream inputStream;
        byte[] imageByteArray = null;

        if (profileImgPath == null) {

            inputStream = getClass().getResourceAsStream("/static/image/profile-spap.png");
            imageByteArray = IOUtils.toByteArray(inputStream);
            inputStream.close();
        } else {

            imageByteArray = imgService.selectProfileImg(profileImgPath);
        }

        return new ResponseEntity<>(imageByteArray, HttpStatus.OK);
    }

    /**
     * selectProfileImgByPrincipal: 사용자의 프로필이미지를 뷰로 전송
     * 뷰에서 바이트 단위의 데이터를 받기 위해서는
     * HttpStatus, HttpHeaders, HttpBody 를 모두 포함해야하기 때문에
     * ResponseDTO 가 아닌 ResponseEntity 를 응답한다.
     */
    @GetMapping("/api/profileImg")
    public ResponseEntity<?> selectProfileImgByPrincipal(Principal principal) throws IOException {

        User user = userService.selectUser(principal.getName());
        byte[] imageByteArray = null;

        if (user != null) {

            imageByteArray = imgService.selectProfileImg(user.getProfileImgPath());
        }

        return new ResponseEntity<>(imageByteArray, HttpStatus.OK);
    }

    // ================================== 구분 ================================== //

    /**
     * getPostImg: 썸머노트에 이미지 업로드시 뷰에 이미지를 띄움
     */
    @GetMapping("/api/summernoteImg")
    public ResponseEntity<?> getPostImg(@RequestParam String uploadFileName) throws IOException {

        Img img = imgService.selectImg(uploadFileName);

        InputStream inputStream = new FileInputStream(img.getImgPath());
        byte[] imageByteArray = IOUtils.toByteArray(inputStream);
        inputStream.close();

        return new ResponseEntity<>(imageByteArray, HttpStatus.OK);
    }

    /**
     * uploadPostImg: 이미지를 등록 및 수정
     * 1. 썸머노트에 업로드한 이미지를 파일 시스템에 저장
     * 2. 썸머노트 내용에 <img>~</img>태그 저장
     */
    @PostMapping("/api/summernoteImg")
    public ResponseEntity<?> uploadPostImg(@RequestParam("file") MultipartFile uploadImg, Principal principal) throws IOException {

        String uploadFileName = imgService.uploadImgInSummernote(uploadImg, principal.getName());

        return ResponseEntity.ok("/api/summernoteImg?uploadFileName=" + uploadFileName);
    }
}