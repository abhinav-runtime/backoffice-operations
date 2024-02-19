package com.backoffice.operations.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backoffice.operations.entity.ContactUSEntity;
import com.backoffice.operations.payloads.ContactUSDTO;
import com.backoffice.operations.repository.ContactUSRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ContactUSService {

    @Autowired
    private ContactUSRepository contactUSRepository;

    public List<ContactUSEntity> findAllContacts() {
        return contactUSRepository.findAll();
    }

    public ContactUSEntity createContact(ContactUSDTO contactDTO) {
        ContactUSEntity contact = new ContactUSEntity();
        contact.setContactType(contactDTO.getContactType());
        contact.setContactNo(contactDTO.getContactNo());
        return contactUSRepository.save(contact);
    }

    public Optional<ContactUSEntity> getContactById(Long id) {
        return contactUSRepository.findById(id);
    }
}
