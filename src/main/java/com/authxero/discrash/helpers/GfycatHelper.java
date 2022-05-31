package com.authxero.discrash.helpers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.file.Path;
import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class GfycatHelper {
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final RestTemplate restTemplate = new RestTemplate();

    public static String uploadVideo(Path fileInput) {
        try {
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            HttpHeaders headers1 = new HttpHeaders();
            headers1.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers1.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> request = new HttpEntity<>("{ \"title\": \"" + fileInput.getFileName() + "\" }", headers1);
            ResponseEntity<String> response1 = restTemplate.postForEntity("https://api.gfycat.com/v1/gfycats", request, String.class);
            JsonNode jn = mapper.readTree(response1.getBody());
            String name = jn.get("gfyname").asText();
            //System.out.println(name);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("key", name);
            body.add("file", new FileSystemResource(fileInput));
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
            String serverUrl = "https://filedrop.gfycat.com/";
            ResponseEntity<String> response = restTemplate.postForEntity(serverUrl, requestEntity, String.class);
            //System.out.print(response);
            return response.getStatusCode() == HttpStatus.NO_CONTENT ? getGfyUrl(name) : "Failed to upload to gfycat!";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to upload to gfycat!";
        }
    }

    private static String getGfyUrl(String key) throws Exception {
        TimeUnit.SECONDS.sleep(8);
        ResponseEntity<String> response = restTemplate.getForEntity("https://api.gfycat.com/v1/gfycats/fetch/status/"+key,String.class);
        JsonNode jn = mapper.readTree(response.getBody());
        JsonNode name = jn.get("gfyname");
        int attempts = 0;
        while (Objects.isNull(name)){
            attempts++;
            TimeUnit.SECONDS.sleep(6);
            ResponseEntity<String> responseLoop = restTemplate.getForEntity("https://api.gfycat.com/v1/gfycats/fetch/status/"+key,String.class);
            JsonNode jnLoop = mapper.readTree(responseLoop.getBody());
            name = jnLoop.get("gfyname");
            if(attempts > 5){
                throw new Exception("Failed to get gif name from Gfycat!");
            }
        }
        return "https://gfycat.com/" + name.asText();
    }
}

