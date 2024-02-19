package com.backoffice.operations.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.backoffice.operations.entity.ReportAnIssue;
import com.backoffice.operations.exceptions.BlogAPIException;
import com.backoffice.operations.payloads.ReportAnIssueDto;
import com.backoffice.operations.repository.ReportIssueRepository;


@Service
public class ReportIssueService {
	
	@Autowired
	private ReportIssueRepository reportIssueRepository;
	
    private final List<String> ALLOWED_FILE_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png");
    private final List<String> Types_Of_Issue = Arrays.asList("Transaction Issue", "Payment processing outages", "Long Waiting Times");
    private final long MAX_FILE_SIZE_BYTES = 1 * 1024 * 1024; // 1MB
	
	
	public String postReportIssue(ReportAnIssueDto reportAnIssueDto, String path, MultipartFile file) throws IOException{
		
		//file size validation
    	if (file.getSize() > MAX_FILE_SIZE_BYTES) {
    		throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Size exceeds maximum range!.");
        }
    	
    	//file extension validation
    	String fileExtension = getFileExtension(file.getOriginalFilename());
        if (!ALLOWED_FILE_EXTENSIONS.contains(fileExtension.toLowerCase())) {
        	throw new BlogAPIException(HttpStatus.BAD_REQUEST, "File extension is Invalid!.");
        }
    	
        //Types of Issue validation
        String type_of_issue = reportAnIssueDto.getTypeOfIssue();
        if (!Types_Of_Issue.contains(type_of_issue)) {
        	throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Only selective issues applied!.");
        }
		
		//FullPath
    	String filePath = path+file.getOriginalFilename();
    	
    	//Create folder if not created
    	File f = new File(path);
    	if(!f.exists())
    	{
    		f.mkdir();
    	}
    	
    	//file copy
    	Files.copy(file.getInputStream(), Paths.get(filePath));
        
    	ReportAnIssue reportAnIssue = new ReportAnIssue();
    	reportAnIssue.setName(file.getOriginalFilename());
    	reportAnIssue.setType(file.getContentType());
    	reportAnIssue.setFilePath(filePath);
    	reportAnIssue.setTypeOfIssue(reportAnIssueDto.getTypeOfIssue());
    	reportAnIssue.setMessage(reportAnIssueDto.getMessage());
    	reportIssueRepository.save(reportAnIssue);
        
        return "Issue reported successfully!";
    }
    
    	//Get all Report Details
	public List<ReportAnIssue> findAllReports() {
		return reportIssueRepository.findAll();
	}

		//Get By ID Report Details
	public Optional<ReportAnIssue> getReportIssueById(String id) {
	        return reportIssueRepository.findById(id);
	}
	
	private String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex == -1) {
        	// No extension found
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "File must contain extensions like 'jpg', 'jpeg', 'png'.");
        }
        return filename.substring(lastDotIndex + 1);
    }	
}
