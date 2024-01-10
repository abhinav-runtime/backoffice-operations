package com.backoffice.operations.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backoffice.operations.entity.WebsiteEntity;
import com.backoffice.operations.payloads.WebsiteDTO;
import com.backoffice.operations.repository.WebsiteRepository;

import java.util.List;
import java.util.Optional;

@Service
public class WebsiteService {

    @Autowired
    private WebsiteRepository websiteRepository;

    public List<WebsiteEntity> findAllWebsites() {
        return websiteRepository.findAll();
    }

    public WebsiteEntity createWebsite(WebsiteDTO websiteDTO) {
        WebsiteEntity website = new WebsiteEntity();
        website.setId(websiteDTO.getId());
        website.setWebsiteName(websiteDTO.getWebsiteName());
        website.setWebsiteLink(websiteDTO.getWebsiteLink());
        return websiteRepository.save(website);
    }

    public Optional<WebsiteEntity> getWebsiteById(Long id) {
        return websiteRepository.findById(id);
    }
}
