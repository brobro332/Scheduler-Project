package kr.co.scheduler.global.config.fcm;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Service
public class FCMConfig {
    @PostConstruct
    public void FCMInit() {

        try {
            Resource resource = new ClassPathResource("static/json/scheduler-project-fad66-firebase-adminsdk-dnukg-c5b65d34ea.json");
            InputStream inputStream = resource.getInputStream();

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(inputStream))
                    .build();

            FirebaseApp.initializeApp(options);

        } catch (IOException e) {

            e.printStackTrace();
        }
    }
}
