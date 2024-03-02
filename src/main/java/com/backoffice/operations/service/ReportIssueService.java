package com.backoffice.operations.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.backoffice.operations.entity.ReportAnIssue;
import com.backoffice.operations.entity.User;
import com.backoffice.operations.exceptions.BlogAPIException;
import com.backoffice.operations.payloads.ReportAnIssueDto;
import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.backoffice.operations.repository.ReportIssueRepository;
import com.backoffice.operations.repository.UserRepository;
import com.backoffice.operations.security.JwtTokenProvider;

@Service
public class ReportIssueService {

    private final List<String> ALLOWED_FILE_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png");
    private final long MAX_FILE_SIZE_BYTES = 2 * 1024 * 1024; // 1MB
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ReportIssueRepository reportIssueRepository;

    public GenericResponseDTO<Object> postReportIssue(ReportAnIssueDto reportAnIssueDto, String path,
                                                      MultipartFile file, String token) throws IOException {

        GenericResponseDTO<Object> responseDTO = new GenericResponseDTO<>();
        String userEmail = jwtTokenProvider.getUsername(token);
        Optional<User> user = userRepository.findByEmail(userEmail);
        ReportAnIssue reportAnIssue = new ReportAnIssue();
        if (user.isPresent()) {
            if (!file.isEmpty()) {
                // file size validation
                if (file.getSize() > MAX_FILE_SIZE_BYTES) {
                    throw new BlogAPIException(HttpStatus.BAD_REQUEST, "File size should be below 2MB!.");
                }

                // file extension validation
                String fileExtension = getFileExtension(file.getOriginalFilename());
                if (!ALLOWED_FILE_EXTENSIONS.contains(fileExtension.toLowerCase())) {
                    throw new BlogAPIException(HttpStatus.BAD_REQUEST, "File type Should be 'jpg', 'jpeg', 'png'.");
                }

                // FullPath
                String filePath = path + File.separator + file.getOriginalFilename();

                // Create folder if not created
                File f = new File(path);
                if (!f.exists()) {
                    f.mkdir();
                }

                // file copy
                Files.copy(file.getInputStream(), Paths.get(filePath));
                reportAnIssue.setFilePath(filePath);
                reportAnIssue.setName(file.getOriginalFilename());
                reportAnIssue.setType(file.getContentType());
            }
            reportAnIssue.setTypeOfIssue(reportAnIssueDto.getTypeOfIssue());
            reportAnIssue.setMessage(reportAnIssueDto.getMessage());
            reportAnIssue.setTime(LocalDateTime.now());
			reportAnIssue.setLang(reportAnIssueDto.getLang());

            reportIssueRepository.save(reportAnIssue);

            Map<String, Object> data = new HashMap<>();
            responseDTO.setStatus("Success");
            responseDTO.setMessage("Issue reported successfully!");
            responseDTO.setData(data);
            return responseDTO;
        }
        responseDTO.setStatus("Failure");
        responseDTO.setMessage("Something went wrong");
        return responseDTO;
    }

    // Get all Report Details
    public GenericResponseDTO<Object> findAllReports(String token) {
        GenericResponseDTO<Object> responseDTO = new GenericResponseDTO<>();
        String userEmail = jwtTokenProvider.getUsername(token);
        Optional<User> user = userRepository.findByEmail(userEmail);

        if (user.isPresent()) {
            List<ReportAnIssue> reportAnIssue = reportIssueRepository.findAll();
            Map<String, Object> data = new HashMap<>();
            responseDTO.setStatus("Success");
            responseDTO.setMessage("Report an issues list!");
            data.put("list", reportAnIssue);
            responseDTO.setData(data);
            return responseDTO;
        }
        responseDTO.setStatus("Failure");
        responseDTO.setMessage("Something went wrong");
        return responseDTO;
    }

    // Get By ID Report Details
    public GenericResponseDTO<Object> getReportIssueById(String id, String token) {
        GenericResponseDTO<Object> responseDTO = new GenericResponseDTO<>();
        String userEmail = jwtTokenProvider.getUsername(token);
        Optional<User> user = userRepository.findByEmail(userEmail);
        if (user.isPresent()) {
            Optional<ReportAnIssue> reportAnIssue = reportIssueRepository.findById(id);
            Map<String, Object> data = new HashMap<>();
            responseDTO.setStatus("Success");
            responseDTO.setMessage("Report an issues");
            data.put("list", reportAnIssue);
            responseDTO.setData(data);
            return responseDTO;
        }
        responseDTO.setStatus("Failure");
        responseDTO.setMessage("Something went wrong");
        return responseDTO;
    }

    private String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex == -1) {
            // No extension found
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "File type Should be 'jpg', 'jpeg', 'png'.");
        }
        return filename.substring(lastDotIndex + 1);
    }
}
