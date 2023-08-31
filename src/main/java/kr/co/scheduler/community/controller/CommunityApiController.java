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
import java.text.SimpleDateFormat;
import java.util.Date;
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
        System.out.println(create.getTitle());
        System.out.println(create.getContent());

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

        System.out.println(email);
        User user = userService.findUser(email);

        InputStream inputStream = new FileInputStream(user.getProfileImgPath());
        byte[] imageByteArray = IOUtils.toByteArray(inputStream);
        inputStream.close();

        return new ResponseEntity<>(imageByteArray, HttpStatus.OK);
    }

    /**
     * uploadPostImg: 게시글 이미지를 등록 및 수정
     * 1. 업로드 된 게시글 이미지 등록
     */
    @PostMapping("/api/community/postImg/upload")
    public ResponseEntity<?> uploadPostImg(@RequestParam("file") MultipartFile uploadImg) throws IOException {

        String uploadFolder = "C:\\upload\\post\\";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String str = sdf.format(date);
        String datePath = str.replace("-", File.separator);

        File uploadPath = new File(uploadFolder, datePath);

        if(uploadPath.exists() == false) {
            uploadPath.mkdirs();
        }

        String uploadFileName = uploadImg.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();

        uploadFileName = uuid + "_" + uploadFileName;

        File saveFile = new File(uploadPath, uploadFileName);


        Img image = Img.builder()
                .imgPath(uploadFolder + datePath + "\\" + uploadFileName)
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
