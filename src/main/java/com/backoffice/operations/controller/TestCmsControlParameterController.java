package com.backoffice.operations.controller;

import com.backoffice.operations.payloads.TestCmsControlParameterDTO;
import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.backoffice.operations.service.TestCmsControlParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/test-cms-control-parameters")
public class TestCmsControlParameterController {

	private final TestCmsControlParameterService service;

	@Autowired
	public TestCmsControlParameterController(TestCmsControlParameterService service) {
		this.service = service;
	}

	@GetMapping
	public ResponseEntity<Object> getAllParameters(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		List<TestCmsControlParameterDTO> parameters = service.getAllParameters();
		response.setData(parameters);
		response.setStatus("Success");
		response.setMessage("Success");
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Object> getParameterById(@PathVariable String id,
			@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		TestCmsControlParameterDTO parameter = service.getParameterById(id);
		if (parameter != null) {
			response.setData(parameter);
			response.setStatus("Success");
			response.setMessage("Success");
			return new ResponseEntity<>(response, HttpStatus.OK);
		} else {
			response.setStatus("Failure");
			response.setMessage("Parameter not found");
			response.setData(new HashMap<>());
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping
	public ResponseEntity<Object> createParameter(@RequestBody TestCmsControlParameterDTO parameterDTO) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		TestCmsControlParameterDTO createdParameter = service.createParameter(parameterDTO);
		response.setData(createdParameter);
		response.setStatus("Success");
		response.setMessage("Successfully create");
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Object> updateParameter(@PathVariable String id,
			@RequestBody TestCmsControlParameterDTO parameterDTO) {
		TestCmsControlParameterDTO updatedParameter = service.updateParameter(id, parameterDTO);
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		if (updatedParameter != null) {
			response.setData(updatedParameter);
			response.setStatus("Success");
			response.setMessage("Successfully updated");
			return new ResponseEntity<>(response, HttpStatus.OK);
		} else {
			response.setStatus("Failure");
			response.setMessage("Parameter not found");
			response.setData(new HashMap<>());
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Object> deleteParameter(@PathVariable String id) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		service.deleteParameter(id);
		response.setStatus("Success");
		response.setMessage("Successfully deleted");
		response.setData(new HashMap<>());
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}