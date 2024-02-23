package com.backoffice.operations.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.backoffice.operations.entity.EmailEntity;
import com.backoffice.operations.payloads.EmailDTO;
import com.backoffice.operations.service.EmailService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @GetMapping
    public List<EmailEntity> getAllEmails() {
        return emailService.findAllEmails();
    }

    @PostMapping
    public EmailEntity createEmail(@RequestBody EmailDTO emailDTO) {
        return emailService.createEmail(emailDTO);
    }

    @GetMapping("/{id}")
    public Optional<EmailEntity> getEmailById(@PathVariable String id) {
        return emailService.getEmailById(id);
    }
}
