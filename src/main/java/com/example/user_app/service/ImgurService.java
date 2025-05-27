package com.example.user_app.service;

import java.io.IOException;
import java.util.Map;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImgurService {

    private final String CLIENT_ID = "myclientid";
    private final String IMGUR_API_URL = "https://api.imgur.com/3/image";

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * Upload image to Imgur and return a Map containing 'link' and 'deleteHash'.
     */
    public Map<String, String> uploadImageWithDeleteHash(MultipartFile file) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.set("Authorization", "Client-ID " + CLIENT_ID);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("image", new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        });

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(IMGUR_API_URL, requestEntity, Map.class);

        Map<String, Object> data = (Map<String, Object>) response.getBody().get("data");

        // Cast to String and return
        String link = (String) data.get("link");
        String deleteHash = (String) data.get("deletehash");

        return Map.of(
            "link", link,
            "deleteHash", deleteHash
        );
    }

    /**
     * Delete image from Imgur by deleteHash
     */
    public void deleteImage(String deleteHash) {
        String url = "https://api.imgur.com/3/image/" + deleteHash;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Client-ID " + CLIENT_ID);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        restTemplate.exchange(url, org.springframework.http.HttpMethod.DELETE, requestEntity, Map.class);
    }
}