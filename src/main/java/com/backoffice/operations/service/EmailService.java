package com.backoffice.operations.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backoffice.operations.entity.EmailEntity;
import com.backoffice.operations.payloads.EmailDTO;
import com.backoffice.operations.repository.EmailRepository;

import java.util.List;
import java.util.Optional;

@Service
public class EmailService {

    @Autowired
    private EmailRepository emailRepository;

    public List<EmailEntity> findAllEmails() {
        return emailRepository.findAll();
    }

    public EmailEntity createEmail(EmailDTO emailDTO) {
        EmailEntity email = new EmailEntity();
        email.setId(emailDTO.getId());
        email.setEmailName(emailDTO.getEmailName());
        email.setEmailId(emailDTO.getEmailId());
        return emailRepository.save(email);
    }

    public Optional<EmailEntity> getEmailById(Long id) {
        return emailRepository.findById(id);
    }
}
