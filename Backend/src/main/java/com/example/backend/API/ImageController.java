package com.example.backend.API;

//import org.springframework.http.*;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.client.RestTemplate;
//
//import java.io.IOException;
//import java.util.Base64;
//
//@RestController
//public class ImageController {
//
//    @PostMapping("/upload")
//    public ResponseEntity<String> uploadImage(@RequestParam("base64Image") String base64Image) throws IOException {
//        try {
//            // Base64 kodunu byte dizisine dönüştür
//            byte[] imageBytes = Base64.getDecoder().decode(base64Image);
//
//            // ImageProcessing API'ye gönder
//            String response = sendImageToProcessingApi(imageBytes);
//
//            // İşleme API'sinden gelen yanıtı döndür
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            // Hata durumunda 500 Internal Server Error döndür
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing the image.");
//        }
//    }
//
//    private String sendImageToProcessingApi(byte[] imageBytes) throws IOException {
//        try {
//            // HttpHeaders nesnesi oluştur
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.valueOf("image/jpg"));
//
//            // HttpEntity oluştur
//            HttpEntity<byte[]> request = new HttpEntity<>(imageBytes, headers);
//
//            // İşleme API'sine bir POST isteği gönder
//            ResponseEntity<String> response = new RestTemplate().postForEntity("http://127.0.0.1:5000/process", request, String.class);
//
//            // İşleme API'sinden gelen yanıtı döndür
//            return response.getBody();
//        } catch (Exception e) {
//            // Hata durumunda 500 Internal Server Error döndür
//            return String.valueOf(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing the image."));
//        }
//    }
//}
//

import com.example.backend.Dtos.Response;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class ImageController {
    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("image") MultipartFile file) {
        try {
            forwardImage(file);
            return ResponseEntity.ok("Resim başarıyla alındı ve yönlendirildi");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/response")
    public ResponseEntity<String> handlePostRequest(@RequestBody Map<String, Object> payload) {
        Response response = new Response();
        response.setJson(payload.toString());
        return ResponseEntity.ok(response.getJson());
    }

    @GetMapping("/responseSend")
    public ResponseEntity<String> handleGetRequest(@RequestBody Map<String, Object> payload) {
        Response response = new Response();
        response.setJson(payload.toString());
        return ResponseEntity.ok(response.getJson());
    }

    public void forwardImage(MultipartFile file) throws Exception {
        String apiUrl = "http://127.0.0.1:5000/upload";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("image", file.getResource());
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, requestEntity, String.class);
        System.out.println(response.getBody());
    }
}
