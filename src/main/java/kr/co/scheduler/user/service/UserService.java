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

    public Map<String, String> validateHandling(BindingResult bindingResult) {
        Map<String, String> validateResult = new HashMap<>();

        for(FieldError error : bindingResult.getFieldErrors()) {
            String validKey = String.format("valid_%s", error.getField());
            validateResult.put(validKey, error.getDefaultMessage());
        }

        return validateResult;
    }
}
