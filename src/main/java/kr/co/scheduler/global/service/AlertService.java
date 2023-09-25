package kr.co.scheduler.global.service;

import kr.co.scheduler.global.entity.Alert;
import kr.co.scheduler.global.entity.AlertUser;
import kr.co.scheduler.global.repository.AlertUserRepository;
import kr.co.scheduler.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AlertService {

    private final AlertUserRepository alertUserRepository;

    @Transactional
    public void createAlert(String content, User user) {

        Alert alert = Alert
                .builder()
                .content(content)
                .build();

        AlertUser alertUser = AlertUser
                .builder()
                .alert(alert)
                .user(user)
                .build();

        alertUserRepository.save(alertUser);
    }
}
