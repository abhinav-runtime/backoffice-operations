package com.backoffice.operations.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.backoffice.operations.entity.AboutUs;
import com.backoffice.operations.repository.AboutUsRepository;

import java.util.List;

@RestController
@RequestMapping("/api/about-us")
public class AboutUsController {

    @Autowired
    private AboutUsRepository aboutUsRepository;

    @GetMapping
    public List<AboutUs> getAllAboutUs() {
        return aboutUsRepository.findAll();
    }

    @GetMapping("/{id}")
    public AboutUs getAboutUsById(@PathVariable Long id) {
        return aboutUsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("About Us not found with id: " + id));
    }

    @PostMapping
    public AboutUs createAboutUs(@RequestBody AboutUs aboutUs) {
        return aboutUsRepository.save(aboutUs);
    }

    @PutMapping("/{id}")
    public AboutUs updateAboutUs(@PathVariable Long id, @RequestBody AboutUs updatedAboutUs) {
        AboutUs existingAboutUs = aboutUsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("About Us not found with id: " + id));

        // Update the fields based on your requirements
        existingAboutUs.setPhoneNumber(updatedAboutUs.getPhoneNumber());
        existingAboutUs.setEmailAddress(updatedAboutUs.getEmailAddress());
        existingAboutUs.setWebsiteLink(updatedAboutUs.getWebsiteLink());

        return aboutUsRepository.save(existingAboutUs);
    }

    @DeleteMapping("/{id}")
    public void deleteAboutUs(@PathVariable Long id) {
        aboutUsRepository.deleteById(id);
    }
}