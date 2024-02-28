package com.backoffice.operations.controller;

import com.backoffice.operations.entity.CivilIdEntity;
import com.backoffice.operations.repository.CivilIdRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import com.backoffice.operations.entity.SystemDetail;
import com.backoffice.operations.payloads.SystemDetailDTO;
import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.backoffice.operations.repository.SystemDetailRepository;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/system-details")
public class SystemDetailsController {

    private static final Logger logger = LoggerFactory.getLogger(SystemDetailsController.class);

    @Autowired
    private SystemDetailRepository systemDetailRepository;

    @Autowired
    private CivilIdRepository civilIdRepository;

    @GetMapping
    public List<SystemDetailDTO> getAllSystemDetails() {
        List<SystemDetail> systemDetails = systemDetailRepository.findAll();
        return systemDetails.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @GetMapping("/all/{civilId}")
    public List<SystemDetailDTO> getAllSystemDetailsByCivilId(@PathVariable String civilId,
                                                              @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        List<SystemDetail> systemDetails = systemDetailRepository.findAllByCivilId(civilId);
        return systemDetails.stream().filter(systemDetail -> StringUtils.hasLength(systemDetail.getStatus()) &&
                        systemDetail.getStatus().equalsIgnoreCase("Active"))
                .map(this::mapToDTO).collect(Collectors.toList());
    }

    @GetMapping("/{civilId}")
    public SystemDetailDTO getSystemDetailById(@PathVariable String id,
                                               @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        SystemDetail systemDetail = systemDetailRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("System detail not found with id: " + id));
        return mapToDTO(systemDetail);
    }

    @PostMapping
    public GenericResponseDTO<Object> createSystemDetail(@RequestBody SystemDetailDTO systemDetailDTO,
                                                         @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        GenericResponseDTO<Object> responseDTO = new GenericResponseDTO<>();
        try {
            Optional<CivilIdEntity> civilIdEntity = civilIdRepository.findById(systemDetailDTO.getUniqueKey());
            SystemDetail systemDetail = mapToEntity(systemDetailDTO, civilIdEntity);
            SystemDetail savedSystemDetail = systemDetailRepository.save(systemDetail);
            if (Objects.nonNull(savedSystemDetail)) {
                Map<String, Object> data = new HashMap<>();
                responseDTO.setStatus("Success");
                responseDTO.setMessage("Success");
                data.put("uniqueKey", savedSystemDetail.getUniqueKey());
                responseDTO.setData(data);
                return responseDTO;
            }
            Map<String, Object> data = new HashMap<>();
            responseDTO.setStatus("Failure");
            responseDTO.setMessage("Something went wrong");
            data.put("uniqueKey", systemDetailDTO.getUniqueKey());
            responseDTO.setData(data);
            return responseDTO;
        } catch (Exception e) {
            logger.error("ERROR in class SystemDetailsController method createSystemDetail", e);
            Map<String, Object> data = new HashMap<>();
            responseDTO.setStatus("Failure");
            responseDTO.setMessage("Something went wrong");
            data.put("uniqueKey", systemDetailDTO.getUniqueKey());
            responseDTO.setData(data);
            return responseDTO;
        }

    }

    @PutMapping("/{id}")
    public GenericResponseDTO<Object> updateSystemDetail(@PathVariable String id,
                                                         @RequestBody SystemDetailDTO updatedSystemDetailDTO,
                                                         @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        GenericResponseDTO<Object> responseDTO = new GenericResponseDTO<>();
        try {
            SystemDetail existingSystemDetail = systemDetailRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("System detail not found with id: " + id));
            existingSystemDetail.setStatus(updatedSystemDetailDTO.getStatus());
            SystemDetail updatedSystemDetail = systemDetailRepository.save(existingSystemDetail);
            if (Objects.nonNull(updatedSystemDetail)) {
                Map<String, Object> data = new HashMap<>();
                responseDTO.setStatus("Success");
                responseDTO.setMessage("Success");
                data.put("uniqueKey", updatedSystemDetail.getUniqueKey());
                responseDTO.setData(data);
                return responseDTO;
            }
            Map<String, Object> data = new HashMap<>();
            responseDTO.setStatus("Failure");
            responseDTO.setMessage("Something went wrong");
            data.put("uniqueKey", updatedSystemDetailDTO.getUniqueKey());
            responseDTO.setData(data);
            return responseDTO;
        } catch (Exception e) {
            logger.error("ERROR in class SystemDetailsController method updateSystemDetail", e);
            Map<String, Object> data = new HashMap<>();
            responseDTO.setStatus("Failure");
            responseDTO.setMessage("Something went wrong");
            data.put("uniqueKey", updatedSystemDetailDTO.getUniqueKey());
            responseDTO.setData(data);
            return responseDTO;
        }
    }

    @DeleteMapping("/{id}")
    public void deleteSystemDetail(@PathVariable String id) {
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
        dto.setUniqueKey(entity.getUniqueKey());
        dto.setCivilId(entity.getCivilId());
        return dto;
    }

    // Helper method to map DTO to entity
    private SystemDetail mapToEntity(SystemDetailDTO dto, Optional<CivilIdEntity> civilIdEntity) {
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
        entity.setUniqueKey(dto.getUniqueKey());
        if (civilIdEntity.isPresent()) {
            entity.setCivilId(civilIdEntity.get().getCivilId());
        }
        return entity;
    }
}
