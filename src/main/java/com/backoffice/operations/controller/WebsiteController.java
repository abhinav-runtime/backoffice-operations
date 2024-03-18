package com.backoffice.operations.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;

import com.backoffice.operations.entity.WebsiteEntity;
import com.backoffice.operations.payloads.WebsiteDTO;
import com.backoffice.operations.service.WebsiteService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/website")
public class WebsiteController {

	@Autowired
	private WebsiteService websiteService;

	@GetMapping
	public List<WebsiteEntity> getAllWebsites(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
		return websiteService.findAllWebsites();
	}

	@PostMapping
	public WebsiteEntity createWebsite(@RequestBody WebsiteDTO websiteDTO) {
		return websiteService.createWebsite(websiteDTO);
	}

	@GetMapping("/{id}")
	public Optional<WebsiteEntity> getWebsiteById(@PathVariable String id,
			@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
		return websiteService.getWebsiteById(id);
	}
}
