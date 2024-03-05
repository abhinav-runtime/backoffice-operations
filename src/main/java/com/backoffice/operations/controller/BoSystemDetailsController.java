package com.backoffice.operations.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backoffice.operations.payloads.BoSystemDetailsResponseDTO;
import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.backoffice.operations.service.BoSystemDetailsService;

@RestController
@RequestMapping("/bo/v1/systemDetails")
public class BoSystemDetailsController {
	@Autowired
	private BoSystemDetailsService boSystemDetailsService;

	@GetMapping
	private ResponseEntity<Object> getSystemDetails() {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		List<BoSystemDetailsResponseDTO> data = boSystemDetailsService.getSystemDetails();
		if (data != null) {
			if (data.size() == 0) {
				response.setMessage("System details not found");
				response.setStatus("Failure");
				response.setData(new HashMap<>());
				return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			}
			response.setData(data);
			response.setMessage("System details fetched successfully");
			response.setStatus("Success");
			return new ResponseEntity<>(response, HttpStatus.OK);
		} else {
			response.setMessage("System details not found");
			response.setStatus("Failure");
			response.setData(new HashMap<>());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
