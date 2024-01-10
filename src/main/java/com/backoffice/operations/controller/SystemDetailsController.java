package com.backoffice.operations.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.backoffice.operations.entity.SystemDetail;
import com.backoffice.operations.payloads.SystemDetailDTO;
import com.backoffice.operations.repository.SystemDetailRepository;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/system-details")
public class SystemDetailsController {

    @Autowired
    private SystemDetailRepository systemDetailRepository;

    @GetMapping
    public List<SystemDetailDTO> getAllSystemDetails() {
        List<SystemDetail> systemDetails = systemDetailRepository.findAll();
        return systemDetails.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public SystemDetailDTO getSystemDetailById(@PathVariable Long id) {
        SystemDetail systemDetail = systemDetailRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("System detail not found with id: " + id));
        return mapToDTO(systemDetail);
    }

    @PostMapping
    public SystemDetailDTO createSystemDetail(@RequestBody SystemDetailDTO systemDetailDTO) {
        SystemDetail systemDetail = mapToEntity(systemDetailDTO);
        SystemDetail savedSystemDetail = systemDetailRepository.save(systemDetail);
        return mapToDTO(savedSystemDetail);
    }

    @PutMapping("/{id}")
    public SystemDetailDTO updateSystemDetail(@PathVariable Long id, @RequestBody SystemDetailDTO updatedSystemDetailDTO) {
        SystemDetail existingSystemDetail = systemDetailRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("System detail not found with id: " + id));

        // Update fields based on your requirements
        existingSystemDetail.setName(updatedSystemDetailDTO.getName());
        existingSystemDetail.setType(updatedSystemDetailDTO.getType());
        existingSystemDetail.setStatus(updatedSystemDetailDTO.getStatus());
        // ...

        SystemDetail updatedSystemDetail = systemDetailRepository.save(existingSystemDetail);
        return mapToDTO(updatedSystemDetail);
    }

    @DeleteMapping("/{id}")
    public void deleteSystemDetail(@PathVariable Long id) {
        systemDetailRepository.deleteById(id);
    }

    // Helper method to map entity to DTO
    private SystemDetailDTO mapToDTO(SystemDetail systemDetail) {
        SystemDetailDTO dto = new SystemDetailDTO();
        // Map fields from entity to DTO
        dto.setDeviceId(systemDetail.getDeviceId());
        dto.setName(systemDetail.getName());
        dto.setType(systemDetail.getType());
        dto.setModel(systemDetail.getModel());
        dto.setStatus(systemDetail.getStatus());
        dto.setAppVersion(systemDetail.getAppVersion());
        dto.setCarrier(systemDetail.getCarrier());
        dto.setLocation(systemDetail.getLocation());
        dto.setIpAddress(systemDetail.getIpAddress());
        dto.setCreated(systemDetail.getCreated());
        dto.setLastSeen(systemDetail.getLastSeen());
        dto.setAction(systemDetail.getAction());
        return dto;
    }

    // Helper method to map DTO to entity
    private SystemDetail mapToEntity(SystemDetailDTO dto) {
        SystemDetail systemDetail = new SystemDetail();
        // Map fields from DTO to entity
        systemDetail.setDeviceId(dto.getDeviceId());
        systemDetail.setName(dto.getName());
        systemDetail.setType(dto.getType());
        systemDetail.setModel(dto.getModel());
        systemDetail.setStatus(dto.getStatus());
        systemDetail.setAppVersion(dto.getAppVersion());
        systemDetail.setCarrier(dto.getCarrier());
        systemDetail.setLocation(dto.getLocation());
        systemDetail.setIpAddress(dto.getIpAddress());
        systemDetail.setCreated(dto.getCreated());
        systemDetail.setLastSeen(dto.getLastSeen());
        systemDetail.setAction(dto.getAction());
        return systemDetail;
    }
}
