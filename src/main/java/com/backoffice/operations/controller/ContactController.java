package com.backoffice.operations.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.HttpHeaders;
import com.backoffice.operations.entity.ContactUSEntity;
import com.backoffice.operations.payloads.ContactUSDTO;
import com.backoffice.operations.service.ContactUSService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/contacts")
public class ContactController {

	@Autowired
	private ContactUSService contactUSService;

	@GetMapping
	public List<ContactUSEntity> getAllContacts(@RequestHeader(HttpHeaders.AUTHORIZATION)  String token) {
		return contactUSService.findAllContacts();
	}

	@PostMapping
	public ContactUSEntity createContact(@RequestBody ContactUSDTO contactDTO) {
		return contactUSService.createContact(contactDTO);
	}

	@GetMapping("/{id}")
	public Optional<ContactUSEntity> getContactById(@PathVariable String id,
			@RequestHeader(HttpHeaders.AUTHORIZATION)  String token) {
		return contactUSService.getContactById(id);
	}
}
