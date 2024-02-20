package com.backoffice.operations.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


import com.backoffice.operations.entity.ReportAnIssue;
import com.backoffice.operations.payloads.ReportAnIssueDto;
import com.backoffice.operations.service.ReportIssueService;


@RestController
@RequestMapping("/api/ReportIssue")
public class ReportIssueController {
	
	
	@Autowired
	private ReportIssueService reportIssueService;
	
	@Value("${project.image}")
	private String path;
	
	@PostMapping("/postReport")
    public String postReportIssue(
    							@RequestParam("typeOfIssue") String text, 
    							@RequestParam("message") String text1,
    							@RequestParam("lang") String lang,
    							@RequestParam("file") MultipartFile file) throws IOException {
		ReportAnIssueDto reportAnIssueDto = new ReportAnIssueDto();
		reportAnIssueDto.setTypeOfIssue(text);
		reportAnIssueDto.setMessage(text1);
		reportAnIssueDto.setLang(lang);
        return reportIssueService.postReportIssue(reportAnIssueDto, path, file);
    }
	
	@GetMapping("/getReports")
    public List<ReportAnIssue> getAllReports() {
        return reportIssueService.findAllReports();
    }
	
	@GetMapping("/getReportById/{id}")
    public Optional<ReportAnIssue> getReportIssueById(@PathVariable String id) {
        return reportIssueService.getReportIssueById(id);
    }
	
}
