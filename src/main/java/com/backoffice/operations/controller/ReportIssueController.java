package com.backoffice.operations.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


import com.backoffice.operations.entity.ReportAnIssue;
import com.backoffice.operations.payloads.ReportAnIssueDto;
import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.backoffice.operations.service.ReportIssueService;


@RestController
@RequestMapping("/api/ReportIssue")
public class ReportIssueController {
	
	
	@Autowired
	private ReportIssueService reportIssueService;
	
	@Value("${project.image}")
	private String path;
	
	@PostMapping("/postReport")
	public ResponseEntity<GenericResponseDTO<Object>> postReportIssue(
    							@RequestParam("typeOfIssue") String text, 
    							@RequestParam("message") String text1,
    							@RequestParam("lang") String lang,
    							@RequestParam("file") MultipartFile file,
    							@RequestHeader(HttpHeaders.AUTHORIZATION) String token) throws IOException {
		ReportAnIssueDto reportAnIssueDto = new ReportAnIssueDto();
		reportAnIssueDto.setTypeOfIssue(text);
		reportAnIssueDto.setMessage(text1);
		reportAnIssueDto.setLang(lang);
		GenericResponseDTO<Object> validationResultDTO = reportIssueService.postReportIssue(reportAnIssueDto, path, file, token.substring("Bearer ".length()));
		return ResponseEntity.ok(validationResultDTO);
    }
	
	@GetMapping("/getReports")
	public ResponseEntity<GenericResponseDTO<Object>> getAllReports(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
		GenericResponseDTO<Object> validationResultDTO = reportIssueService.findAllReports(token.substring("Bearer ".length()));
		return ResponseEntity.ok(validationResultDTO);
    }
	
	@GetMapping("/getReportById/{id}")
	public ResponseEntity<GenericResponseDTO<Object>> getReportIssueById(@PathVariable String id, @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
		GenericResponseDTO<Object> validationResultDTO = reportIssueService.getReportIssueById(id, token.substring("Bearer ".length()));
		return ResponseEntity.ok(validationResultDTO);
    }
	
}
