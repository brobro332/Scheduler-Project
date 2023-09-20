package kr.co.scheduler.global.config.fcm;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;

@Service
public class FCMConfig {
    @PostConstruct
    public void FCMInit() {

        try {
            FileInputStream serviceAccount = new FileInputStream("src/main/resources/static/json/scheduler-project-fad66-firebase-adminsdk-dnukg-812b98b891.json");
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            FirebaseApp.initializeApp(options);

        } catch (IOException e) {

            e.printStackTrace();
        }
    }
}
