package HooYah.Yacht.conf;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
public class FCMConfig {

    @Value("${firebase.credentials}")
    private Resource credentials;

    @PostConstruct
    public void init() throws IOException {
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(credentials.getInputStream()))
                .build();

        FirebaseApp.initializeApp(options);
    }
}
