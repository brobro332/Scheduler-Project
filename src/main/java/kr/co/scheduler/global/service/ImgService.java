package kr.co.scheduler.global.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ImgService {

    /**
     * uploadImg: 이미지를 파일시스템에 저장하고, 난수로 구성된 파일명을 리턴
     */
    @Transactional
    public String uploadImg(String uploadFolder, MultipartFile uploadImg){

        File uploadPath = new File(uploadFolder);

        if(uploadPath.exists() == false) {
            uploadPath.mkdirs();
        }

        String uploadFileName = uploadImg.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();

        uploadFileName = uuid + "_" + uploadFileName;

        File saveFile = new File(uploadPath, uploadFileName);

        try {
            uploadImg.transferTo(saveFile);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return uploadFileName;
    }

    /**
     * 이미지 경로를 기반으로 이미지 제거
     */
    @Transactional
    public void deleteImg(String imgPath) throws IOException {

        File file = null;

        try {

            file = new File(URLDecoder.decode(imgPath, StandardCharsets.UTF_8));
            file.delete();
        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}
