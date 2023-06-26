package kr.co.scheduler.user.service;

import kr.co.scheduler.user.dtos.UserReqDTO;
import kr.co.scheduler.user.entity.User;
import kr.co.scheduler.user.repository.UserRepository;
import kr.co.scheduler.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * signUp : 회원가입 기능
     */
    @Transactional
    public Long signUp(UserReqDTO userReqDTO) throws Exception {

        // 회원가입 하려는 이메일이 이미 등록되어 있는지 확인
        if(userRepository.findByEmail(userReqDTO.getEmail()).isPresent()){
            throw new Exception("이미 존재하는 이메일입니다.");
        }

        // 패스워드와 확인용 패스워드가 일치하는지 확인
        if(!userReqDTO.getPassword().equals(userReqDTO.getCheckedPassword())){
            throw new Exception("비밀번호가 일치하지 않습니다.");
        }

        // DB에 User 객체를 저장
        User user = userRepository.save(userReqDTO.toEntity());
        
        // User 객체에서 PasswordEncoder를 통해 패스워드를 암호화하여 저장
        user.encodePassword(passwordEncoder);

        // User 객체에 role(USER) 저장
        user.addUserAuthority();

        return user.getId();
    }

    /**
     * login : 로그인 기능
     */
    public String login(UserReqDTO userReqDTO) {
        
        // 로그인 폼에 입력한 이메일 식별
        // 회원가입 되어있다면 user 객체에 해당 사용자정보를 저장
        User user = userRepository.findByEmail(userReqDTO.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 Email 입니다."));

        // 로그인 폼에 입력한 패스워드 식별
        String password = userReqDTO.getPassword();
        if (!user.matchPassword(passwordEncoder, password)) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }

        // 해당 사용자 정보에서 권한 수집
        String role = user.getRole().name();

        return jwtTokenProvider.createToken(user.getUsername(), role);
    }
}
