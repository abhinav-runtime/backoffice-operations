package com.backoffice.operations.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.backoffice.operations.payloads.EditInfoRequestDto;
import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.backoffice.operations.service.DashboardService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import com.backoffice.operations.payloads.CardDTO;
import com.backoffice.operations.utils.PinGenerationUtil;

@RestController
@RequestMapping("/api/v1/dashboard")
public class DashboardController {

    private final WebClient.Builder webClientBuilder;

    private final DashboardService dashboardService;

    public DashboardController(WebClient.Builder webClientBuilder, DashboardService dashboardService) {
        this.webClientBuilder = webClientBuilder;
        this.dashboardService = dashboardService;
    }

    @GetMapping("/urls")
    public GenericResponseDTO<List<String>> getUrls() {
        List<String> urlList = Arrays.asList(
                "http://182.18.138.199/Alizz/1.png",
                "http://182.18.138.199/Alizz/3.png",
                "http://182.18.138.199/Alizz/2.png"
        );

        GenericResponseDTO<List<String>> genericUrlList = new GenericResponseDTO<>();
        genericUrlList.setStatus("Success");
        genericUrlList.setMessage("Success");
        genericUrlList.setData(urlList);

        // Convert the list of URLs to a list of UrlResponse objects
        // return urlList.stream().map(UrlResponse::new).collect(Collectors.toList());
        return genericUrlList;
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

    @GetMapping()
    public ResponseEntity<GenericResponseDTO<Object>> getDashboardDetails(@RequestParam String uniqueKey){
        return ResponseEntity.ok(dashboardService.getDashboardDetails(uniqueKey));
    }

    @GetMapping("/info")
    public ResponseEntity<GenericResponseDTO<Object>> getDashboardInfo(@RequestParam String uniqueKey){
        return ResponseEntity.ok(dashboardService.getDashboardInfo(uniqueKey));
    }

    @GetMapping("/accountTransactions")
    public ResponseEntity<GenericResponseDTO<Object>> getAccountTransactions(@RequestParam String accountNumber, @RequestParam(required = false) String fromDate
            , @RequestParam(required = false) String toDate,@RequestParam String uniqueKey){
        return ResponseEntity.ok(dashboardService.getAccountTransactions(accountNumber, fromDate, toDate, uniqueKey));
    }

    @GetMapping("/creditCardTransactions")
    public ResponseEntity<GenericResponseDTO<Object>> getCreditCardTransactions(@RequestParam(required = false) String fromDate
            , @RequestParam(required = false) String toDate,@RequestParam String uniqueKey){
        return ResponseEntity.ok(dashboardService.getCreditCardTransactions(fromDate, toDate, uniqueKey));
    }

    @GetMapping("/upcomingBills")
    public ResponseEntity<GenericResponseDTO<Object>> getUpComingBills(@RequestParam String uniqueKey){
        return ResponseEntity.ok(dashboardService.getUpComingBills(uniqueKey));
    }

    @PostMapping("/editInfo")
    public ResponseEntity<GenericResponseDTO<Object>> editInfo(@RequestBody EditInfoRequestDto editInfoRequestDto){
        return ResponseEntity.ok(dashboardService.editInfo(editInfoRequestDto));
    }
}
