package com.backoffice.operations.payloads;

import jakarta.persistence.Column;

public class ReportAnIssueDto {
	
	private String id;
    private String typeOfIssue;
    @Column(length=255)
    private String message;
    private String name;
    private String type;
    private String filePath;
    private String lang;
    
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTypeOfIssue() {
		return typeOfIssue;
	}
	public void setTypeOfIssue(String typeOfIssue) {
		this.typeOfIssue = typeOfIssue;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getLang() {
		return lang;
	}
	public void setLang(String lang) {
		this.lang = lang;
	}	
}
