package com.backoffice.operations.payloads;

public class UrlResponse {
	
	private String url;
    
    // Default constructor
    public UrlResponse() {}

    // Parameterized constructor
    public UrlResponse(String url) {
        this.url = url;
    }

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}    

}
