package com.backoffice.operations.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.backoffice.operations.payloads.CardDTO;
import com.backoffice.operations.payloads.UrlResponse;
import com.backoffice.operations.utils.PinGenerationUtil;

@RestController
@RequestMapping("/api/v1/dashboard")
public class DashboardController {
	
	private final WebClient.Builder webClientBuilder;

    public DashboardController(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    @GetMapping("/urls")
    public List<UrlResponse> getUrls() {
        List<String> urls = Arrays.asList(
                "http://182.18.138.199/Alizz/1.png",
                "http://182.18.138.199/Alizz/3.png",
                "http://182.18.138.199/Alizz/2.png"
        );

        // Convert the list of URLs to a list of UrlResponse objects
        return urls.stream()
                .map(UrlResponse::new)
                .collect(Collectors.toList());
    }
    
    @PostMapping("/encrypt")
    public String encryptPin(@RequestBody CardDTO pinRequestDTO) throws Exception {
        String setPinKey = pinRequestDTO.getSetPinKey();
        String kitNo = pinRequestDTO.getKitNo();
        String pin = pinRequestDTO.getPin();

        // Encryption
        String formattedPinBlock = PinGenerationUtil.format0Encode(pin, kitNo);
        String encryptedPin = PinGenerationUtil.encrypt(formattedPinBlock, setPinKey);

        return encryptedPin;
    }
	
	@GetMapping(value = "/image", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getImage() throws IOException {
		List<String> urls = Arrays.asList(
                "http://182.18.138.199/Alizz/1.png",
                "http://182.18.138.199/Alizz/3.png",
                "http://182.18.138.199/Alizz/2.png"
        );

        // Create a buffer for storing image bytes
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            for (String url : urls) {
                byte[] imageBytes = webClientBuilder.build()
                        .get()
                        .uri(url)
                        .retrieve()
                        .bodyToMono(byte[].class)
                        .block();

                if (imageBytes != null) {
                    outputStream.write(imageBytes);
                }
            }

            byte[] combinedImageBytes = outputStream.toByteArray();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            headers.setContentLength(combinedImageBytes.length);

            return new ResponseEntity<>(combinedImageBytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            // Handle exceptions
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
	
}
