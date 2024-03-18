package com.backoffice.operations.controller;
import com.backoffice.operations.payloads.TestCmsControlParameterDTO;
import com.backoffice.operations.service.TestCmsControlParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;

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
    public ResponseEntity<List<TestCmsControlParameterDTO>> getAllParameters(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        List<TestCmsControlParameterDTO> parameters = service.getAllParameters();
        return new ResponseEntity<>(parameters, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TestCmsControlParameterDTO> getParameterById(@PathVariable String id, @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        TestCmsControlParameterDTO parameter = service.getParameterById(id);
        if (parameter != null) {
            return new ResponseEntity<>(parameter, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<TestCmsControlParameterDTO> createParameter(@RequestBody TestCmsControlParameterDTO parameterDTO) {
        TestCmsControlParameterDTO createdParameter = service.createParameter(parameterDTO);
        return new ResponseEntity<>(createdParameter, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TestCmsControlParameterDTO> updateParameter(@PathVariable String id, @RequestBody TestCmsControlParameterDTO parameterDTO) {
        TestCmsControlParameterDTO updatedParameter = service.updateParameter(id, parameterDTO);
        if (updatedParameter != null) {
            return new ResponseEntity<>(updatedParameter, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteParameter(@PathVariable String id) {
        service.deleteParameter(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}