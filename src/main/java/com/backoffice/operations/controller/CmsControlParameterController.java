package com.backoffice.operations.controller;


import com.backoffice.operations.payloads.CmsControlParameterDTO;
import com.backoffice.operations.service.CmsControlParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cms-control-parameters")
public class CmsControlParameterController {

    private final CmsControlParameterService service;

    @Autowired
    public CmsControlParameterController(CmsControlParameterService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<CmsControlParameterDTO>> getAllParameters() {
        List<CmsControlParameterDTO> parameters = service.getAllParameters();
        return new ResponseEntity<>(parameters, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CmsControlParameterDTO> getParameterById(@PathVariable String id) {
        CmsControlParameterDTO parameter = service.getParameterById(id);
        if (parameter != null) {
            return new ResponseEntity<>(parameter, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<CmsControlParameterDTO> createParameter(@RequestBody CmsControlParameterDTO parameterDTO) {
        CmsControlParameterDTO createdParameter = service.createParameter(parameterDTO);
        return new ResponseEntity<>(createdParameter, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CmsControlParameterDTO> updateParameter(@PathVariable String id, @RequestBody CmsControlParameterDTO parameterDTO) {
        CmsControlParameterDTO updatedParameter = service.updateParameter(id, parameterDTO);
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