package HooYah.Yacht.conf;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * RestTemplate 설정
 * AI API 호출을 위한 HTTP 클라이언트 설정
 */
@Configuration
public class RestTemplateConfig {
    
    /**
     * RestTemplate 빈 등록
     * - 연결 타임아웃: 5초
     * - 읽기 타임아웃: 30초 (AI 분석 시간 고려)
     */
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5000);  // 5초
        factory.setReadTimeout(30000);    // 30초 (AI 분석 시간)
        
        return builder
                .requestFactory(() -> factory)
                .build();
    }
}

