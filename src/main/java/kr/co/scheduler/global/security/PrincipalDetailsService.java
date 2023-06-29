package kr.co.scheduler.global.security;

import kr.co.scheduler.user.entity.User;
import kr.co.scheduler.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User principal = userRepository.findOptionalByEmail(username)
                .orElseThrow(() -> {
                    return new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다.");
                });

        return new PrincipalDetails(principal);
    }
}
