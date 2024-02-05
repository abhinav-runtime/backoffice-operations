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
        existingSystemDetail.setName(updatedSystemDetailDTO.getName());
        existingSystemDetail.setType(updatedSystemDetailDTO.getType());
        existingSystemDetail.setStatus(updatedSystemDetailDTO.getStatus());
        SystemDetail updatedSystemDetail = systemDetailRepository.save(existingSystemDetail);
        return mapToDTO(updatedSystemDetail);
    }

    @DeleteMapping("/{id}")
    public void deleteSystemDetail(@PathVariable Long id) {
        systemDetailRepository.deleteById(id);
    }

    // Helper method to map entity to DTO
    private SystemDetailDTO mapToDTO(SystemDetail entity) {
        SystemDetailDTO dto = new SystemDetailDTO();
        dto.setDeviceId(entity.getDeviceId());
        dto.setName(entity.getName());
        dto.setType(entity.getType());
        dto.setModel(entity.getModel());
        dto.setStatus(entity.getStatus());
        dto.setOsVersion(entity.getOsVersion());
        dto.setAppVersion(entity.getAppVersion());
        dto.setCarrier(entity.getCarrier());
        dto.setLocation(entity.getLocation());
        dto.setIpAddress(entity.getIpAddress());
        dto.setCreated(entity.getCreated());
        dto.setLastSeen(entity.getLastSeen());
        dto.setAction(entity.getAction());
        dto.setUniqueKey(entity.getUniqueKey());
        return dto;
    }

    // Helper method to map DTO to entity
    private SystemDetail mapToEntity(SystemDetailDTO dto) {
        SystemDetail entity = new SystemDetail();
        entity.setDeviceId(dto.getDeviceId());
        entity.setName(dto.getName());
        entity.setType(dto.getType());
        entity.setModel(dto.getModel());
        entity.setStatus(dto.getStatus());
        entity.setOsVersion(dto.getOsVersion());
        entity.setAppVersion(dto.getAppVersion());
        entity.setCarrier(dto.getCarrier());
        entity.setLocation(dto.getLocation());
        entity.setIpAddress(dto.getIpAddress());
        entity.setCreated(dto.getCreated());
        entity.setLastSeen(dto.getLastSeen());
        entity.setAction(dto.getAction());
        entity.setUniqueKey(dto.getUniqueKey());
        return entity;
    }
}
