package HooYah.Yacht.yacht.service;

import HooYah.Yacht.part.dto.response.PartDto;
import HooYah.Yacht.yacht.dto.response.AiAnalysisResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class YachtDefaultService {

    private final RestTemplate restTemplate;
    
    @Value("${ai.api.base-url:http://localhost:5000}")
    private String aiApiBaseUrl;

    /**
     * 요트 부품 리스트 조회
     * 1. 기본 부품 리스트 (요트 이름으로 AI 조회)
     * 2. 추가 부품 리스트 (PDF 파일 분석)
     */
    public List<PartDto> getPartList(String name, List<MultipartFile> files) {
        List<PartDto> partList = getDefaultPartList(name);
        
        if(files != null && !files.isEmpty()) {
            List<PartDto> additionalParts = getAdditionalPartList(files);
            if (additionalParts != null && !additionalParts.isEmpty()) {
                partList.addAll(additionalParts);
            }
        }

        return partList;
    }

    /**
     * 기본 부품 리스트 조회 (요트 이름으로 AI 조회)
     * AI API 호출: GET /api/yacht/analyze?yacht_name={name}
     */
    public List<PartDto> getDefaultPartList(String name) {
        try {
            String url = aiApiBaseUrl + "/api/yacht/analyze?yacht_name=" + name;
            
            log.info("🤖 AI API 호출: {}", url);
            
            ResponseEntity<AiAnalysisResponse> response = restTemplate.getForEntity(
                    url,
                    AiAnalysisResponse.class
            );
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                AiAnalysisResponse aiResponse = response.getBody();
                
                if (aiResponse != null && aiResponse.isSuccess() && aiResponse.getParts() != null) {
                    log.info("✅ AI 분석 성공: {} 부품", aiResponse.getTotalParts());
                    return convertAiPartsToPartDto(aiResponse.getParts());
                } else {
                    log.warn("⚠️ AI 분석 실패: {}", aiResponse != null ? aiResponse.getError() : "null response");
                    return getFallbackPartList(name);
                }
            } else {
                log.warn("⚠️ AI API 응답 오류: {}", response.getStatusCode());
                return getFallbackPartList(name);
            }
            
        } catch (RestClientException e) {
            log.error("❌ AI API 호출 실패, Fallback 데이터 반환", e);
            return getFallbackPartList(name);
        }
    }

    /**
     * 추가 부품 리스트 조회 (PDF 파일 분석)
     * AI API 호출: POST /api/yacht/analyze-pdf
     */
    private List<PartDto> getAdditionalPartList(List<MultipartFile> files) {
        List<PartDto> allParts = new ArrayList<>();
        
        for (MultipartFile file : files) {
            try {
                List<PartDto> parts = analyzePdfFile(file);
                if (parts != null && !parts.isEmpty()) {
                    allParts.addAll(parts);
                }
            } catch (Exception e) {
                log.error("❌ PDF 분석 실패: {}", file.getOriginalFilename(), e);
            }
        }
        
        return allParts;
    }

    /**
     * PDF 파일 분석
     * AI API 호출: POST /api/yacht/analyze-pdf
     */
    private List<PartDto> analyzePdfFile(MultipartFile file) throws IOException {
        String url = aiApiBaseUrl + "/api/yacht/analyze-pdf";
        
        log.info("🤖 AI PDF 분석 시작: {}", file.getOriginalFilename());
        
        // MultipartFile을 ByteArrayResource로 변환
        ByteArrayResource resource = new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        };
        
        // Multipart 요청 생성
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", resource);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        
        try {
            ResponseEntity<AiAnalysisResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    AiAnalysisResponse.class
            );
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                AiAnalysisResponse aiResponse = response.getBody();
                
                if (aiResponse != null && aiResponse.isSuccess() && aiResponse.getParts() != null) {
                    log.info("✅ PDF 분석 성공: {} 부품", aiResponse.getTotalParts());
                    return convertAiPartsToPartDto(aiResponse.getParts());
                } else {
                    log.warn("⚠️ PDF 분석 실패: {}", aiResponse != null ? aiResponse.getError() : "null response");
                    return new ArrayList<>();
                }
            } else {
                log.warn("⚠️ AI API 응답 오류: {}", response.getStatusCode());
                return new ArrayList<>();
            }
            
        } catch (RestClientException e) {
            log.error("❌ AI API 호출 실패", e);
            return new ArrayList<>();
        }
    }

    /**
     * AI 부품 데이터를 PartDto로 변환
     */
    private List<PartDto> convertAiPartsToPartDto(List<AiAnalysisResponse.AiPartDto> aiParts) {
        return aiParts.stream()
                .map(aiPart -> PartDto.builder()
                        .name(aiPart.getName())
                        .manufacturer(aiPart.getManufacturer())
                        .model(aiPart.getModel())
                        .interval(aiPart.getInterval() != null ? aiPart.getInterval().longValue() : null)
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * Fallback 부품 리스트 (AI 서버 다운 시)
     */
    private List<PartDto> getFallbackPartList(String name) {
        log.warn("⚠️ Fallback 데이터 반환: {}", name);
        
        // 기본 더미 데이터
        return List.of(
                PartDto.builder()
                        .name("Hull")
                        .manufacturer("Unknown")
                        .model(name + "-Hull")
                        .interval(12L)
                        .build(),
                PartDto.builder()
                        .name("Mast")
                        .manufacturer("Unknown")
                        .model(name + "-Mast")
                        .interval(12L)
                        .build(),
                PartDto.builder()
                        .name("Rudder")
                        .manufacturer("Unknown")
                        .model(name + "-Rudder")
                        .interval(6L)
                        .build()
        );
    }
}
