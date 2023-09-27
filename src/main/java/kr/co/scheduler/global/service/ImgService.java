package kr.co.scheduler.global.service;

import kr.co.scheduler.global.entity.Img;
import kr.co.scheduler.global.repository.ImgRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ImgService {

    private final ImgRepository imgRepository;

    /**
     * selectImg: Img 객체 리턴
     */
    public Img selectImg(String fileName) {

        Img img = imgRepository.findByImgName(fileName);

        return img;
    }

    /**
     * uploadImg: 이미지를 파일시스템에 저장하고, 난수로 구성된 파일명을 리턴
     */
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
     * uploadImgInSummernote: 사용자가 썸머노트에 이미지를 업로드시 파일시스템에 저장
     * 경로: c:\\upload\\temp\\{email}
     */
    @Transactional
    public String uploadImgInSummernote(MultipartFile uploadImg, String email) throws IOException {

        String uploadFolder = "C:\\upload\\temp\\" + email;

        String uploadFileName = uploadImg(uploadFolder, uploadImg);

        Img img = Img.builder()
                .imgPath(uploadFolder + "\\" + uploadFileName)
                .imgName(uploadFileName)
                .build();

        imgRepository.save(img);

        return uploadFileName;
    }

    /**
     * renameImg: 파일 이동
     */
    public void renameImg(String beforeFilePath, String replaceFolder, String fileName) throws IOException {

        File replacePath = new File(replaceFolder);

        if(replacePath.exists() == false) {
            replacePath.mkdirs();
        }

        try {
            File file = new File(beforeFilePath);

            file.renameTo(new File(replaceFolder + "\\" + fileName));
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * renameImgInSummernote: 게시글 업로드 또는 수정 시 replaceFolder 폴더로 파일 이동
     */
    @Transactional
    public void renameImgInSummernote(String content, String replaceFolder) throws IOException {

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

                Img img = selectImg(fileName);

                String beforeFilePath = img.getImgPath();

                img.setImgPath(replaceFolder + "\\" + fileName);

                // 폴더 이동
                renameImg(beforeFilePath, replaceFolder, fileName);
            }
        }
    }

    /**
     * deleteImg: 이미지 경로를 기반으로 이미지 제거
     */
    public void deleteImg(String imgPath) {

        File file = null;

        try {

            file = new File(URLDecoder.decode(imgPath, StandardCharsets.UTF_8));

            file.delete();
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    /**
     * deleteImgInSummernote: 게시글이나 프로젝트 삭제 시 업로드된 이미지 데이터 및 파일 제거
     */
    @Transactional
    public void deleteImgInSummernote(String content) {

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

                Img img = selectImg(fileName);
                imgRepository.delete(img);

                deleteImg(img.getImgPath());
            }
        }
    }

    // ================================== 구분 ================================== //

    /**
     * selectProfileImg: 프로필이미지를 뷰로 전송
     */
    public byte[] selectProfileImg(String profileImgPath) throws IOException {

        byte[] imageByteArray = null;

        InputStream inputStream = new FileInputStream(profileImgPath);
        imageByteArray = IOUtils.toByteArray(inputStream);
        inputStream.close();

        return imageByteArray;
    }

    // ================================== 구분 ================================== //

    @Transactional
    public void clearTempDir(String deletePath) {

        try {

            File folder = new File(deletePath);

            if(folder.exists()) {

                File[] file_list = folder.listFiles();
                for(File file : file_list) {

                    file.delete();

                    Img img = imgRepository.findByImgName(file.getName());
                    imgRepository.delete(img);
                }
            }

        } catch(Exception e) {

            e.printStackTrace();
        }
    }
}
