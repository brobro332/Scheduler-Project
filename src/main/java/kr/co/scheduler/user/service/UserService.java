package kr.co.scheduler.user.service;

import kr.co.scheduler.user.dtos.UserReqDTO;
import kr.co.scheduler.user.entity.User;
import kr.co.scheduler.user.enums.Role;
import kr.co.scheduler.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    /**
     * signUp: 회원가입
     *
     * 1. 컨트롤러에서 Dto 를 받아 User 객체로 빌드하여 저장
     */
    @Transactional
    public void signUp(UserReqDTO userReqDTO) {

            User user = User.builder()
                    .email(userReqDTO.getEmail())
                    .password(passwordEncoder.encode(userReqDTO.getPassword()))
                    .name(userReqDTO.getName())
                    .phone(userReqDTO.getPhone())
                    .role(Role.USER)
                    .build();

            userRepository.save(user);
    }

    /**
     * validateDuplication: 이미 가입된 계정인지 검증
     */
    public boolean validateDuplication(UserReqDTO userReqDTO) {

        User user = userRepository.findByEmail(userReqDTO.getEmail());

        if (user != null) {

            return true;
        } else {

            return false;
        }
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
}
