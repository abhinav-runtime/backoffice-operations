package com.backoffice.operations.entity;

import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
//TODO: Capital letters not allowed.
@Table(name = "az_report_Issue_bk")
public class ReportAnIssue {

	@Id
	@GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", unique = true, nullable = false)
	private String id;
	
	@Column(name="typeOfIssue")
    private String typeOfIssue;
	
	@Column(name="message", length = 255)
    private String message;
    
	@Column(name="name")
	private String name;
	
	@Column(name="type")
    private String type;

	//TODO: changes this into camel case.
	@Column(name="filepath")
    private String filePath;

	//TODO: changes this into camel case.
	@Column(name="Date modified")
	private LocalDateTime time;

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
	
	public LocalDateTime getTime() {
		return time;
	}

	public void setTime(LocalDateTime time) {
		this.time = time;
	}
}
