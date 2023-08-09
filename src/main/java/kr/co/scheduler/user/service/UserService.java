package kr.co.scheduler.user.service;

import jakarta.servlet.http.HttpSession;
import kr.co.scheduler.user.dtos.UserReqDTO;
import kr.co.scheduler.user.dtos.UserResDTO;
import kr.co.scheduler.user.entity.User;
import kr.co.scheduler.user.enums.Role;
import kr.co.scheduler.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    /**
     * signUp: 회원가입
     *
     * 컨트롤러에서 Dto 를 받아 User 객체로 빌드하여 저장
     */
    @Transactional
    public void signUp(UserReqDTO.CREATE create) {

            User user = User.builder()
                    .email(create.getEmail())
                    .password(passwordEncoder.encode(create.getPassword()))
                    .name(create.getName())
                    .phone(create.getPhone())
                    .role(Role.USER)
                    .build();

            userRepository.save(user);
    }

    /**
     * validateCheckedPassword: 비밀번호와 확인용 비밀번호 일치여부 검증하여 boolean 타입 반환
     */
    public boolean validateCheckedPassword(String password, String checkedPassword) {

        if (!password.equals(checkedPassword)) {

            return true;
        }

        return  false;
    }

    /**
     * validateDuplication: 이미 가입된 계정인지 검증하여 boolean 타입 반환
     */
    public boolean validateDuplication(UserReqDTO.CREATE userReqDTO) {

        User user = userRepository.findByEmail(userReqDTO.getEmail());

        if (user != null) {

            return true;
        }

        return false;
    }

    /**
     * validateHandling: 회원가입에 Validation 적용
     *
     * 1. BindingResult 를 파라미터로 받음
     * 2. Map 객체를 만든 후
     * 3. bindingResult 의 모든 error 에 validKey 와 error.getDefaultMessage 를 붙여 리턴함
     */
    public Map<String, String> validateHandling(BindingResult bindingResult) {

        Map<String, String> validateResult = new HashMap<>();

        for(FieldError error : bindingResult.getFieldErrors()) {

            String validKey = String.format("valid_%s", error.getField());
            validateResult.put(validKey, error.getDefaultMessage());
        }

        return validateResult;
    }

    /**
     * searchInfo: 회원정보 조회
     * 1. 세션의 이메일을 통해 가입된 회원인지 검증
     * 2. UserResDTO 에 회원이 조회 가능한 데이터 선별하여 반환
     */
    public UserResDTO searchInfo(String email) {

        final User userInfo = userRepository.findOptionalByEmail(email)
                .orElseThrow(()->{
                    return new IllegalArgumentException("가입되지 않은 회원입니다.");
                });

        return UserResDTO.builder()
                .email(userInfo.getEmail())
                .name(userInfo.getName())
                .phone(userInfo.getPhone())
                .createdAt(userInfo.getCreatedAt())
                .updatedAt(userInfo.getUpdatedAt())
                .build();
    }

    /**
     * updateInfo: 회원정보 수정
     * 1. 가입된 회원인지 검증
     * 2. UserReqDTO 를 통해 비밀번호, 이름, 휴대전화번호를 넘겨 수정함
     */
    @Transactional
    public void updateInfo(UserReqDTO.UPDATE update, String email) {

        User user = userRepository.findOptionalByEmail(email)
                .orElseThrow(()->{
                    return new IllegalArgumentException("가입된 회원이 아닙니다.");
                });

        user.update(passwordEncoder.encode(update.getPassword()), update.getName(), update.getPhone());
    }

    /**
     * uploadProfileImg : 프로필이미지 등록 및 수정
     */
    @Transactional
    public void uploadProfileImg(String email, MultipartFile uploadImg) {

        String uploadFolder = "C:\\upload";

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

        try {
            uploadImg.transferTo(saveFile);
        } catch (Exception e) {
            e.printStackTrace();
        }

        User user = userRepository.findOptionalByEmail(email)
                .orElseThrow(()->{
                    return new IllegalArgumentException("가입된 회원이 아닙니다.");
                });

        user.setProfileImgName(uploadFileName);
        user.setProfileImgPath(uploadPath+"\\"+uploadFileName);
    }

    /**
     * deleteImgData: DB 에서 프로필이미지에 대한 데이터에 null 값을 넣음
     */
    @Transactional
    public void deleteImgData(String email, HttpSession session) {

        User user = userRepository.findByEmail(email);

            user.setProfileImgName(null);
            user.setProfileImgPath(null);
    }
}
