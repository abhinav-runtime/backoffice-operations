package com.backoffice.operations.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.backoffice.operations.entity.SystemDetail;
import com.backoffice.operations.payloads.SystemDetailDTO;
import com.backoffice.operations.payloads.ValidationResultDTO;
import com.backoffice.operations.repository.SystemDetailRepository;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/system-details")
public class SystemDetailsController {

	private static final Logger logger = LoggerFactory.getLogger(SystemDetailsController.class);

	@Autowired
	private SystemDetailRepository systemDetailRepository;

	@GetMapping
	public List<SystemDetailDTO> getAllSystemDetails() {
		List<SystemDetail> systemDetails = systemDetailRepository.findAll();
		return systemDetails.stream().map(this::mapToDTO).collect(Collectors.toList());
	}

	@GetMapping("/{id}")
	public SystemDetailDTO getSystemDetailById(@PathVariable String id) {
		SystemDetail systemDetail = systemDetailRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("System detail not found with id: " + id));
		return mapToDTO(systemDetail);
	}

	@PostMapping
	public ValidationResultDTO createSystemDetail(@RequestBody SystemDetailDTO systemDetailDTO) {
		ValidationResultDTO validationResultDTO = new ValidationResultDTO();
		ValidationResultDTO.Data data = new ValidationResultDTO.Data();
		try {
			SystemDetail systemDetail = mapToEntity(systemDetailDTO);
			SystemDetail savedSystemDetail = systemDetailRepository.save(systemDetail);
			if (Objects.nonNull(savedSystemDetail)) {
				validationResultDTO.setStatus("Success");
				validationResultDTO.setMessage("Success");
				data.setUniqueKey(savedSystemDetail.getUniqueKey());
				validationResultDTO.setData(data);
				return validationResultDTO;
			}
			validationResultDTO.setStatus("Failure");
			validationResultDTO.setMessage("Something went wrong");
			data.setUniqueKey(systemDetailDTO.getUniqueKey());
			validationResultDTO.setData(data);
			return validationResultDTO;
		} catch (Exception e) {
			logger.error("ERROR in class SystemDetailsController method createSystemDetail", e);
			validationResultDTO.setStatus("Failure");
			validationResultDTO.setMessage("Something went wrong");
			data.setUniqueKey(systemDetailDTO.getUniqueKey());
			validationResultDTO.setData(data);
			return validationResultDTO;
		}

	}

	@PutMapping("/{id}")
	public ValidationResultDTO updateSystemDetail(@PathVariable String id,
			@RequestBody SystemDetailDTO updatedSystemDetailDTO) {
		ValidationResultDTO validationResultDTO = new ValidationResultDTO();
		ValidationResultDTO.Data data = new ValidationResultDTO.Data();
		try {
			SystemDetail existingSystemDetail = systemDetailRepository.findById(id)
					.orElseThrow(() -> new RuntimeException("System detail not found with id: " + id));
			existingSystemDetail.setName(updatedSystemDetailDTO.getName());
			existingSystemDetail.setType(updatedSystemDetailDTO.getType());
			existingSystemDetail.setStatus(updatedSystemDetailDTO.getStatus());
			SystemDetail updatedSystemDetail = systemDetailRepository.save(existingSystemDetail);
			if (Objects.nonNull(updatedSystemDetail)) {
				validationResultDTO.setStatus("Success");
				validationResultDTO.setMessage("Success");
				data.setUniqueKey(updatedSystemDetail.getUniqueKey());
				validationResultDTO.setData(data);
				return validationResultDTO;
			}
			validationResultDTO.setStatus("Failure");
			validationResultDTO.setMessage("Something went wrong");
			data.setUniqueKey(updatedSystemDetailDTO.getUniqueKey());
			validationResultDTO.setData(data);
			return validationResultDTO;
		} catch (Exception e) {
			logger.error("ERROR in class SystemDetailsController method updateSystemDetail", e);
			validationResultDTO.setStatus("Failure");
			validationResultDTO.setMessage("Something went wrong");
			data.setUniqueKey(updatedSystemDetailDTO.getUniqueKey());
			validationResultDTO.setData(data);
			return validationResultDTO;
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
		entity.setUniqueKey(dto.getUniqueKey());
		return entity;
	}
}
