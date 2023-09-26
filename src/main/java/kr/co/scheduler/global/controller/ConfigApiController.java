package kr.co.scheduler.global.controller;

import kr.co.scheduler.global.config.fcm.FirebaseProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ConfigApiController {

    private final FirebaseProperties firebaseProperties;

    @GetMapping("/api/var")
    public ResponseEntity<FirebaseProperties> selectVar() {
        return ResponseEntity.ok(firebaseProperties);
    }
}
